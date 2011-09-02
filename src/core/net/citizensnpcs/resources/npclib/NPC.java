package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.SettingsManager;

import org.bukkit.ChatColor;

import com.google.common.base.Joiner;

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
		return ChatColor.stripColor(Joiner.on(SettingsManager.getString("SpaceChar"))
				.skipNulls().join(this.name.split(" ")));
	}

	public String getStrippedName() {
		return ChatColor.stripColor(this.name);
	}

	public int getUID() {
		return this.UID;
	}
}