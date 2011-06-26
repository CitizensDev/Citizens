package com.Citizens.NPCTypes.Guards;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.Misc.ActionManager;
import com.Citizens.Misc.CachedAction;
import com.Citizens.Misc.NPCLocation;
import com.Citizens.NPCTypes.Guards.GuardNPC;
import com.Citizens.NPCs.NPCManager;
import com.Citizens.Utils.LocationUtils;
import com.Citizens.Utils.PathUtils;
import com.Citizens.Utils.StringUtils;

public class GuardTask implements Runnable {
	private LivingEntity entity;
	private final static Map<String, NPCLocation> toRespawn = new HashMap<String, NPCLocation>();

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			HumanNPC npc = entry.getValue();
			if (npc.isType("guard")) {
				GuardNPC guard = npc.getToggleable("guard");
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
							name = entity.getClass().getName().toLowerCase()
									.replace("entity", "");
						}
						if (LocationUtils.checkLocation(loc,
								entity.getLocation(),
								guard.getProtectionRadius())) {
							cacheActions(npc, entity, entity.getEntityId(),
									name);
						} else {
							resetActions(entity.getEntityId(), name, npc);
						}
					}
					entity = null;
				} else if (guard.isBodyguard()) {
					Player p = Bukkit.getServer().getPlayer(npc.getOwner());
					if (p != null) {
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
								name = entity.getClass().getName()
										.toLowerCase();
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

	private void cacheActions(HumanNPC npc, LivingEntity entity, int entityID,
			String name) {
		GuardNPC guard = npc.getToggleable("guard");
		if (guard.isBouncer()) {
			CachedAction cached = ActionManager.getAction(entityID, name);
			if (!cached.has("attemptedEntry")) {
				if (isBlacklisted(npc, name)
						|| (entity instanceof Player && !name.equals(npc
								.getOwner()))) {
					attack(entity, npc);
				}
				cached.set("attemptedEntry");
			}
			ActionManager.putAction(entityID, name, cached);
		} else if (guard.isBodyguard()) {
			if (guard.isAggressive()) {
				if (entity instanceof Player) {
					if (!guard.getWhitelist().contains(name)
							&& !guard.getWhitelist().contains("all")) {
						attack(entity, npc);
					}
				} else if (!(entity instanceof Player)
						&& CreatureType.fromName(StringUtils.capitalise(name)) != null) {
					if (isBlacklisted(npc, name)
							|| guard.getBlacklist().contains("all")) {
						attack(entity, npc);
					}
				}
			}
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
		return ((GuardNPC) npc.getToggleable("guard")).getBlacklist().contains(
				name);
	}

	/**
	 * Attack a player/mob if they enter a bouncer's protection zone
	 * 
	 * @param player
	 * @param npc
	 */
	private void attack(LivingEntity entity, HumanNPC npc) {
		PathUtils.target(npc, entity, true, -1, -1, 25);
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