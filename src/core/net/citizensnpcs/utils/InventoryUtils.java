package net.citizensnpcs.utils;

import net.citizensnpcs.api.event.npc.NPCInventoryOpenEvent;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Material;
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

	@SuppressWarnings("deprecation")
	public static void removeItems(Player player, Material material,
			int amount, int slot) {
		int remaining = amount;
		ItemStack[] contents = player.getInventory().getContents();
		if (slot != -1) {
			ItemStack item = contents[slot];
			if (item != null && item.getType() == material) {
				if (remaining - item.getAmount() < 0) {
					item.setAmount(item.getAmount() - remaining);
					remaining = 0;
				} else {
					remaining -= item.getAmount();
					item = null;
				}
				if (item != null && item.getAmount() == 0)
					item = null;
				contents[slot] = item;
			}
		}
		if (remaining == 0) {
			player.getInventory().setContents(contents);
			player.updateInventory();
			return;
		}
		for (int i = 0; i != contents.length; ++i) {
			ItemStack item = contents[i];
			if (item != null && item.getType() == material) {
				if (remaining - item.getAmount() < 0) {
					item.setAmount(item.getAmount() - remaining);
					remaining = 0;
				} else {
					remaining -= item.getAmount();
					item = null;
				}
				if (item != null && item.getAmount() == 0)
					item = null;
				contents[i] = item;
				if (remaining <= 0)
					break;
			}
		}
		player.getInventory().setContents(contents);
		player.updateInventory();
	}

	public static void removeItems(Player player, Material material, int amount) {
		removeItems(player, material, amount, -1);
	}

	public static int getRemainder(Player player, Material material, int amount) {
		int remaining = amount;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() == material) {
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

	public static boolean has(Player player, Material material, int amount) {
		return getRemainder(player, material, amount) <= 0;
	}

	public static boolean has(Player player, ItemStack stack) {
		return has(player, stack.getType(), stack.getAmount());
	}

	public static void removeItems(Player player, ItemStack buying, int slot) {
		removeItems(player, buying.getType(), buying.getAmount(), slot);
	}

}