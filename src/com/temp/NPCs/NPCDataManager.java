package com.temp.NPCs;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.temp.NPCs.NPCDataManager;
import com.temp.NPCs.NPCManager;
import com.temp.Utils.StringUtils;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

public class NPCDataManager {
	/**
	 * Adds items to an npc so that they are visible.
	 * 
	 * @param npc
	 * @param items
	 */
	public static void addItems(HumanNPC npc, ArrayList<Integer> items) {
		if (items != null) {
			Material matHelm = Material.getMaterial(items.get(1));
			Material matTorso = Material.getMaterial(items.get(2));
			Material matLegs = Material.getMaterial(items.get(3));
			Material matBoots = Material.getMaterial(items.get(4));

			// TODO: reduce the long if-tree.
			if (matHelm != null && matHelm != Material.AIR) {
				npc.getInventory().setHelmet(new ItemStack(matHelm, 1));
			} else {
				npc.getInventory().setHelmet(null);
			}
			if (matBoots != null && matBoots != Material.AIR) {
				npc.getInventory().setBoots(new ItemStack(matBoots, 1));
			} else {
				npc.getInventory().setBoots(null);
			}
			if (matLegs != null && matLegs != Material.AIR) {
				npc.getInventory().setLeggings(new ItemStack(matLegs, 1));
			} else {
				npc.getInventory().setLeggings(null);
			}
			if (matTorso != null && matTorso != Material.AIR) {
				npc.getInventory().setChestplate(new ItemStack(matTorso, 1));
			} else {
				npc.getInventory().setChestplate(null);
			}
			npc.getNPCData().setItems(items);
		}
	}

	/**
	 * Sets the in-hand item of an npc.
	 * 
	 * @param p
	 * @param npc
	 * @param material
	 */
	public static void setItemInHand(Player p, HumanNPC npc, String material) {
		Material mat = StringUtils.parseMaterial(material);
		if (mat == null) {
			p.sendMessage(ChatColor.RED + "Incorrect item name.");
			return;
		}
		if (!p.getInventory().contains(mat)) {
			p.sendMessage(ChatColor.RED
					+ "You need to have at least 1 of the item in your inventory to add it to the NPC.");
			return;
		}
		if (npc.isType("trader")) {
			p.sendMessage(ChatColor.GRAY
					+ "That NPC is a trader. Please put the item manually in the first slot of the trader's inventory instead.");
			return;
		}
		int slot = p.getInventory().first(mat);
		ItemStack item = decreaseItemStack(p.getInventory().getItem(slot));
		p.getInventory().setItem(slot, item);

		ArrayList<Integer> items = npc.getNPCData().getItems();

		int olditem = items.get(0);
		items.set(0, mat.getId());

		npc.getNPCData().setItems(items);

		if (mat != null && mat != Material.AIR) {
			npc.getInventory().setItem(0, new ItemStack(mat, 1));
		} else {
			npc.getInventory().setItem(0, null);
		}

		NPCDataManager.addItems(npc, items);

		if ((olditem != 0 && items.get(0) == 0)) {
			NPCManager.removeForRespawn(npc.getUID());
			NPCManager.register(npc.getUID(), npc.getOwner());
		}
		p.sendMessage(StringUtils.wrap(npc.getName())
				+ "'s in-hand item was set to " + StringUtils.wrap(mat.name())
				+ ".");
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