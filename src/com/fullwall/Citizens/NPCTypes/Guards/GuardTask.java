package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.Map.Entry;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardTask implements Runnable {
	private Citizens plugin;
	private RegionHandler region = new RegionHandler();

	public GuardTask(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			HumanNPC npc = entry.getValue();
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				if (LocationUtils.checkLocation(npc.getLocation(),
						player.getLocation(), Constants.guardProtectionRadius)) {
					for (Entity entity : player.getNearbyEntities(npc.getX(),
							npc.getY(), npc.getZ())) {
						if (isBlacklisted(entity)) {
							blockEntry(entity, npc);
						}
					}
				}
			}
		}
	}

	/**
	 * Check if a player or mob is blacklisted from entry
	 * 
	 * @param entity
	 */
	private boolean isBlacklisted(Entity entity) {
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (!region.isAllowed(p.getName())) {
				return true;
			}
		}
		if (region.isBlacklisted(region.getMobType(entity))) {
			return true;
		} else {
			return false;
		}
	}

	private void blockEntry(Entity entity, HumanNPC npc) {
		if (entity instanceof Player) {
			Player p = (Player) entity;
			p.sendMessage(npc.getStrippedName()
					+ " has blocked you from entering this zone.");
		}
	}
}