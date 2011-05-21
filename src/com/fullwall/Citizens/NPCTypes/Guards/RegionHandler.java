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
import org.bukkit.entity.Player;
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

	/**
	 * Get the players allowed to enter a zone
	 * 
	 * @return
	 */
	public List<String> getAllowed() {
		return allowed;
	}

	/**
	 * Add a player to the allowed list
	 * 
	 * @param player
	 */
	public void addAllowed(Player player) {
		allowed.add(player.getName());
	}

	/**
	 * Remove a player from the allowed list
	 * 
	 * @param player
	 */
	public void removeAllowed(Player player) {
		if (allowed.contains(player.getName())) {
			allowed.remove(player.getName());
		}
	}

	/**
	 * Get whether a player is allowed to enter a zone
	 * 
	 * @param name
	 * @return
	 */
	public boolean isAllowed(String name) {
		if (allowed.contains(name)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get mobs blacklisted from entry
	 * 
	 * @return
	 */
	public List<String> getBlacklisted() {
		return blacklisted;
	}

	/**
	 * Get whether a mob is blacklisted from entry
	 * 
	 * @param entity
	 * @return
	 */
	public boolean isBlacklisted(String entity) {
		if (blacklisted.contains(entity)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the a string of a mob type
	 * 
	 * @param entity
	 * @return
	 */
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