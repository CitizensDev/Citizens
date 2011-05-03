package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Utils.PropertyPool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class NPCDataManager extends NPCManager {

	public NPCDataManager(Citizens plugin) {
		super(plugin);
	}

	public static void addItems(HumanNPC npc, ArrayList<Integer> items) {
		if (items != null) {
			Material matHand = Material.getMaterial(items.get(0));
			Material matHelm = Material.getMaterial(items.get(1));
			Material matTorso = Material.getMaterial(items.get(2));
			Material matLegs = Material.getMaterial(items.get(3));
			Material matBoots = Material.getMaterial(items.get(4));

			// TODO: reduce the long if-tree.
			if (!matHelm.equals(Material.AIR))
				npc.getInventory().setHelmet(new ItemStack(matHelm, 1));
			else
				npc.getInventory().setHelmet(null);

			if (!matBoots.equals(Material.AIR))
				npc.getInventory().setBoots(new ItemStack(matBoots, 1));
			else
				npc.getInventory().setBoots(null);

			if (!matLegs.equals(Material.AIR))
				npc.getInventory().setLeggings(new ItemStack(matLegs, 1));
			else
				npc.getInventory().setLeggings(null);

			if (!matTorso.equals(Material.AIR))
				npc.getInventory().setChestplate(new ItemStack(matTorso, 1));
			else
				npc.getInventory().setChestplate(null);

			if (!matHand.equals(Material.AIR))
				npc.getInventory().setItem(0, new ItemStack(matHand, 1));
			else
				npc.getInventory().setItem(0, null);

			PropertyPool.items.setString(npc.getUID(), "" + items.get(0) + ","
					+ items.get(1) + "," + items.get(2) + "," + items.get(3)
					+ "," + items.get(4) + ",");
		}
	}

	public ArrayList<Object> getAllBasicData(String name) {
		return new ArrayList<Object>();
	}

}
