package com.citizens.utils;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

public class EntityUtils {
	public static String getMonsterName(LivingEntity le) {
		if (le instanceof Chicken) {
			return "chicken";
		} else if (le instanceof Cow) {
			return "cow";
		} else if (le instanceof Creeper) {
			return "creeper";
		} else if (le instanceof Ghast) {
			return "ghast";
		} else if (le instanceof Giant) {
			return "giant";
		} else if (le instanceof Pig) {
			return "pig";
		} else if (le instanceof PigZombie) {
			return "pigzombie";
		} else if (le instanceof Sheep) {
			return "sheep";
		} else if (le instanceof Skeleton) {
			return "skeleton";
		} else if (le instanceof Slime) {
			return "slime";
		} else if (le instanceof Spider) {
			return "spider";
		} else if (le instanceof Squid) {
			return "squid";
		} else if (le instanceof Wolf) {
			return "wolf";
		} else if (le instanceof Zombie) {
			return "zombie";
		}
		return "";
	}

	public static CreatureType getType(Entity entity) {
		if (entity instanceof Chicken) {
			return CreatureType.CHICKEN;
		} else if (entity instanceof Cow) {
			return CreatureType.COW;
		} else if (entity instanceof Creeper) {
			return CreatureType.CREEPER;
		} else if (entity instanceof Ghast) {
			return CreatureType.GHAST;
		} else if (entity instanceof Giant) {
			return CreatureType.GIANT;
		} else if (entity instanceof Pig) {
			return CreatureType.PIG;
		} else if (entity instanceof PigZombie) {
			return CreatureType.PIG_ZOMBIE;
		} else if (entity instanceof Sheep) {
			return CreatureType.SHEEP;
		} else if (entity instanceof Skeleton) {
			return CreatureType.SKELETON;
		} else if (entity instanceof Slime) {
			return CreatureType.SLIME;
		} else if (entity instanceof Spider) {
			return CreatureType.SPIDER;
		} else if (entity instanceof Squid) {
			return CreatureType.SQUID;
		} else if (entity instanceof Wolf) {
			return CreatureType.WOLF;
		} else if (entity instanceof Zombie) {
			return CreatureType.ZOMBIE;
		}
		return null;
	}
}
