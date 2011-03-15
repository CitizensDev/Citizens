package com.fullwall.Citizens;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;

import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicNPCHandler extends NPCManager {
	public BasicNPCHandler(Citizens plugin) {
		super(plugin);
	}

	public void spawnNPC(String name, Location loc) {
		super.registerBasicNPC(name, loc, NPCType.BASIC);
	}

	public void spawnNPC(String name) {
		super.registerBasicNPC(name, NPCType.BASIC);
	}

	public void setNPCText(String name, ArrayList<String> text) {
		text = StringUtils.colourise(text);
		super.setBasicNPCText(name, text);
		PropertyPool.saveText(name, text);
	}

	public void setName(String name, String changeTo) {
		Location loc = super.getNPC(super.BasicUIDs.get(name).get(0))
				.getBukkitEntity().getLocation();
		PropertyPool.changeName(name, changeTo);
		super.removeNPC(name, super.BasicUIDs.get(name).get(0));
		super.registerBasicNPC(name, loc, NPCType.BASIC);
	}

	public void setColour(String name, String colourChange) {
		Location loc = super.getNPC(super.BasicUIDs.get(name).get(0))
				.getBukkitEntity().getLocation();
		PropertyPool.changeName(name, colourChange.replace("§", "&") + name);
		super.removeNPC(name, super.BasicUIDs.get(name).get(0));
		super.registerBasicNPC(name, loc, NPCType.BASIC);
	}

	public void addNPCText(String name, String text) {
		ArrayList<String> texts = super.getBasicNPCText(name);
		texts.add(text);
		texts = StringUtils.colourise(texts);
		super.setBasicNPCText(name, texts);
		PropertyPool.saveText(name, texts);
	}

	public void resetText(String name) {
		ArrayList<String> a = new ArrayList<String>();
		super.setBasicNPCText(name, a);
	}

	public void setItemInHand(String name, String material) {
		Material mat = StringUtils.parseMaterial(material);
		HumanNPC NPC = super.getNPC(super.BasicUIDs.get(name).get(0));
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(name);
		items.set(0, mat.getId());
		NPCDataManager.addItems(NPC, items);
	}

	public void setItemInSlot(String[] args) {
		Material mat = StringUtils.parseMaterial(args[2]);
		HumanNPC NPC = super.getNPC(super.BasicUIDs.get(args[1]).get(0));
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(args[1]);
		if (args[0].equalsIgnoreCase("helmet")) {
			items.set(1, mat.getId());
		} else if (args[0].equalsIgnoreCase("boots")) {
			items.set(2, mat.getId());
		} else if (args[0].equalsIgnoreCase("legs")) {
			items.set(3, mat.getId());
		} else if (args[0].equalsIgnoreCase("torso")) {
			items.set(4, mat.getId());
		}
		NPCDataManager.addItems(NPC, items);
	}

	public void removeNPC(String name) {
		super.removeNPC(name, super.BasicUIDs.get(name).get(0));
	}

	public void removeAllNPCs() {
		for (Entry<String, ArrayList<String>> i : super.BasicUIDs.entrySet()) {
			for (String s : i.getValue()) {
				super.removeNPC(i.getKey(), s);
			}
		}
	}

	public void despawnAllNPCs() {
		for (Entry<String, ArrayList<String>> i : super.BasicUIDs.entrySet()) {
			for (String s : i.getValue()) {
				super.despawnNPC(i.getKey(), s);
			}
		}
	}

	public void despawnNPC(String name) {
		super.despawnNPC(name, BasicUIDs.get(name).get(0));
	}

	public void moveNPC(String name, Location loc) {
		super.moveNPC(super.BasicUIDs.get(name).get(0), loc);
	}
}
