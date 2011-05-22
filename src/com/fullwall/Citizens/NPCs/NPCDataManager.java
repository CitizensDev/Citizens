package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;

import com.fullwall.Citizens.Citizens;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class NPCDataManager extends NPCManager {

	public NPCDataManager(Citizens plugin) {
		super(plugin);
	}

	/**
	 * Adds items to an npc so that they are visible.
	 * 
	 * @param npc
	 * @param items
	 */
	public static void addItems(HumanNPC npc, ArrayList<Integer> items) {
		if (items != null) {
			Material matHand = Material.getMaterial(items.get(0));
			Material matHelm = Material.getMaterial(items.get(1));
			Material matTorso = Material.getMaterial(items.get(2));
			Material matLegs = Material.getMaterial(items.get(3));
			Material matBoots = Material.getMaterial(items.get(4));

			// TODO: reduce the long if-tree.
			if (matHelm != null && matHelm != Material.AIR)
				npc.getInventory().setHelmet(new ItemStack(matHelm, 1));
			else
				npc.getInventory().setHelmet(null);

			if (matBoots != null && matBoots != Material.AIR)
				npc.getInventory().setBoots(new ItemStack(matBoots, 1));
			else
				npc.getInventory().setBoots(null);

			if (matLegs != null && matLegs != Material.AIR)
				npc.getInventory().setLeggings(new ItemStack(matLegs, 1));
			else
				npc.getInventory().setLeggings(null);

			if (matTorso != null && matTorso != Material.AIR)
				npc.getInventory().setChestplate(new ItemStack(matTorso, 1));
			else
				npc.getInventory().setChestplate(null);

			if (matHand != null && matHand != Material.AIR)
				npc.getInventory().setItem(0, new ItemStack(matHand, 1));
			else
				npc.getInventory().setItem(0, null);
			npc.getNPCData().setItems(items);
		}
	}
}
