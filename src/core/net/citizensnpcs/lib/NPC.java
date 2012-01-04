package net.citizensnpcs.lib;

import net.citizensnpcs.Settings;

import org.bukkit.ChatColor;

import com.google.common.base.Objects;

public abstract class NPC<T> {
	private String name;
	private final int UID;

	public NPC(int UID, String name) {
		if (!Settings.getString("SpaceChar").isEmpty())
			name = name.replace(Settings.getString("SpaceChar"), " ");
		this.name = ChatColor.stripColor(name);
		this.UID = UID;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		NPC<T> other = (NPC<T>) obj;
		return UID == other.UID;
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

	public void setName(String name) {
		this.name = ChatColor.stripColor(name);
	}
}