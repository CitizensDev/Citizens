package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;

import org.bukkit.Location;

public class NPCData {

	private String name;
	private int UID;
	private Location location;
	private String colour;
	private ArrayList<Integer> items;
	private ArrayList<String> texts;
	private boolean lookClose;
	private boolean talkClose;
	private String owner;
	private int balance;

	/**
	 * Acts as a container for various npc data.
	 * 
	 * @param name
	 * @param UID
	 * @param loc
	 * @param colour
	 * @param items
	 * @param texts
	 * @param lookClose
	 * @param talkClose
	 * @param owner
	 */
	public NPCData(String name, int UID, Location loc, String colour,
			ArrayList<Integer> items, ArrayList<String> texts,
			boolean lookClose, boolean talkClose, String owner, int balance) {
		this.setName(name);
		this.setUID(UID);
		this.setLocation(loc);
		this.setColour(colour);
		this.setItems(items);
		this.setTexts(texts);
		this.setLookClose(lookClose);
		this.setTalkClose(talkClose);
		this.setOwner(owner);
		this.setBalance(balance);
	}

	private void setBalance(int balance) {
		this.balance = balance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public int getUID() {
		return UID;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getColour() {
		return colour;
	}

	public void setItems(ArrayList<Integer> items) {
		this.items = items;
	}

	public ArrayList<Integer> getItems() {
		return items;
	}

	public void setTexts(ArrayList<String> texts) {
		this.texts = texts;
	}

	public ArrayList<String> getTexts() {
		return texts;
	}

	public void setLookClose(boolean lookClose) {
		this.lookClose = lookClose;
	}

	public boolean isLookClose() {
		return lookClose;
	}

	public void setTalkClose(boolean talkClose) {
		this.talkClose = talkClose;
	}

	public boolean isTalkClose() {
		return talkClose;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

	public int getBalance() {
		return balance;
	}
}
