package com.fullwall.Citizens;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TickTask implements Runnable {

	private Citizens plugin;
	// How far an NPC can 'see'
	private double range;

	public TickTask(Citizens plugin, double range) {
		this.plugin = plugin;
		// range is checked in both directions, so we halve the passed range.
		this.range = range / 2;
	}

	@Override
	public void run() {
		HumanNPC npc;
		int UID;
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			{
				npc = entry.getValue();
				npc.updateMovement();
				UID = entry.getKey();
				for (Player p : online) {
					String name = p.getName();
					if (npc.getNPCData().isLookClose()
							|| npc.getNPCData().isTalkClose()) {
						// If the player is within 'seeing' range
						if (LocationUtils.checkLocation(npc.getLocation(),
								p.getLocation(), range)) {
							if (npc.getNPCData().isLookClose()) {
								NPCManager.facePlayer(npc, p);
							}
							cacheActions(p, npc, UID, name);
						} else {
							resetActions(UID, name, npc);
						}
					}
				}
			}
		}
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "saidText", npc.getNPCData()
				.isTalkClose());
	}

	private void cacheActions(Player p, HumanNPC npc, int entityID, String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("saidText") && npc.getNPCData().isTalkClose()) {
			MessageUtils.sendText(npc, p, plugin);
			cached.set("saidText");
		}
		ActionManager.putAction(entityID, name, cached);
	}
}