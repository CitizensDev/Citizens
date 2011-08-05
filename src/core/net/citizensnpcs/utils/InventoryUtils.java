package net.citizensnpcs.utils;

import net.citizensnpcs.events.NPCInventoryOpenEvent;
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
}