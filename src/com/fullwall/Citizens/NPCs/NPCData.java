package com.fullwall.Citizens.NPCs;

import java.util.ArrayDeque;
import java.util.ArrayList;

import org.bukkit.Location;

public class NPCData {

	private String name;
	private int UID;
	private Location location;
	private int colour = 0xf;
	private ArrayList<Integer> items;
	private ArrayDeque<String> texts;
	private boolean lookClose;
	private boolean talkClose;
	private String owner;

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
	public NPCData(String name, int UID, Location loc, int colour,
			ArrayList<Integer> items, ArrayDeque<String> texts,
			boolean lookClose, boolean talkClose, String owner) {
		this.setName(name);
		this.setUID(UID);
		this.setLocation(loc);
		this.setColour(colour);
		this.setItems(items);
		this.setTexts(texts);
		this.setLookClose(lookClose);
		this.setTalkClose(talkClose);
		this.setOwner(owner);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUID(int UID) {
		this.UID = UID;
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

	public void setColour(int code) {
		this.colour = code;
	}

	public int getColour() {
		return colour;
	}

	public void setItems(ArrayList<Integer> items) {
		this.items = items;
	}

	public ArrayList<Integer> getItems() {
		return items;
	}

	public void setTexts(ArrayDeque<String> text) {
		this.texts = text;
	}

	public ArrayDeque<String> getTexts() {
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
}