package net.citizensnpcs.resources.npclib.creatures;

import net.citizensnpcs.properties.SettingsManager;
import net.citizensnpcs.resources.npclib.creatures.SpawnValidator.Spawn.Range;
import net.citizensnpcs.resources.npclib.creatures.SpawnValidator.Spawn.Type;

public enum CreatureNPCType {
	EVIL(
			EvilCreatureNPC.class,
			SettingsManager.getInt("MaxEvils"),
			SettingsManager.getString("EvilNames"),
			new SpawnValidator(Type.JUST, 0),
			new SpawnValidator(Range.DEFAULT, false));
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