package net.citizensnpcs.resources.npclib.creatures;

import net.citizensnpcs.properties.SettingsManager;

public enum CreatureNPCType {
	EVIL(EvilCreatureNPC.class, SettingsManager.getInt("MaxEvils"),
			SettingsManager.getInt("EvilSpawnChance"), SettingsManager
					.getString("EvilNames"), SpawnValidator.DEFAULT_SPAWNIN,
			SpawnValidator.DEFAULT_SPAWNON);
	private final int max;
	private final int spawnChance;
	private final String possible;
	private final Class<? extends CreatureNPC> instance;
	private final SpawnValidator spawnIn;
	private final SpawnValidator spawnOn;
	private Spawner spawner = DefaultSpawner.INSTANCE;

	CreatureNPCType(Class<? extends CreatureNPC> instance, int max,
			int spawnChance, String possibleNames, SpawnValidator spawnIn,
			SpawnValidator spawnOn) {
		this.instance = instance;
		this.max = max;
		this.spawnChance = spawnChance;
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
		try {
			return CreatureNPCType.valueOf(mob.toUpperCase().replace(
					"CREATURENPC", ""));
		} catch (Exception ex) {
			return null;
		}
	}

	public int getSpawnChance() {
		return this.spawnChance;
	}
}