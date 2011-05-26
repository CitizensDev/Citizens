package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.ActionManager;
import com.fullwall.Citizens.Utils.CachedAction;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.Citizens.Utils.PathUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardTask implements Runnable {
	@SuppressWarnings("unused")
	private Citizens plugin;
	private LivingEntity entity;

	public GuardTask(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			HumanNPC npc = entry.getValue();
			if (npc.isGuard()) {
				GuardNPC guard = npc.getGuard();
				if (guard.isBouncer()) {
					Location loc = npc.getLocation();
					for (Entity temp : npc.getPlayer().getNearbyEntities(
							guard.getHalvedRadius(),
							guard.getProtectionRadius(),
							guard.getHalvedRadius())) {
						if (!(temp instanceof LivingEntity)) {
							continue;
						}
						entity = (LivingEntity) temp;
						String name = "";
						if (entity instanceof Player) {
							Player player = (Player) entity;
							if (!NPCManager.validateOwnership(player,
									npc.getUID())) {
								name = player.getName();
							}
						} else {
							name = getTypeFromEntity(entity);
						}
						if (LocationUtils.checkLocation(loc, entity
								.getLocation(), npc.getGuard()
								.getProtectionRadius())) {
							cacheActions(npc, entity, entity.getEntityId(),
									name);
						} else {
							resetActions(entity.getEntityId(), name, npc);
						}
					}
					entity = null;
				}
			}
		}
	}

	private String getTypeFromEntity(Entity entity) {
		String name = "";
		if (entity instanceof Chicken) {
			name = "chicken";
		} else if (entity instanceof Cow) {
			name = "cow";
		} else if (entity instanceof Creeper) {
			name = "creeper";
		} else if (entity instanceof Ghast) {
			name = "ghast";
		} else if (entity instanceof Giant) {
			name = "giant";
		} else if (entity instanceof Pig) {
			name = "pig";
		} else if (entity instanceof PigZombie) {
			name = "sigZombie";
		} else if (entity instanceof Sheep) {
			name = "sheep";
		} else if (entity instanceof Skeleton) {
			name = "skeleton";
		} else if (entity instanceof Slime) {
			name = "slime";
		} else if (entity instanceof Spider) {
			name = "spider";
		} else if (entity instanceof Squid) {
			name = "squid";
		} else if (entity instanceof Wolf) {
			name = "wolf";
		} else if (entity instanceof Zombie) {
			name = "zombie";
		}
		return name;
	}

	private void cacheActions(HumanNPC npc, LivingEntity entity, int entityID,
			String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("attemptedEntry")) {
			if (isBlacklisted(npc, name) || entity instanceof Player) {
				attack(entity, npc);
			}
			cached.set("attemptedEntry");
		}
		ActionManager.putAction(entityID, name, cached);
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "attemptedEntry");
	}

	/**
	 * Check if a mob is blacklisted from entry
	 * 
	 * @param entity
	 */
	private boolean isBlacklisted(HumanNPC npc, String name) {
		if (npc.getGuard().getMobBlacklist().contains(name)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Attack a player/mob if they enter a bouncer's protection zone
	 * 
	 * @param player
	 * @param npc
	 */
	private void attack(LivingEntity entity, HumanNPC npc) {
		PathUtils.target(npc, entity, true);
	}
}