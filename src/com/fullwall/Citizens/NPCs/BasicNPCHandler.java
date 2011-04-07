package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicNPCHandler extends NPCManager {

	public BasicNPCHandler(Citizens plugin) {
		super(plugin);
	}

	public int spawnNPC(String name, Location loc) {
		return super.registerBasicNPC(name, loc, NPCType.BASIC);
	}

	public void spawnExistingNPC(String name, int UID) {
		super.registerBasicNPC(name, NPCType.BASIC, UID);
	}

	public void moveNPC(int UID, Location loc) {
		super.moveNPC(UID, loc);
	}

	public void despawnNPC(int UID) {
		super.despawnNPC(UID);
	}

	public void despawnAllNPCs() {
		for (Entry<Integer, String> i : GlobalUIDs.entrySet()) {
			super.despawnNPC(i.getKey());
		}
	}

	public void removeNPC(int UID) {
		super.removeNPC(UID);
		if (PropertyPool.locations.getString("list").isEmpty()) {
			PropertyPool.locations.removeKey("list");
			PropertyPool.locations.setInt("currentID", 0);
		}
	}

	public void removeAllNPCs() {
		for (Entry<Integer, String> i : GlobalUIDs.entrySet()) {
			super.removeNPC(i.getKey());
		}
	}

	public String getOwner(int UID) {
		return PropertyPool.getNPCOwner(UID);
	}

	public void setOwner(int UID, String name) {
		PropertyPool.setNPCOwner(UID, name);
	}

	public void setName(int UID, String changeTo) {
		HumanNPC n = super.getNPC(UID);
		PropertyPool.changeName(UID, n.getName(), changeTo);
		super.removeNPCForRespawn(UID);
		super.registerBasicNPC(changeTo, NPCType.BASIC, UID);
	}

	// TODO: maybe remove this, since it changes the skin URL.
	public void setColour(int UID, String colourChange) {
		HumanNPC n = super.getNPC(UID);
		PropertyPool.saveColour(UID, colourChange.replace("&", "§"));
		super.removeNPCForRespawn(UID);
		super.registerBasicNPC(n.getName(), NPCType.BASIC, UID);
	}

	public void addNPCText(int UID, String text) {
		ArrayList<String> texts = super.getBasicNPCText(UID);
		if (texts == null)
			texts = new ArrayList<String>();
		texts.add(text);
		texts = StringUtils.colourise(texts);
		super.setBasicNPCText(UID, texts);
		PropertyPool.saveText(UID, texts);
	}

	public void setNPCText(int UID, ArrayList<String> text) {
		text = StringUtils.colourise(text);
		super.setBasicNPCText(UID, text);
		PropertyPool.saveText(UID, text);
	}

	public void resetText(int UID) {
		ArrayList<String> a = new ArrayList<String>();
		super.setBasicNPCText(UID, a);
	}

	// Perhaps merge this with setItemInSlot.
	public void setItemInHand(Player p, int UID, String material) {
		Material mat = StringUtils.parseMaterial(material);
		if (mat == null
				&& !PropertyPool.itemlookups.getString(material).isEmpty())
			mat = StringUtils.parseMaterial(PropertyPool.itemlookups
					.getString(material));
		if (mat == null) {
			p.sendMessage(ChatColor.RED + "Incorrect Item Name.");
			return;
		}
		HumanNPC NPC = super.getNPC(UID);
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(UID);
		int olditem = items.get(0);
		items.set(0, mat.getId());
		NPCDataManager.addItems(NPC, items);
		if ((olditem != 0 && items.get(0) == 0)) {
			super.removeNPCForRespawn(NPC.getUID());
			super.registerBasicNPC(NPC.getName(), NPC.getType(), NPC.getUID());
		}
	}

	public void setItemInSlot(Player p, String[] args) {
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		Material mat = StringUtils.parseMaterial(args[1]);
		if (mat == null
				&& !PropertyPool.itemlookups.getString(args[1]).isEmpty())
			mat = StringUtils.parseMaterial(PropertyPool.itemlookups
					.getString(args[1]));
		if (mat == null) {
			p.sendMessage(ChatColor.RED + "Incorrect Item Name.");
			return;
		}
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(n.getUID());
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
		PropertyPool.saveItems(n.getUID(), items);
		NPCDataManager.addItems(n, items);
		if ((oldhelmet != 0 && items.get(1) == 0)) {
			// Despawn the old NPC, register our new one.
			super.removeNPCForRespawn(n.getUID());
			super.registerBasicNPC(n.getName(), n.getType(), n.getUID());
		}
	}
}
