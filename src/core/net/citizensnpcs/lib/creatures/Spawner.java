package net.citizensnpcs.lib.creatures;

import net.citizensnpcs.lib.CraftNPC;

import org.bukkit.Location;

public interface Spawner {
	public CraftNPC spawn(CreatureNPCType type, Location location);
}