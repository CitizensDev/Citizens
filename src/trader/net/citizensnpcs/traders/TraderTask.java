package net.citizensnpcs.traders;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.Economy;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TraderTask implements Listener {
    private final HumanNPC npc;
    private final Player player;
    private final TraderMode mode;
    private int prevTraderSlot = -1;
    private int prevPlayerSlot = -1;

    // Gets run every tick, checks the inventory for changes.
    public TraderTask(HumanNPC npc, Player player, TraderMode mode) {
        this.npc = npc;
        this.player = player;
        this.mode = mode;
        sendJoinMessage();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getPlayer().equals(player))
            return;
        HandlerList.unregisterAll(this);
        sendLeaveMessage();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (mode == TraderMode.STOCK)
            return;
        if (event.getInventory().getHolder().equals(player)) {
            handlePlayerClick(event);
        } else if (event.getInventory().getHolder().equals(npc.getPlayer())) {
            handleTraderClick(event);
        }
    }

    @SuppressWarnings("deprecation")
    public void handleTraderClick(InventoryClickEvent event) {
        Inventory npcInv = npc.getInventory();
        int slot = event.getSlot();
        Stockable stockable = getStockable(npcInv.getItem(slot), "sold", false);
        if (stockable == null) {
            event.setCancelled(true);
            return;
        }
        if (prevTraderSlot != slot) {
            prevTraderSlot = slot;
            sendStockableMessage(stockable);
            event.setCancelled(true);
            return;
        }
        prevTraderSlot = slot;
        prevPlayerSlot = -1;
        if (checkMiscellaneous(npcInv, stockable, true)) {
            event.setCancelled(true);
            return;
        }
        ItemStack buying = stockable.getStocking().clone();
        Economy.pay(player, stockable.getPrice().getPrice());
        if (mode != TraderMode.INFINITE) {
            InventoryUtils.removeItems(npc.getPlayer(), buying, slot);
        }
        Map<Integer, ItemStack> unbought = player.getInventory().addItem(buying);
        if (unbought.size() >= 1) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Not enough room in your inventory to add "
                    + MessageUtils.getStackString(buying, ChatColor.RED) + ".");
            return;
        }
        double price = stockable.getPrice().getPrice();
        npc.setBalance(npc.getBalance() + price);
        npc.getPlayer().updateInventory();
        player.updateInventory();
        player.sendMessage(ChatColor.GREEN + "Transaction successful.");
    }

    @SuppressWarnings("deprecation")
    public void handlePlayerClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        Inventory playerInv = player.getInventory();
        Stockable stockable = getStockable(playerInv.getItem(slot), "bought", true);
        if (stockable == null) {
            event.setCancelled(true);
            return;
        }
        if (prevPlayerSlot != slot) {
            prevPlayerSlot = slot;
            sendStockableMessage(stockable);
            event.setCancelled(true);
            return;
        }
        prevPlayerSlot = slot;
        prevTraderSlot = -1;
        if (checkMiscellaneous(playerInv, stockable, false)) {
            event.setCancelled(true);
            return;
        }
        ItemStack selling = stockable.getStocking().clone();
        if (mode != TraderMode.INFINITE) {
            Economy.pay(npc, stockable.getPrice().getPrice());
        }
        InventoryUtils.removeItems(player, selling, slot);
        Map<Integer, ItemStack> unsold = new HashMap<Integer, ItemStack>();
        Trader trader = npc.getType("trader");
        if (mode != TraderMode.INFINITE) {
            if (!trader.isLocked())
                unsold = npc.getInventory().addItem(selling);
        }
        if (unsold.size() >= 1) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Not enough room available to add "
                    + MessageUtils.getStackString(selling, ChatColor.RED) + " to the trader's stock.");
            event.setCancelled(true);
            return;
        }
        double price = stockable.getPrice().getPrice();
        Economy.add(player.getName(), price);
        npc.getPlayer().updateInventory();
        player.updateInventory();
        player.sendMessage(ChatColor.GREEN + "Transaction successful.");
    }

    private boolean checkMiscellaneous(Inventory inv, Stockable stockable, boolean buying) {
        ItemStack stocking = stockable.getStocking();
        if (buying) {
            if (!InventoryUtils.has(npc.getPlayer(), stocking)) {
                sendNoMoneyMessage(stocking, false);
                return true;
            }
            if (!Economy.hasEnough(player, stockable.getPrice().getPrice())) {
                sendNoMoneyMessage(stocking, true);
                return true;
            }
        } else {
            if (!InventoryUtils.has(player, stocking)) {
                sendNoMoneyMessage(stocking, true);
                return true;
            }
            if (mode != TraderMode.INFINITE && !Economy.hasEnough(npc, stockable.getPrice().getPrice())) {
                sendNoMoneyMessage(stocking, false);
                return true;
            }
        }

        return false;
    }

    private void sendNoMoneyMessage(ItemStack stocking, boolean selling) {
        String start = "The trader doesn't";
        if (selling) {
            start = "You don't";
        }
        player.sendMessage(ChatColor.RED + start + " have enough money available to buy "
                + MessageUtils.getStackString(stocking) + ".");
    }

    private void sendStockableMessage(Stockable stockable) {
        String[] message = TraderMessageUtils.getStockableMessage(stockable, ChatColor.AQUA).split("for");
        player.sendMessage(ChatColor.AQUA + "Item: " + message[0].trim());
        player.sendMessage(ChatColor.AQUA + "Price: " + message[1].trim());
        player.sendMessage(ChatColor.GOLD + "Click to confirm.");
    }

    private Stockable getStockable(ItemStack item, String keyword, boolean selling) {
        // durability needs to be reset to 0 for tools / weapons / armor
        short durability = item.getDurability();
        if (InventoryUtils.isTool(item.getTypeId()) || InventoryUtils.isArmor(item.getTypeId())) {
            durability = 0;
        }
        Trader trader = npc.getType("trader");
        if (!trader.isStocked(item.getTypeId(), durability, selling)) {
            player.sendMessage(StringUtils.wrap(MessageUtils.getItemName(item.getTypeId()), ChatColor.RED)
                    + " isn't being " + keyword + " here.");
            return null;
        }
        return trader.getStockable(item.getTypeId(), durability, selling);
    }

    private void sendJoinMessage() {
        switch (mode) {
        case INFINITE:
        case NORMAL:
            player.sendMessage(ChatColor.GREEN + "Transaction log");
            player.sendMessage(ChatColor.GOLD + "-------------------------------");
            break;
        case STOCK:
            player.sendMessage(ChatColor.GOLD + "Stocking of " + StringUtils.wrap(npc.getName(), ChatColor.GOLD)
                    + " started.");
            break;
        }
    }

    private void sendLeaveMessage() {
        switch (mode) {
        case INFINITE:
        case NORMAL:
            player.sendMessage(ChatColor.GOLD + "-------------------------------");
            break;
        case STOCK:
            player.sendMessage(ChatColor.GOLD + "Stocking of " + StringUtils.wrap(npc.getName(), ChatColor.GOLD)
                    + " finished.");
            break;
        }
    }
}