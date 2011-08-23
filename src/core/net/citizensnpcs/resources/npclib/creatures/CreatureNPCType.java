package net.citizensnpcs.resources.npclib.creatures;

import java.lang.reflect.Constructor;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.utils.StringUtils;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

public enum CreatureNPCType {
	EVIL(
			EvilCreatureNPC.class,
			"Evil",
			SpawnValidator.DEFAULT_SPAWNIN,
			SpawnValidator.DEFAULT_SPAWNON);
	private final int max;
	private final int spawnChance;
	private final String possible;
	private final boolean spawn;
	private Constructor<? extends CreatureNPC> instance;
	private final SpawnValidator spawnIn;
	private final SpawnValidator spawnOn;
	private Spawner spawner = DefaultSpawner.INSTANCE;

	CreatureNPCType(Class<? extends CreatureNPC> instance, String name,
			SpawnValidator spawnIn, SpawnValidator spawnOn) {
		name = StringUtils.capitalise(name.toLowerCase());
		try {
			this.instance = instance.getConstructor(MinecraftServer.class,
					World.class, String.class, ItemInWorldManager.class);
		} catch (Exception ex) {
		}
		this.spawn = SettingsManager.getBoolean("Spawn" + name + "s");
		this.max = SettingsManager.getInt("Max" + name + "s");
		this.spawnChance = SettingsManager.getInt(name + "SpawnChance");
		this.possible = SettingsManager.getString(name + "Names");
		this.spawnIn = spawnIn;
		this.spawnOn = spawnOn;
	}

	public int getMaxSpawnable() {
		return this.max;
	}

	public String getPossibleNames() {
		return possible;
	}

	public Constructor<? extends CreatureNPC> getEntityConstructor() {
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

	public boolean isSpawn() {
		return spawn;
	}
}