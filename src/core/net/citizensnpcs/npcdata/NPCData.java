package net.citizensnpcs.npcdata;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class NPCData {
	private ChatColor colour = ChatColor.WHITE;
	private List<ItemData> items = new ArrayList<ItemData>();
	private Location location;
	private boolean lookClose;
	private String name;
	private String owner;
	private boolean talk;
	private boolean talkClose;
	private Deque<String> texts = new ArrayDeque<String>();
	private int UID = -1;

	public NPCData(){}
    
    // Acts as a container for various npc data.
	public NPCData(String name, int UID, Location loc, ChatColor colour,
			List<ItemData> items, Deque<String> texts, boolean talk,
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
		this.setTalk(talk);
	}

	public ChatColor getColour() {
		return colour;
	}

	public List<ItemData> getItems() {
		return items;
	}

	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	public Deque<String> getTexts() {
		return texts;
	}

	public int getUID() {
		return UID;
	}

	public boolean isLookClose() {
		return lookClose;
	}

	public boolean isTalk() {
		return this.talk;
	}

	public boolean isTalkClose() {
		return talkClose;
	}

	public void setColour(ChatColor code) {
		this.colour = code;
	}

	public void setItems(List<ItemData> items) {
		this.items = items;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setLookClose(boolean lookClose) {
		this.lookClose = lookClose;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setTalk(boolean talk) {
		this.talk = talk;
	}

	public void setTalkClose(boolean talkClose) {
		this.talkClose = talkClose;
	}

	public void setTexts(Deque<String> text) {
		this.texts = text;
	}

	public void setUID(int UID) {
		this.UID = UID;
	}
}