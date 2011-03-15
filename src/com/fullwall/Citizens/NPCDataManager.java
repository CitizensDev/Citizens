package com.fullwall.Citizens;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class NPCDataManager extends NPCManager {

	public NPCDataManager(Citizens plugin) {
		super(plugin);
	}

	public static void addItems(HumanNPC npc, ArrayList<Integer> items) {
		if (items != null) {
			PlayerInventory inv = npc.getBukkitEntity().getInventory();

			Material inHand = Material.getMaterial(items.get(0));
			Material helmet = Material.getMaterial(items.get(1));
			Material chestplate = Material.getMaterial(items.get(2));
			Material leggings = Material.getMaterial(items.get(3));
			Material boots = Material.getMaterial(items.get(4));

			inv.setItemInHand(new ItemStack(inHand));
			inv.setHelmet(new ItemStack(helmet));
			inv.setChestplate(new ItemStack(chestplate));
			inv.setLeggings(new ItemStack(leggings));
			inv.setBoots(new ItemStack(boots));
		}
	}

	public ArrayList<Object> getAllBasicData(String name) {
		return new ArrayList<Object>();
	}
	
}
