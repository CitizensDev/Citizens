package net.citizensnpcs.npcs;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class NPCData {
	private String name;
	private int UID;
	private Location location;
	private ChatColor colour = ChatColor.WHITE;
	private List<HashMap<Integer, Short>> items;
	private Deque<String> texts;
	private boolean lookClose;
	private boolean talkClose;
	private String owner;

	// Acts as a container for various npc data.
	public NPCData(String name, int UID, Location loc, ChatColor colour,
			List<HashMap<Integer, Short>> items, Deque<String> texts,
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

	public void setColour(ChatColor code) {
		this.colour = code;
	}

	public ChatColor getColour() {
		return colour;
	}

	public void setItems(List<HashMap<Integer, Short>> items) {
		this.items = items;
	}

	public List<HashMap<Integer, Short>> getItems() {
		return items;
	}

	public void setTexts(Deque<String> text) {
		this.texts = text;
	}

	public Deque<String> getTexts() {
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