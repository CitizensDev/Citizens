package net.citizensnpcs.utils;

import net.citizensnpcs.lib.creatures.CreatureNPCType;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;

public class EntityUtils {
	public static String getMonsterName(Entity ent) {
		CreatureType type = getType(ent);
		return type != null ? type.getName().toLowerCase() : "";
	}

	public static CreatureType getType(Entity entity) {
		for (CreatureType type : CreatureType.values()) {
			if (type.getEntityClass().isAssignableFrom(entity.getClass()))
				return type;
		}
		return null;
	}

	public static boolean validType(String name) {
		return validType(name, false);
	}

	public static boolean validType(String name, boolean both) {
		String formatted = StringUtils.capitalise(name.toLowerCase()).replace(
				" ", "");
		if (formatted.equals("Pigzombie")) {
			formatted = "PigZombie";
		}
		if (formatted.equals("Cavespider")) {
			formatted = "CaveSpider";
		}
		if (formatted.equals("Enderdragon")) {
			formatted = "EnderDragon";
		}
		if (formatted.equals("Mushroomcow")) {
			formatted = "MushroomCow";
		}
		return CreatureType.fromName(formatted) != null
				|| (both && CreatureNPCType.fromName(formatted) != null);
	}
}
