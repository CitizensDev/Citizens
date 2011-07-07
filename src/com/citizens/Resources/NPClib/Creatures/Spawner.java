package com.citizens.resources.npclib.creatures;

import org.bukkit.Location;

import com.citizens.resources.npclib.HumanNPC;

public interface Spawner {
	public HumanNPC spawn(CreatureNPCType type, Location location);
}
