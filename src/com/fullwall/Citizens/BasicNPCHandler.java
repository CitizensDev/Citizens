package com.fullwall.Citizens;

import java.util.ArrayList;
import java.util.Map.Entry;

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
	
	public void spawnExcistingNPC(String name, int UID) {
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
		HumanNPC n = super.getNPC(""+UID);
		PropertyPool.changeName(UID, n.getName(), changeTo);
		super.removeNPCForRespawn(UID);
		super.registerBasicNPC(changeTo, NPCType.BASIC, UID);
	}

	public void setColour(int UID, String colourChange) {
		HumanNPC n = super.getNPC(""+UID);
		PropertyPool.saveColour(UID, colourChange.replace("&","§"));//changeName(UID, colourChange.replace("§", "&") + n.getName());
		super.removeNPCForRespawn(UID);
		super.registerBasicNPC(n.getName(), NPCType.BASIC, UID);
	}

	public void addNPCText(int UID, String text) {
		ArrayList<String> texts = super.getBasicNPCText(UID);
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
		HumanNPC NPC = super.getNPC(""+UID);
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(UID);
		items.set(0, mat.getId());
		NPCDataManager.addItems(NPC, items);
	}

	public void setItemInSlot(Player p, String[] args) {
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		Material mat = StringUtils.parseMaterial(args[1]);
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(n.getUID());
		Citizens.log.info("Recieved items: " + items);
		if (args[0].equalsIgnoreCase("helmet")) {
			items.set(1, mat.getId());
		} else if (args[0].equalsIgnoreCase("torso")) {
			items.set(2, mat.getId());
		} else if (args[0].equalsIgnoreCase("legs")) {
			items.set(3, mat.getId());
		} else if (args[0].equalsIgnoreCase("boots")) {
			items.set(4, mat.getId());
		}
		Citizens.log.info("Outgoing items: " + items);
		PropertyPool.saveItems(n.getUID(), items);
		NPCDataManager.addItems(n, items);
	}

	public void removeNPC(int UID) {
		super.removeNPC(NPCManager.getNPC(""+UID).getName(), UID);
		if(PropertyPool.locations.getString("list").isEmpty()){
			PropertyPool.locations.removeKey("list");
			PropertyPool.locations.setString("currentID", ""+0);
		}
	}

	public void removeAllNPCs() {
		for (Entry<String, ArrayList<String>> i : super.BasicUIDs.entrySet()) {
			super.removeNPC(i.getKey(), Integer.valueOf(i.getValue().get(0)));
		}
	}

	public void despawnAllNPCs() {
		for (Entry<String, ArrayList<String>> i : super.BasicUIDs.entrySet()) {
			super.despawnNPC(i.getKey(), i.getValue().get(0));
		}
	}

	public void despawnNPC(String name) {
		super.despawnNPC(name, BasicUIDs.get(name).get(0));
	}

	public void moveNPC(int UID, Location loc) {
		super.moveNPC(""+UID, loc);
	}
}
