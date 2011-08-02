package com.citizens.resources.npclib;

import org.bukkit.ChatColor;

import com.citizens.Citizens;
import com.citizens.properties.SettingsManager;

public class NPC {

	private String name;
	private final int UID;

	public NPC(int UID, String name) {
		this.name = name;
		this.UID = UID;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public String getName() {
		if (SettingsManager.getBoolean("ConvertSlashes")) {
			String returnName = "";
			String[] brokenName = this.name.split(" ");
			for (int i = 0; i < brokenName.length; i++) {
				if (i == 0) {
					returnName = brokenName[i];
				} else {
					returnName += Citizens.separatorChar + brokenName[i];
				}
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