package com.citizens.Utils;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.Events.NPCInventoryOpenEvent;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;

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
	 * @param material
	 */
	public static void decreaseItemInHand(Player player, Material material) {
		ItemStack item = null;
		if (player.getItemInHand().getAmount() > 1) {
			item = new ItemStack(material,
					player.getItemInHand().getAmount() - 1);
		}
		player.setItemInHand(item);
	}
}