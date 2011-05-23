package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.Map.Entry;

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
	private RegionHandler region = new RegionHandler();

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
						if (!NPCManager.validateOwnership(player, npc.getUID())) {
							if (LocationUtils.checkLocation(npc.getLocation(),
									player.getLocation(), npc.getGuard()
											.getProtectionRadius())) {
								cacheActions(player, npc, npc.getUID(), name);
							} else {
								resetActions(npc.getUID(), name, npc);
							}
						}
					}
				}
			}
		}
	}

	private void cacheActions(Player p, HumanNPC npc, int entityID, String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("attemptedEntry") && isBlacklisted(p)) {
			attack(p, npc);
			cached.set("attemptedEntry");
		}
		ActionManager.putAction(entityID, name, cached);
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "attemptedEntry", npc
				.getNPCData().isTalkClose());
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
	 * Attack a player if they enter a bouncer's protection zone
	 * 
	 * @param player
	 * @param npc
	 */
	private void attack(Player player, HumanNPC npc) {
		player.sendMessage("Guardin'!");
		PathUtils.targetPlayer(npc, player, true);
	}
}