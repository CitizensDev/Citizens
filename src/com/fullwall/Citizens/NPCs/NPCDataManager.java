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

			HumanNPC NPC = npc;
			// TODO: reduce the long if-tree.
			if (!matHelm.equals(Material.AIR))
				NPC.getBukkitEntity().getInventory()
						.setHelmet(new ItemStack(matHelm, 1));
			else
				NPC.getBukkitEntity().getInventory()
						.setHelmet(new ItemStack(Material.AIR, 1));

			if (!matBoots.equals(Material.AIR))
				NPC.getBukkitEntity().getInventory()
						.setBoots(new ItemStack(matBoots, 1));
			else
				NPC.getBukkitEntity().getInventory()
						.setBoots(new ItemStack(Material.AIR, 1));

			if (!matLegs.equals(Material.AIR))
				NPC.getBukkitEntity().getInventory()
						.setLeggings(new ItemStack(matLegs, 1));
			else
				NPC.getBukkitEntity().getInventory()
						.setLeggings(new ItemStack(Material.AIR, 1));

			if (!matTorso.equals(Material.AIR))
				NPC.getBukkitEntity().getInventory()
						.setChestplate(new ItemStack(matTorso, 1));
			else
				NPC.getBukkitEntity().getInventory()
						.setChestplate(new ItemStack(Material.AIR, 1));

			if (!matHand.equals(Material.AIR))
				NPC.getBukkitEntity().getInventory()
						.setItem(0, new ItemStack(matHand, 1));
			else
				NPC.getBukkitEntity().getInventory()
						.setItem(0, new ItemStack(Material.AIR, 1));

			PropertyPool.items.setString(NPC.getUID(), "" + items.get(0) + ","
					+ items.get(1) + "," + items.get(2) + "," + items.get(3)
					+ "," + items.get(4) + ",");
		}
	}

	public ArrayList<Object> getAllBasicData(String name) {
		return new ArrayList<Object>();
	}

}
