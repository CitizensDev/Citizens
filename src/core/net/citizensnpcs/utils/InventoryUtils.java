package net.citizensnpcs.utils;

import net.citizensnpcs.api.events.NPCInventoryOpenEvent;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

	/**
	 * Uses craftbukkit methods to show a player an npc's inventory screen.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void showInventory(HumanNPC npc, Player player) {
		NPCInventoryOpenEvent inventoryOpenEvent = new NPCInventoryOpenEvent(
				npc, player);
		if (inventoryOpenEvent.isCancelled()) {
			return;
		}
		((CraftPlayer) player).getHandle().a(npc.getHandle().inventory);
	}

	/**
	 * Remove items from a player's current held slot
	 * 
	 * @param player
	 */
	public static void decreaseItemInHand(Player player) {
		player.setItemInHand(decreaseItemStack(player.getItemInHand()));
	}

	public static ItemStack decreaseItemStack(ItemStack stack) {
		int amount = stack.getAmount() - 1;
		if (amount == 0) {
			stack = null;
		} else {
			stack.setAmount(amount);
		}
		return stack;
	}

	/**
	 * Validate that an item has a durability and can be repaired
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isTool(ItemStack item) {
		int id = item.getTypeId();
		return (id >= 256 && id <= 259) || (id >= 267 && id <= 279)
				|| (id >= 283 && id <= 286) || (id >= 290 && id <= 294)
				|| id == 346 || id == 359;
	}

	/**
	 * Validate that the item to repair is armor
	 * 
	 * @param armor
	 * @return
	 */
	public static boolean isArmor(ItemStack armor) {
		int id = armor.getTypeId();
		return id >= 298 && id <= 317;
	}

	public static void removeItems(Player player, ItemStack stack) {
		int remaining = stack.getAmount();
		ItemStack[] contents = player.getInventory().getContents();
		for (int i = 0; i != contents.length; ++i) {
			ItemStack item = contents[i];
			if (item.getType() == stack.getType()) {
				if (remaining - item.getAmount() < 0) {
					item.setAmount(item.getAmount() - remaining);
					remaining = 0;
				} else {
					remaining -= item.getAmount();
					item = null;
				}
				if (item.getAmount() == 0)
					item = null;
				contents[i] = item;
				if (remaining <= 0)
					break;
			}
		}
		player.getInventory().setContents(contents);
	}

	public static void removeItems(Player player, ItemStack stack, int slot) {
		int remaining = stack.getAmount();
		ItemStack[] contents = player.getInventory().getContents();
		ItemStack item = contents[slot];
		if (item.getType() == stack.getType()) {
			if (remaining - item.getAmount() < 0) {
				item.setAmount(item.getAmount() - remaining);
				remaining = 0;
			} else {
				remaining -= item.getAmount();
				item = null;
			}
			if (item.getAmount() == 0)
				item = null;
			contents[slot] = item;
		}
		if (remaining > 0) {
			for (int i = 0; i != contents.length; ++i) {
				item = contents[i];
				if (item.getType() == stack.getType()) {
					if (remaining - item.getAmount() < 0) {
						item.setAmount(item.getAmount() - remaining);
						remaining = 0;
					} else {
						remaining -= item.getAmount();
						item = null;
					}
					if (item.getAmount() == 0)
						item = null;
					contents[i] = item;
					if (remaining <= 0)
						break;
				}
			}
		}
		player.getInventory().setContents(contents);
	}

	public static int getRemainder(Player player, ItemStack stack) {
		int remaining = stack.getAmount();
		for (ItemStack item : player.getInventory().getContents()) {
			if (item.getType() == stack.getType()) {
				if (remaining - item.getAmount() < 0) {
					item.setAmount(item.getAmount() - remaining);
					remaining = 0;
				} else {
					remaining -= item.getAmount();
				}
				if (remaining <= 0)
					break;
			}
		}
		return remaining;
	}

	public static boolean has(Player player, ItemStack stack) {
		return getRemainder(player, stack) <= 0;
	}
}