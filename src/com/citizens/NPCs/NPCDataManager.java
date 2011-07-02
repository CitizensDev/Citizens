package com.citizens.NPCs;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.NPCs.NPCDataManager;
import com.citizens.NPCs.NPCManager;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Utils.MessageUtils;
import com.citizens.Utils.StringUtils;

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
	 * @param player
	 * @param npc
	 * @param material
	 */
	public static void setItemInHand(Player player, HumanNPC npc,
			String material) {
		Material mat = StringUtils.parseMaterial(material);
		if (mat == null) {
			player.sendMessage(ChatColor.RED + "Incorrect item name.");
			return;
		}
		if (!player.getInventory().contains(mat)) {
			player.sendMessage(ChatColor.RED
					+ "You need to have at least 1 of the item in your inventory to add it to the NPC.");
			return;
		}
		if (npc.isType("trader")) {
			player.sendMessage(ChatColor.GRAY
					+ "That NPC is a trader. Please put the item manually in the first slot of the trader's inventory instead.");
			return;
		}
		int slot = player.getInventory().first(mat);
		ItemStack item = decreaseItemStack(player.getInventory().getItem(slot));
		player.getInventory().setItem(slot, item);

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
		player.sendMessage(StringUtils.wrap(npc.getName())
				+ "'s in-hand item was set to "
				+ StringUtils.wrap(MessageUtils.getMaterialName(mat.getId()))
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