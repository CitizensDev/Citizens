package com.fullwall.resources.redecouverte.NPClib.Creatures;

import com.fullwall.Citizens.Constants;
import com.fullwall.resources.redecouverte.NPClib.Creatures.SpawnValidator.Spawn.Range;
import com.fullwall.resources.redecouverte.NPClib.Creatures.SpawnValidator.Spawn.Type;

public enum CreatureNPCType {
	// TODO - might be a little longwinded on constructing validators.
	EVIL(EvilCreatureNPC.class, Constants.maxEvils, Constants.evilNames,
			new SpawnValidator(Type.ALL_EXCEPT, 0).set(Range.LIQUIDS, false),
			new SpawnValidator(Range.DEFAULT, false)), 
	PIRATE(PirateCreatureNPC.class, Constants.maxPirates, Constants.pirateNames,
			new SpawnValidator(Type.ALL_EXCEPT, 0).set(Range.LIQUIDS, false),
			new SpawnValidator(Type.INCLUDING, 8, 9));
	private final int max;
	private final String possible;
	private final Class<? extends CreatureNPC> instance;
	private final SpawnValidator spawnIn;
	private final SpawnValidator spawnOn;

	CreatureNPCType(Class<? extends CreatureNPC> instance, int max,
			String possibleNames, SpawnValidator spawnIn, SpawnValidator spawnOn) {
		this.instance = instance;
		this.max = max;
		this.possible = possibleNames;
		this.spawnIn = spawnIn;
		this.spawnOn = spawnOn;
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

	public SpawnValidator spawnIn() {
		return spawnIn;
	}

	public SpawnValidator spawnOn() {
		return spawnOn;
	}
}