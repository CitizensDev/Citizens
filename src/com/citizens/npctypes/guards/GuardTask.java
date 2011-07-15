package com.citizens.npctypes.guards;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.citizens.misc.ActionManager;
import com.citizens.misc.CachedAction;
import com.citizens.misc.NPCLocation;
import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.npclib.creatures.CreatureNPC;
import com.citizens.utils.LocationUtils;
import com.citizens.utils.PathUtils;
import com.citizens.utils.StringUtils;

public class GuardTask implements Runnable {
	private LivingEntity entity;
	private final static Map<String, NPCLocation> toRespawn = new HashMap<String, NPCLocation>();

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			HumanNPC npc = entry.getValue();
			if (npc.isType("guard")) {
				GuardNPC guard = npc.getToggleable("guard");
				if (guard.isAttacking() && !npc.getHandle().hasTarget()) {
					GuardManager.returnToBase(npc);
					guard.setAttacking(false);
				}
				if (guard.isAttacking()
						&& npc.getHandle().hasTarget()
						&& (!LocationUtils.checkLocation(npc.getLocation(), npc
								.getHandle().getTarget().getLocation(),
								guard.getProtectionRadius()) || !LocationUtils
								.checkLocation(npc.getLocation(), npc
										.getNPCData().getLocation(), guard
										.getProtectionRadius()))) {
					npc.getHandle().cancelTarget();
					GuardManager.returnToBase(npc);
					guard.setAttacking(false);
				}
				if (guard.isAttacking()) {
					continue;
				}
				if (npc.isPaused()
						&& LocationUtils.checkLocation(npc.getLocation(), npc
								.getNPCData().getLocation())) {
					npc.setPaused(false);
				}
				if (guard.isBouncer()) {
					Location loc = npc.getLocation();
					for (Entity temp : npc.getPlayer().getNearbyEntities(
							guard.getHalvedProtectionRadius(),
							guard.getProtectionRadius(),
							guard.getHalvedProtectionRadius())) {
						if (!(temp instanceof LivingEntity)) {
							continue;
						}
						entity = (LivingEntity) temp;
						String name = getNameFromEntity(entity, npc.getUID());
						if (name.isEmpty())
							continue;
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
					if (!npc.isPaused())
						npc.setPaused(true);
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
							String name = getNameFromEntity(entity,
									npc.getUID());
							if (name.isEmpty())
								continue;
							if (LocationUtils.checkLocation(ownerloc,
									entity.getLocation(),
									guard.getProtectionRadius())) {
								cacheActions(npc, entity, entity.getEntityId(),
										name);
							} else {
								resetActions(entity.getEntityId(), name, npc);
							}
						}
						entity = null;
						if (LocationUtils.checkLocation(npc.getLocation(),
								p.getLocation(), 25)) {
							PathUtils.target(npc, p, false, -1, -1, 25);
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

	private String getNameFromEntity(Entity entity, int UID) {
		String name = "";
		if (((CraftEntity) entity).getHandle() instanceof CreatureNPC) {
			name = "npcmob."
					+ ((CreatureNPC) ((CraftEntity) entity).getHandle())
							.getType().name().toLowerCase()
							.replace("creaturenpc", "");
		} else if (entity instanceof Player) {
			Player player = (Player) entity;
			if (!NPCManager.validateOwnership(player, UID, false)) {
				name = player.getName();
			} else
				return "";
		} else {
			name = entity.getClass().getName().toLowerCase()
					.replace("org.bukkit.craftbukkit.entity.craft", "");
		}
		return name;
	}

	private void cacheActions(HumanNPC npc, LivingEntity entity, int entityID,
			String name) {
		GuardNPC guard = npc.getToggleable("guard");
		if (!guard.isAggressive()) {
			return;
		}
		if (guard.isBouncer()) {
			CachedAction cached = ActionManager.getAction(entityID, name);
			if (!cached.has("attemptedEntry")) {
				boolean mob = false;
				if (name.contains("npcmob.")) {
					name = name.replace("npcmob.", "");
					mob = true;
				}
				if (isBlacklisted(npc, name)
						|| (!mob && entity instanceof Player && !guard
								.isWhiteListed((Player) entity))) {
					attack(entity, guard);
					guard.setAttacking(true);
				}
				cached.set("attemptedEntry");
			}
			ActionManager.putAction(entityID, name, cached);
		} else if (guard.isBodyguard()) {
			if (entity instanceof Player) {
				if (!guard.isWhiteListed((Player) entity)) {
					attack(entity, guard);
				}
			} else if (CreatureType.fromName(StringUtils.capitalise(name
					.toLowerCase())) != null) {
				if (isBlacklisted(npc, name)) {
					attack(entity, guard);
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
	private void attack(LivingEntity entity, GuardNPC guard) {
		guard.target(entity);
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