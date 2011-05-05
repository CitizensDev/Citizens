package com.fullwall.Citizens.Traders;

import java.util.HashMap;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryPlayer;
import net.minecraft.server.Packet103SetSlot;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.Payment;
import com.fullwall.Citizens.Traders.TraderInterface.Mode;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderTask implements Runnable {
	private HumanNPC npc;
	private CraftPlayer player;
	private int taskID;
	private int previousNPCClickedSlot = 0;
	private int previousPlayerClickedSlot = 0;
	private Citizens plugin;
	private PlayerInventory previousNPCInv;
	private PlayerInventory previousPlayerInv;
	private Mode mode;
	private EntityPlayer eplayer;
	private boolean stop;

	/**
	 * Gets run every tick, checks the inventory for changes.
	 * 
	 * @param npc
	 * @param player
	 * @param plugin
	 * @param mode
	 */
	public TraderTask(HumanNPC npc, Player player, Citizens plugin, Mode mode) {
		this.npc = npc;
		this.player = (CraftPlayer) player;
		this.eplayer = this.player.getHandle();
		this.plugin = plugin;
		// Create the inventory objects
		this.previousNPCInv = new CraftInventoryPlayer(
				new InventoryPlayer(null));
		this.previousPlayerInv = new CraftInventoryPlayer(new InventoryPlayer(
				null));
		// clone the items to the newly created inventory objects
		clonePlayerInventory(npc.getInventory(), this.previousNPCInv);
		clonePlayerInventory(player.getInventory(), this.previousPlayerInv);

		this.mode = mode;
		sendJoinMessage();
	}

	@Override
	public void run() {
		if (stop)
			return;
		if (npc == null || player == null
				|| eplayer.activeContainer == eplayer.defaultContainer) {
			kill();
			return;
		}
		if (mode == Mode.STOCK)
			return;
		// If the player cursor is empty (no itemstack in it).
		if (player.getHandle().inventory.j() == null)
			return;
		stop = true;
		int count = 0;

		boolean found = false;
		for (ItemStack i : npc.getInventory().getContents()) {
			if (!previousNPCInv.getItem(count).equals(i)
					&& previousNPCInv.getItem(count).getTypeId() == eplayer.inventory
							.j().id) {
				found = true;
				handleNPCItemClicked(count, npc.getInventory());
				break;
			}
			count += 1;
		}

		count = 0;
		if (!found) {
			for (ItemStack i : player.getInventory().getContents()) {
				if (!previousPlayerInv.getItem(count).equals(i)
						&& previousPlayerInv.getItem(count).getTypeId() == eplayer.inventory
								.j().id) {
					handlePlayerItemClicked(count, player.getInventory());
					break;
				}
				count += 1;
			}
		}

		clonePlayerInventory(npc.getInventory(), this.previousNPCInv);
		clonePlayerInventory(player.getInventory(), this.previousPlayerInv);

		// Set the itemstack in the player's cursor to null.
		eplayer.inventory.b((net.minecraft.server.ItemStack) null);
		// Get rid of the picture on the cursor.
		Packet103SetSlot packet = new Packet103SetSlot(-1, -1, null);
		eplayer.netServerHandler.sendPacket(packet);
		stop = false;
	}

	private void handleNPCItemClicked(int slot, PlayerInventory npcInv) {
		npcInv.setItem(slot, previousNPCInv.getItem(slot));
		ItemStack i = npcInv.getItem(slot);
		if (!(npc.getTraderNPC().isStocked(i.getTypeId(), i.getData(), false))) {
			player.sendMessage(StringUtils.yellowify(i.getType().name(),
					ChatColor.RED) + " isn't being sold here.");
			return;
		}
		Stockable stockable = npc.getTraderNPC().getStockable(i.getTypeId(),
				false);
		ItemStack buying = stockable.getStocking();
		if (previousNPCClickedSlot != slot) {
			previousNPCClickedSlot = slot;
			player.sendMessage(ChatColor.AQUA
					+ "Buying "
					+ MessageUtils.getStockableMessage(stockable,
							ChatColor.AQUA) + ".");
			return;
		}
		previousNPCClickedSlot = slot;
		int amount = npcInv.getItem(slot).getAmount();
		if (amount - buying.getAmount() <= 0) {
			player.sendMessage(ChatColor.RED
					+ "Need at least "
					+ StringUtils.yellowify(buying.getAmount() + " "
							+ buying.getType().name(), ChatColor.RED)
					+ "(s) on the clicked stack.");
			return;
		}
		if (!EconomyHandler.canBuy(new Payment(stockable.getPrice()), player)) {
			player.sendMessage(ChatColor.RED
					+ "Not enough money to buy "
					+ StringUtils.yellowify(buying.getAmount() + " "
							+ buying.getType().name(), ChatColor.RED) + "(s).");
			return;
		}
		HashMap<Integer, ItemStack> unbought = player.getInventory().addItem(
				buying);
		if (unbought.size() >= 1) {
			player.getInventory().setContents(previousPlayerInv.getContents());
			player.sendMessage(ChatColor.RED
					+ "Not enough room in your inventory to add "
					+ StringUtils.yellowify(buying.getAmount() + " "
							+ buying.getType().name(), ChatColor.RED) + "(s).");
			return;
		}
		EconomyHandler.pay(new Payment(stockable.getPrice()), player);
		player.sendMessage(ChatColor.GREEN + "Transaction successful.");
		// npcInv.setContents(sortInventory(npc.getInventory()));
	}

	private void handlePlayerItemClicked(int slot, PlayerInventory playerInv) {
		playerInv.setItem(slot, previousPlayerInv.getItem(slot));
		ItemStack i = playerInv.getItem(slot);
		if (!npc.getTraderNPC().isStocked(i.getTypeId(), i.getData(), true)) {
			player.sendMessage(StringUtils.yellowify(i.getType().name(),
					ChatColor.RED) + " isn't being purchased here.");
			return;
		}
		Stockable stockable = npc.getTraderNPC().getStockable(i.getTypeId(),
				true);
		ItemStack selling = stockable.getStocking();
		if (previousPlayerClickedSlot != slot) {
			previousPlayerClickedSlot = slot;
			player.sendMessage(ChatColor.AQUA
					+ "Selling "
					+ MessageUtils.getStockableMessage(stockable,
							ChatColor.AQUA) + ".");
			return;
		}
		previousPlayerClickedSlot = slot;
		int amount = playerInv.getItem(slot).getAmount();
		if (amount - selling.getAmount() <= 0) {
			player.sendMessage(ChatColor.RED
					+ "Need at least "
					+ StringUtils.yellowify(selling.getAmount() + " "
							+ selling.getType().name(), ChatColor.RED)
					+ "(s) on the clicked stack.");
			return;
		}
		if (!EconomyHandler.canBuy(new Payment(stockable.getPrice()), npc)) {
			player.sendMessage(ChatColor.RED
					+ "Not enough money available to buy "
					+ StringUtils.yellowify(selling.getAmount() + " "
							+ selling.getType().name(), ChatColor.RED) + "(s).");
			return;
		}
		HashMap<Integer, ItemStack> unsold = npc.getInventory()
				.addItem(selling);
		if (unsold.size() >= 1) {
			npc.getInventory().setContents(previousNPCInv.getContents());
			player.sendMessage(ChatColor.RED
					+ "Not enough room available to add "
					+ StringUtils.yellowify(selling.getAmount() + " "
							+ selling.getType().name(), ChatColor.RED)
					+ "(s) to the current stock.");
			return;
		}
		EconomyHandler.pay(new Payment(stockable.getPrice()), npc);
		player.sendMessage(ChatColor.GREEN + "Transaction successful.");
		// npc.getInventory().setContents(sortInventory(npc.getInventory()));
	}

	public ItemStack[] sortInventory(PlayerInventory inventory) {
		return InventorySorter.sortItemStack(inventory.getContents());
	}

	public void addID(int ID) {
		this.taskID = ID;
	}

	public void kill() {
		stop = true;
		this.npc.getTraderNPC().setFree(true);
		sendLeaveMessage();
		TraderPropertyPool.saveState(npc);
		int index = TraderInterface.tasks.indexOf(taskID);
		if (index != -1)
			TraderInterface.tasks.remove(TraderInterface.tasks.indexOf(taskID));
		plugin.getServer().getScheduler().cancelTask(taskID);
	}

	private void sendJoinMessage() {
		switch (mode) {
		case INFINITE:
		case NORMAL:
			player.sendMessage(ChatColor.GREEN + "Transaction log");
			player.sendMessage(ChatColor.GOLD
					+ "-------------------------------");
			break;
		case STOCK:
			player.sendMessage(ChatColor.GOLD
					+ "Stocking of "
					+ StringUtils.yellowify(npc.getStrippedName(),
							ChatColor.GOLD) + " started.");
			break;
		}
	}

	private void sendLeaveMessage() {
		switch (mode) {
		case INFINITE:
		case NORMAL:
			player.sendMessage(ChatColor.GOLD
					+ "-------------------------------");
			break;
		case STOCK:
			player.sendMessage(ChatColor.GOLD
					+ "Stocking of "
					+ StringUtils.yellowify(npc.getStrippedName(),
							ChatColor.GOLD) + " finished.");
			break;
		}
	}

	private ItemStack cloneItemStack(ItemStack source) {
		if (source == null) // sanity check
			return null;
		ItemStack clone = new ItemStack(source.getType(), source.getAmount(),
				source.getDurability(), (source.getData() != null ? source
						.getData().getData() : null));
		return clone;
	}

	// Clones the first passed PlayerInventory object to the second one.
	private void clonePlayerInventory(PlayerInventory source,
			PlayerInventory target) {
		ItemStack[] contents = new ItemStack[source.getContents().length];
		System.arraycopy(source.getContents(), 0, contents, 0, contents.length);
		target.setContents(contents);

		target.setHelmet(cloneItemStack(source.getHelmet()));
		target.setChestplate(cloneItemStack(source.getChestplate()));
		target.setLeggings(cloneItemStack(source.getLeggings()));
		target.setBoots(cloneItemStack(source.getBoots()));
	}
}
