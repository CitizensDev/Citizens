package net.citizensnpcs.resources.npclib.creatures;

import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.utils.StringUtils;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public enum CreatureNPCType {
	EVIL(EvilCreatureNPC.class, "Evil", SpawnValidator.DEFAULT_SPAWNIN,
			SpawnValidator.DEFAULT_SPAWNON);

	private final int max;
	private final int spawnChance;
	private final String possible;
	private final boolean spawn;
	private Constructor<? extends CreatureNPC> instance;
	private final SpawnValidator spawnIn;
	private final SpawnValidator spawnOn;
	private Spawner spawner = DefaultSpawner.INSTANCE;
	private final String name;

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
		this.name = name;
	}

	private final static Map<String, CreatureNPCType> mapping = Maps
			.newHashMap();
	private final static List<CreatureNPCType> spawning = Lists.newArrayList();

	static {
		for (CreatureNPCType type : EnumSet.allOf(CreatureNPCType.class)) {
			mapping.put(type.name, type);
			if (type.isSpawn())
				spawning.add(type);
		}
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
		return mapping.get(mob);
	}

	public int getSpawnChance() {
		return this.spawnChance;
	}

	public boolean isSpawn() {
		return spawn;
	}

	public static CreatureNPCType getRandomType(Random random) {
		return spawning.size() == 0 ? null : spawning.get(random
				.nextInt(spawning.size()));
	}
}