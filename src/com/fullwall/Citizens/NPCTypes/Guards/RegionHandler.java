package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

public class RegionHandler {
	private List<String> allowed = new ArrayList<String>();
	private List<String> blacklisted = new ArrayList<String>();
	private String mobType = "";

	public List<String> getAllowed() {
		return allowed;
	}

	public boolean isAllowed(String name) {
		if (allowed.contains(name)) {
			return true;
		} else {
			return false;
		}
	}

	public List<String> getBlacklisted() {
		return blacklisted;
	}

	public boolean isBlacklisted(String entity) {
		if (blacklisted.contains(entity)) {
			return true;
		} else {
			return false;
		}
	}

	public String getMobType(Entity entity) {
		if (entity instanceof Creeper) {
			mobType = "creeper";
		}
		if (entity instanceof Zombie) {
			mobType = "zombie";
		}
		if (entity instanceof Skeleton) {
			mobType = "skeleton";
		}
		if (entity instanceof Spider) {
			mobType = "spider";
		}
		if (entity instanceof Ghast) {
			mobType = "ghast";
		}
		if (entity instanceof PigZombie) {
			mobType = "pigzombie";
		}
		if (entity instanceof Giant) {
			mobType = "giant";
		}
		if (entity instanceof Cow) {
			mobType = "cow";
		}
		if (entity instanceof Chicken) {
			mobType = "chicken";
		}
		if (entity instanceof Squid) {
			mobType = "squid";
		}
		if (entity instanceof Sheep) {
			mobType = "sheep";
		}
		if (entity instanceof Pig) {
			mobType = "pig";
		}
		if (entity instanceof Wolf) {
			mobType = "wolf";
		}
		return mobType;
	}
}