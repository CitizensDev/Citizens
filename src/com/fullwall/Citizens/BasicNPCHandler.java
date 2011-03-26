package com.fullwall.Citizens;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

	public void spawnNPC(String name) {
		//super.registerBasicNPC(name, NPCType.BASIC);
	}

	public void setNPCText(int UID, ArrayList<String> text) {
		text = StringUtils.colourise(text);
		super.setBasicNPCText(UID, text);
		PropertyPool.saveText(UID, text);
	}
	
	public void setOwner(int UID, String name){
		PropertyPool.setNPCOwner(UID,name);
	}
	
	public String getOwner(int UID){
		return PropertyPool.getNPCOwner(UID);
	}

	public void setName(int UID, String changeTo) {
		HumanNPC n = super.getNPC(UID);
		PropertyPool.changeName(UID, n.getName(), changeTo);
		super.removeNPCForRespawn(UID);
		super.registerBasicNPC(changeTo, NPCType.BASIC, UID);
	}

	public void setColour(int UID, String colourChange) {
		HumanNPC n = super.getNPC(UID);
		PropertyPool.saveColour(UID, colourChange.replace("&","§"));
		super.removeNPCForRespawn(UID);
		Citizens.log.info("SetColor Name: " + n.getName());
		super.registerBasicNPC(n.getName(), NPCType.BASIC, UID);
	}

	public void addNPCText(int UID, String text) {
		ArrayList<String> texts = super.getBasicNPCText(UID);
		if(texts == null) texts = new ArrayList<String>();
		texts.add(text);
		texts = StringUtils.colourise(texts);
		super.setBasicNPCText(UID, texts);
		PropertyPool.saveText(UID, texts);
	}

	public void resetText(int UID) {
		ArrayList<String> a = new ArrayList<String>();
		super.setBasicNPCText(UID, a);
	}

	public void setItemInHand(int UID, String material) {
		Material mat = StringUtils.parseMaterial(material);
		HumanNPC NPC = super.getNPC(UID);
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(UID);
		int olditem = items.get(0);
		items.set(0, mat.getId());
		NPCDataManager.addItems(NPC, items);
		if((olditem != 0 && items.get(0) == 0)){
			super.removeNPCForRespawn(NPC.getUID());
			super.registerBasicNPC(NPC.getName(), NPC.getType(), NPC.getUID());
		}
	}

	public void setItemInSlot(Player p, String[] args) {
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		Material mat = StringUtils.parseMaterial(args[1]);
		if(mat == null){
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
		if((oldhelmet != 0 && items.get(1) == 0)){
			super.removeNPCForRespawn(n.getUID());
			super.registerBasicNPC(n.getName(), n.getType(), n.getUID());
		}
	}

	public void removeNPC(int UID) {
		super.removeNPC(UID);
		if(PropertyPool.locations.getString("list").isEmpty()){
			PropertyPool.locations.removeKey("list");
			PropertyPool.locations.setInt("currentID", 0);
		}
	}

	public void removeAllNPCs() {
		for (Entry<Integer, String> i : super.BasicUIDs.entrySet()) {
			super.removeNPC(i.getKey());
		}
	}

	public void despawnAllNPCs() {
		for (Entry<Integer, String> i : super.BasicUIDs.entrySet()) {
			super.despawnNPC(i.getKey());
		}
	}

	public void despawnNPC(int UID) {
		super.despawnNPC(UID);
	}

	public void moveNPC(int UID, Location loc) {
		super.moveNPC(UID, loc);
	}
}
