package net.citizensnpcs.resources.npclib.creatures;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Location;

public interface Spawner {
	public HumanNPC spawn(CreatureNPCType type, Location location);
}