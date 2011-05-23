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

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class RegionHandler {
	private List<String> allowed = new ArrayList<String>();
	private List<String> blacklisted = new ArrayList<String>();

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
		String mobType = "";
		if (entity instanceof Creeper) {
			mobType = "creeper";
		} else if (entity instanceof Zombie) {
			mobType = "zombie";
		} else if (entity instanceof Skeleton) {
			mobType = "skeleton";
		} else if (entity instanceof Spider) {
			mobType = "spider";
		} else if (entity instanceof Ghast) {
			mobType = "ghast";
		} else if (entity instanceof PigZombie) {
			mobType = "pigzombie";
		} else if (entity instanceof Giant) {
			mobType = "giant";
		} else if (entity instanceof Cow) {
			mobType = "cow";
		} else if (entity instanceof Chicken) {
			mobType = "chicken";
		} else if (entity instanceof Squid) {
			mobType = "squid";
		} else if (entity instanceof Sheep) {
			mobType = "sheep";
		} else if (entity instanceof Pig) {
			mobType = "pig";
		} else if (entity instanceof Wolf) {
			mobType = "wolf";
		} else if (entity instanceof HumanNPC) {
			mobType = ((HumanNPC) entity).getStrippedName();
		} else if (entity instanceof Player) {
			mobType = ((Player) entity).getName();
		}
		return mobType;
	}
}