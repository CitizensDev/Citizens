package net.citizensnpcs.resources.npclib;

import org.bukkit.ChatColor;

import com.google.common.base.Objects;

public class NPC {
	private final String name;
	private final int UID;

	public NPC(int UID, String name) {
		this.name = ChatColor.stripColor(name);
		this.UID = UID;
	}

	public String getName() {
		return this.name;
	}

	public int getUID() {
		return this.UID;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(UID);
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