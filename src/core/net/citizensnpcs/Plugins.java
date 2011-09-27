package net.citizensnpcs;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Plugins {
	public static WorldGuardPlugin worldGuard;

	public static boolean worldGuardEnabled() {
		return worldGuard != null;
	}
	// TODO: move all external plugin loading and storage to here.
}
