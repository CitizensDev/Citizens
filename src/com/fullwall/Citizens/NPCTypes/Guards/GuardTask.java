package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.ActionManager;
import com.fullwall.Citizens.CachedAction;
import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.Citizens.Utils.PathUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardTask implements Runnable {
	private Citizens plugin;

	public GuardTask(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			HumanNPC npc = entry.getValue();
			if (npc.isGuard()) {
				if (npc.getGuard().isBouncer()) {
					Location loc = npc.getLocation();
					for (Entity entity : npc.getPlayer().getNearbyEntities(
							loc.getX(), loc.getY(), loc.getZ())) {
						if (entity instanceof Player) {
							for (Player player : plugin.getServer()
									.getOnlinePlayers()) {
								String name = player.getName();
								if (!NPCManager.validateOwnership(player,
										npc.getUID())) {
									if (LocationUtils.checkLocation(npc
											.getLocation(), player
											.getLocation(), npc.getGuard()
											.getProtectionRadius())) {
										cacheActions(npc, player, npc.getUID(),
												name);
									} else {
										resetActions(npc.getUID(), name, npc);
									}
								}
							}
						} else {
							if (LocationUtils.checkLocation(loc, entity
									.getLocation(), npc.getGuard()
									.getProtectionRadius())) {
								cacheActions(npc, entity, npc.getUID(),
										entity.toString());
							}
						}
					}
				}
			}
		}
	}

	private void cacheActions(HumanNPC npc, Entity entity, int entityID,
			String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("attemptedEntry")) {
			if (isBlacklisted(npc, entity) || entity instanceof Player) {
				attack(entity, npc);
			}
			cached.set("attemptedEntry");
		}
		ActionManager.putAction(entityID, name, cached);
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "attemptedEntry", npc
				.getNPCData().isTalkClose());
	}

	/**
	 * Check if a mob is blacklisted from entry
	 * 
	 * @param entity
	 */
	private boolean isBlacklisted(HumanNPC npc, Entity entity) {
		if (npc.getGuard().getMobBlacklist()
				.contains(entity.toString().toLowerCase())) {
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
	private void attack(Entity entity, HumanNPC npc) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			PathUtils.target(npc, player, true);
		} else {
			PathUtils.target(npc, (LivingEntity) entity, true);
		}
	}
}