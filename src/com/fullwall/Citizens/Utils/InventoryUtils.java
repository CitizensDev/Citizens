package com.fullwall.Citizens.Utils;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class InventoryUtils {

	/**
	 * Uses craftbukkit methods to show a player an npc's inventory screen.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void showInventory(HumanNPC npc, Player player) {
		((CraftPlayer) player).getHandle().a(npc.getHandle().inventory);
	}

	public static void decreaseItemInHand(Player player, Material material,
			int amount) {
		if (amount == 1) {
			ItemStack emptyStack = null;
			player.setItemInHand(emptyStack);
		} else {
			player.setItemInHand(new ItemStack(material, amount - 1));
		}
	}
}