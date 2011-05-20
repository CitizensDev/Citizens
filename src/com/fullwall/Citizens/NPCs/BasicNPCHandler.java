package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicNPCHandler extends NPCManager {

	public BasicNPCHandler(Citizens plugin) {
		super(plugin);
	}

	/**
	 * Despawns all npcs.
	 */
	public void despawnAll() {
		for (Integer i : GlobalUIDs.keySet()) {
			super.despawn(i);
		}
	}

	/**
	 * Removes all npcs.
	 */
	public void removeAll() {
		for (Integer i : GlobalUIDs.keySet()) {
			super.remove(i);
		}
	}

	/**
	 * Renames an npc.
	 * 
	 * @param UID
	 * @param changeTo
	 * @param owner
	 */
	public void setName(int UID, String changeTo, String owner) {
		HumanNPC n = super.get(UID);
		PropertyManager.getBasicProperties().changeName(UID, n.getName(),
				changeTo);
		super.removeForRespawn(UID);
		super.register(changeTo, UID, owner);
	}

	/**
	 * Sets the colour of an npc's name.
	 * 
	 * @param UID
	 * @param colourChange
	 * @param owner
	 */
	// TODO: maybe remove this, since it changes the skin URL.
	public void setColour(int UID, String colourChange, String owner) {
		HumanNPC n = super.get(UID);
		n.getNPCData().setColour(colourChange.replace("&", "§"));
		super.removeForRespawn(UID);
		super.register(n.getName(), UID, owner);
	}

	/**
	 * Adds to an npc's text.
	 * 
	 * @param UID
	 * @param text
	 */
	public void addText(int UID, String text) {
		ArrayList<String> texts = super.getText(UID);
		if (texts == null)
			texts = new ArrayList<String>();
		texts.add(text);
		texts = StringUtils.colourise(texts);
		super.setText(UID, texts);
		super.get(UID).getNPCData().setTexts(texts);
	}

	/**
	 * Resets an npc's text to blank.
	 * 
	 * @param UID
	 */
	public void resetText(int UID) {
		ArrayList<String> a = new ArrayList<String>();
		super.setText(UID, a);
	}

	/**
	 * Sets the in-hand item of an npc.
	 * 
	 * @param p
	 * @param npc
	 * @param material
	 */
	// Perhaps merge this with setItemInSlot.
	public void setItemInHand(Player p, HumanNPC npc, String material) {
		Material mat = StringUtils.parseMaterial(material);
		if (mat == null) {
			p.sendMessage(ChatColor.RED + "Incorrect Item Name.");
			return;
		}
		if (!p.getInventory().contains(mat)) {
			p.sendMessage(ChatColor.RED
					+ "You need to have at least 1 of the item in your inventory to add it to the NPC.");
			return;
		}
		if (npc.isTrader()) {
			p.sendMessage(ChatColor.GRAY
					+ "That NPC is a trader. Please put the item manually in the first slot of the trader's inventory instead.");
			return;
		}
		int slot = p.getInventory().first(mat);
		ItemStack item = p.getInventory().getItem(slot);
		int amount = item.getAmount() - 1;
		if (amount == 0)
			item = null;
		else
			item.setAmount(amount);
		p.getInventory().setItem(slot, item);
		ArrayList<Integer> items = PropertyManager.getBasicProperties()
				.getItems(npc.getUID());
		int olditem = items.get(0);
		items.set(0, mat.getId());
		NPCDataManager.addItems(npc, items);
		if ((olditem != 0 && items.get(0) == 0)) {
			super.removeForRespawn(npc.getUID());
			super.register(npc.getName(), npc.getUID(), npc.getOwner());
		}
	}

	/**
	 * Sets a given armour type to a material.
	 * 
	 * @param args
	 * @param p
	 * @param npc
	 */
	public void setItemInSlot(String[] args, Player p, HumanNPC npc) {
		Material mat = StringUtils.parseMaterial(args[1]);
		if (mat == null) {
			p.sendMessage(ChatColor.RED + "Incorrect Item Name.");
			return;
		}
		if (!p.getInventory().contains(mat)) {
			p.sendMessage(ChatColor.RED
					+ "You need to have at least 1 of the item in your inventory to add it to the NPC.");
			return;
		}
		p.getInventory().remove(mat);
		ArrayList<Integer> items = PropertyManager.getBasicProperties()
				.getItems(npc.getUID());
		int oldhelmet = items.get(1);
		if (args[0].equalsIgnoreCase("helmet")) {
			items.set(1, mat.getId());
		} else if (args[0].equalsIgnoreCase("torso")) {
			items.set(2, mat.getId());
		} else if (args[0].equalsIgnoreCase("legs")) {
			items.set(3, mat.getId());
		} else if (args[0].equalsIgnoreCase("boots")) {
			items.set(4, mat.getId());
		}
		NPCDataManager.addItems(npc, items);
		if ((oldhelmet != 0 && items.get(1) == 0)) {
			// Despawn the old NPC, register our new one.
			super.removeForRespawn(npc.getUID());
			super.register(npc.getName(), npc.getUID(), npc.getOwner());
		}
	}
}
