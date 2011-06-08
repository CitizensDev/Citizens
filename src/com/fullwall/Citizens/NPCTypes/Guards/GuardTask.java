package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Misc.ActionManager;
import com.fullwall.Citizens.Misc.CachedAction;
import com.fullwall.Citizens.Misc.NPCLocation;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.Citizens.Utils.PathUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardTask implements Runnable {
	@SuppressWarnings("unused")
	private final Citizens plugin;
	private LivingEntity entity;
	private final static Map<String, NPCLocation> toRespawn = new HashMap<String, NPCLocation>();

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
				} else if (guard.isBodyguard()) {
					String owner = npc.getOwner();
					Player p = Bukkit.getServer().getPlayer(owner);
					if (p != null && p.isOnline()) {
						Location ownerloc = p.getLocation();
						if (NPCManager.get(npc.getUID()) == null) {
							npc.getNPCData().setLocation(p.getLocation());
						}
						for (Entity temp : p.getNearbyEntities(ownerloc.getX(),
								ownerloc.getY(), ownerloc.getZ())) {
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
							if (LocationUtils.checkLocation(ownerloc,
									entity.getLocation(), 25)) {
								cacheActions(npc, entity, entity.getEntityId(),
										name);
							} else {
								resetActions(entity.getEntityId(), name, npc);
							}
						}
						entity = null;
						if (LocationUtils.checkLocation(npc.getLocation(),
								p.getLocation(), 25)) {
							npc.target(p, false, -1, 2, 25);
						} else {
							npc.teleport(p.getLocation());
						}
					} else {
						if (NPCManager.get(npc.getUID()) != null) {
							PathUtils.cancelPath(npc);
							toRespawn.put(
									npc.getOwner(),
									new NPCLocation(npc.getLocation(), npc
											.getUID(), npc.getOwner()));
							NPCManager.despawn(npc.getUID());
						}
					}
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
			name = "pigzombie";
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
		if (npc.getGuard().isBouncer()) {
			CachedAction cached = ActionManager.getAction(entityID, name);
			if (!cached.has("attemptedEntry")) {
				if (isBlacklisted(npc, name)
						|| (entity instanceof Player && !((Player) entity)
								.getName().equals(npc.getOwner()))) {
					attack(entity, npc);
				}
				cached.set("attemptedEntry");
			}
			ActionManager.putAction(entityID, name, cached);
		} else if (npc.getGuard().isBodyguard()) {
			String mob = getTypeFromEntity(entity);
			if (npc.getGuard().isAggressive()) {
				if (entity instanceof Player) {
					if (!npc.getGuard().getWhitelist().contains(name)
							&& !npc.getGuard().getWhitelist().contains("all")) {
						attack(entity, npc);
					}
				} else if (!(entity instanceof Player)
						&& CreatureType.fromName(mob.replaceFirst(
								"" + mob.charAt(0),
								"" + Character.toUpperCase(mob.charAt(0)))) != null) {
					if (npc.getGuard().getBlacklist()
							.contains(getTypeFromEntity(entity))
							|| npc.getGuard().getBlacklist().contains("all")) {
						attack(entity, npc);
					}
				}
			}
		} else {
			// Dostuffherelater
		}
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
		if (npc.getGuard().getBlacklist().contains(name)) {
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

	public static void checkRespawn(Player player) {
		String owner = player.getName();
		if (toRespawn.containsKey(owner)) {
			NPCManager.register(toRespawn.get(owner).getUID(), owner);
			NPCManager.get(toRespawn.get(owner).getUID()).teleport(
					player.getLocation());
		}
	}
}