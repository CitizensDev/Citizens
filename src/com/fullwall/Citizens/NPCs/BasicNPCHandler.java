package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;
import java.util.LinkedList;

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
	public void rename(int UID, String changeTo, String owner) {
		HumanNPC n = super.get(UID);
		PropertyManager.getBasic().changeName(UID, n.getName(), changeTo);
		n.getNPCData().setName(changeTo);
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
	public void setColour(int UID, String owner) {
		HumanNPC n = super.get(UID);
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
		LinkedList<String> texts = super.getText(UID);
		if (texts == null) {
			texts = new LinkedList<String>();
		}
		texts.add(text);
		super.setText(UID, texts);
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
			p.sendMessage(ChatColor.RED + "Incorrect item name.");
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
			super.removeForRespawn(npc.getUID());
			super.register(npc.getName(), npc.getUID(), npc.getOwner());
		}
		p.sendMessage(StringUtils.wrap(npc.getName())
				+ "'s in-hand item was set to " + StringUtils.wrap(mat.name())
				+ ".");
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
			p.sendMessage(ChatColor.RED + "Incorrect item name.");
			return;
		}
		if (!p.getInventory().contains(mat)) {
			p.sendMessage(ChatColor.RED
					+ "You need to have at least 1 of the item in your inventory to add it to the NPC.");
			return;
		}
		if (mat.getId() < 298 || mat.getId() > 317) {
			p.sendMessage(ChatColor.GRAY
					+ "That can't be used as an armour material.");
			return;
		}
		int slot = p.getInventory().first(mat);
		ItemStack item = decreaseItemStack(p.getInventory().getItem(slot));
		p.getInventory().setItem(slot, item);
		ArrayList<Integer> items = npc.getNPCData().getItems();
		int oldhelmet = items.get(1);

		if (args[0].contains("helm")) {
			items.set(1, mat.getId());
		} else if (args[0].equalsIgnoreCase("torso")) {
			items.set(2, mat.getId());
		} else if (args[0].contains("leg")) {
			items.set(3, mat.getId());
		} else if (args[0].contains("boot")) {
			items.set(4, mat.getId());
		}
		npc.getNPCData().setItems(items);
		NPCDataManager.addItems(npc, items);

		if ((oldhelmet != 0 && items.get(1) == 0)) {
			// Despawn the old NPC, register our new one.
			super.removeForRespawn(npc.getUID());
			super.register(npc.getName(), npc.getUID(), npc.getOwner());
		}
		p.sendMessage(StringUtils.wrap(npc.getName()) + "'s " + args[0]
				+ " was set to " + StringUtils.wrap(mat.name()) + ".");
	}

	public ItemStack decreaseItemStack(ItemStack stack) {
		int amount = stack.getAmount() - 1;
		if (amount == 0) {
			stack = null;
		} else {
			stack.setAmount(amount);
		}
		return stack;
	}
}