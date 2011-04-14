package com.fullwall.resources.redecouverte.NPClib;

import org.bukkit.ChatColor;

import com.fullwall.Citizens.Citizens;

public class NPC {

	private String name;
	private int UID;

	public NPC(int UID, String name) {
		this.name = name;
		this.UID = UID;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public String getName() {
		if (Citizens.convertSlashes == true) {
			String returnName = "";
			String[] brokenName = this.name.split(" ");
			for (int i = 0; i < brokenName.length; i++) {
				if (i == 0)
					returnName = brokenName[i];
				else
					returnName += Citizens.convertToSpaceChar + brokenName[i];
			}
			return ChatColor.stripColor(returnName);
		}
		return ChatColor.stripColor(this.name);
	}

	public String getStrippedName() {
		return ChatColor.stripColor(this.name);
	}

	public int getUID() {
		return this.UID;
	}

}