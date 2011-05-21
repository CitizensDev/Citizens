package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardTask implements Runnable {
	private Citizens plugin;
	private RegionHandler region = new RegionHandler();
	private HashMap<String, HashMap<Integer, Boolean>> hasAttemptedEntry = new HashMap<String, HashMap<Integer, Boolean>>();

	public GuardTask(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			HumanNPC npc = entry.getValue();
			if (npc.isGuard()) {
				if (npc.getGuard().isBouncer()) {
					for (Player player : plugin.getServer().getOnlinePlayers()) {
						String name = player.getName();
						if (LocationUtils.checkLocation(npc.getLocation(),
								player.getLocation(),
								Constants.guardProtectionRadius)) {
							if (isBlacklisted(player)) {
								if (hasAttemptedEntry.get(name) == null
										|| hasAttemptedEntry.get(name).get(
												npc.getUID()) == null
										|| hasAttemptedEntry.get(name).get(
												npc.getUID()) == false) {
									blockEntry(player, npc);
									HashMap<Integer, Boolean> npcs = new HashMap<Integer, Boolean>();
									if (hasAttemptedEntry.get(name) != null) {
										npcs = hasAttemptedEntry.get(name);
									}
									npcs.put(npc.getUID(), true);
									hasAttemptedEntry.put(name, npcs);
								}
							}
						} else if (hasAttemptedEntry.get(name) != null
								&& hasAttemptedEntry.get(name)
										.get(npc.getUID()) != null
								&& hasAttemptedEntry.get(name)
										.get(npc.getUID()) == true) {
							hasAttemptedEntry.get(name)
									.put(npc.getUID(), false);
						}
					}
				}
			}
		}
	}

	/**
	 * Check if a player is blacklisted from entry
	 * 
	 * @param entity
	 */
	private boolean isBlacklisted(Player player) {
		if (!region.isAllowed(player.getName())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Block a player from entering a protected zone
	 * 
	 * @param player
	 * @param npc
	 */
	private void blockEntry(Player player, HumanNPC npc) {
		player.sendMessage(npc.getStrippedName()
				+ " has blocked you from entering this zone.");
	}
}