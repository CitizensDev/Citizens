package com.fullwall.resources.redecouverte.NPClib.Creatures;

import org.bukkit.Material;

import com.fullwall.Citizens.Constants;

public enum CreatureNPCType {
	EVIL(EvilCreatureNPC.class, Constants.maxEvilNPCs,
			Constants.defaultEvilNames, Material.AIR);
	private int max;
	private String possible;
	private Class<? extends CreatureNPC> instance;
	private final Material spawnIn;

	CreatureNPCType(Class<? extends CreatureNPC> instance, int max,
			String possibleNames, Material spawnIn) {
		this.instance = instance;
		this.max = max;
		this.possible = possibleNames;
		this.spawnIn = spawnIn;
	}

	public int getMaxSpawnable() {
		return this.max;
	}

	public String getPossibleNames() {
		return possible;
	}

	public Class<? extends CreatureNPC> getEntityClass() {
		return instance;
	}

	public Material getSpawnIn() {
		return spawnIn;
	}
}