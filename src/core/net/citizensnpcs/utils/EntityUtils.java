package net.citizensnpcs.utils;

import net.citizensnpcs.resources.npclib.creatures.CreatureNPCType;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

public class EntityUtils {
	public static String getMonsterName(Entity ent) {
		if (ent instanceof Blaze) {
			return "blaze";
		} else if (ent instanceof CaveSpider) {
			return "cavespider";
		} else if (ent instanceof Chicken) {
			return "chicken";
		} else if (ent instanceof Cow) {
			return "cow";
		} else if (ent instanceof Creeper) {
			return "creeper";
		} else if (ent instanceof EnderDragon) {
			return "enderdragon";
		} else if (ent instanceof Enderman) {
			return "enderman";
		} else if (ent instanceof Ghast) {
			return "ghast";
		} else if (ent instanceof Giant) {
			return "giant";
		} else if (ent instanceof MushroomCow) {
			return "mushroomcow";
		} else if (ent instanceof Pig) {
			return "pig";
		} else if (ent instanceof PigZombie) {
			return "pigzombie";
		} else if (ent instanceof Sheep) {
			return "sheep";
		} else if (ent instanceof Silverfish) {
			return "silverfish";
		} else if (ent instanceof Skeleton) {
			return "skeleton";
		} else if (ent instanceof Slime) {
			return "slime";
		} else if (ent instanceof Snowman) {
			return "snowman";
		} else if (ent instanceof Spider) {
			return "spider";
		} else if (ent instanceof Squid) {
			return "squid";
		} else if (ent instanceof Wolf) {
			return "wolf";
		} else if (ent instanceof Villager) {
			return "villager";
		} else if (ent instanceof Zombie) {
			return "zombie";
		}
		return "";
	}

	public static CreatureType getType(Entity entity) {
		if (entity instanceof Blaze) {
			return CreatureType.BLAZE;
		} else if (entity instanceof CaveSpider) {
			return CreatureType.CAVE_SPIDER;
		} else if (entity instanceof Chicken) {
			return CreatureType.CHICKEN;
		} else if (entity instanceof Cow) {
			return CreatureType.COW;
		} else if (entity instanceof Creeper) {
			return CreatureType.CREEPER;
		} else if (entity instanceof Enderman) {
			return CreatureType.ENDERMAN;
		} else if (entity instanceof EnderDragon) {
			return CreatureType.ENDER_DRAGON;
		} else if (entity instanceof Ghast) {
			return CreatureType.GHAST;
		} else if (entity instanceof Giant) {
			return CreatureType.GIANT;
		} else if (entity instanceof MushroomCow) {
			return CreatureType.MUSHROOM_COW;
		} else if (entity instanceof Pig) {
			return CreatureType.PIG;
		} else if (entity instanceof PigZombie) {
			return CreatureType.PIG_ZOMBIE;
		} else if (entity instanceof Sheep) {
			return CreatureType.SHEEP;
		} else if (entity instanceof Skeleton) {
			return CreatureType.SKELETON;
		} else if (entity instanceof Silverfish) {
			return CreatureType.SILVERFISH;
		} else if (entity instanceof Slime) {
			return CreatureType.SLIME;
		} else if (entity instanceof Spider) {
			return CreatureType.SPIDER;
		} else if (entity instanceof Squid) {
			return CreatureType.SQUID;
		} else if (entity instanceof Wolf) {
			return CreatureType.WOLF;
		} else if (entity instanceof Villager) {
			return CreatureType.VILLAGER;
		} else if (entity instanceof Zombie) {
			return CreatureType.ZOMBIE;
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
