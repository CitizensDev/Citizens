package net.citizensnpcs.resources.npclib;

import org.bukkit.ChatColor;

public class NPC {
	private final String name;
	private final int UID;

	public NPC(int UID, String name) {
		this.name = name;
		this.UID = UID;
	}

	public String getName() {
		return ChatColor.stripColor(this.name);
	}

	public int getUID() {
		return this.UID;
	}

	@Override
	public int hashCode() {
		return 31 + UID;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		NPC other = (NPC) obj;
		return UID == other.UID;
	}
}