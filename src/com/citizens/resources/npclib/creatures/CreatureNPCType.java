package com.citizens.resources.npclib.creatures;

import com.citizens.SettingsManager.Constant;
import com.citizens.resources.npclib.creatures.SpawnValidator.Spawn.Range;
import com.citizens.resources.npclib.creatures.SpawnValidator.Spawn.Type;

public enum CreatureNPCType {
	EVIL(EvilCreatureNPC.class, Constant.MaxEvils.getInt(), Constant.EvilNames
			.getString(), new SpawnValidator(Type.JUST, 0), new SpawnValidator(
			Range.DEFAULT, false)), PIRATE(
			PirateCreatureNPC.class,
			Constant.MaxPirates.getInt(),
			Constant.PirateNames.getString(),
			new SpawnValidator(Type.JUST, 0),
			new SpawnValidator(Type.INCLUDING, 8, 9));
	private final int max;
	private final String possible;
	private final Class<? extends CreatureNPC> instance;
	private final SpawnValidator spawnIn;
	private final SpawnValidator spawnOn;
	private Spawner spawner = new DefaultSpawner();

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

	public Spawner getSpawner() {
		return spawner;
	}

	public void setSpawner(Spawner spawner) {
		this.spawner = spawner;
	}

	public static CreatureNPCType fromName(String mob) {
		return CreatureNPCType.valueOf(mob.toUpperCase().replace("CREATURENPC",
				""));
	}
}