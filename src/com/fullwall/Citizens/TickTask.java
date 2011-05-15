package com.fullwall.Citizens;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TickTask implements Runnable {

	private Citizens plugin;
	// How far an NPC can 'see'
	private double range;
	// Key: UID Value: a hash map of player names: whether text has been said to
	// them or not.
	private HashMap<Integer, HashMap<String, Boolean>> hasSaidText = new HashMap<Integer, HashMap<String, Boolean>>();

	public TickTask(Citizens plugin, double range) {
		this.plugin = plugin;
		// range is checked in both directions, so we halve the passed range.
		this.range = range / 2;
	}

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList()
				.entrySet()) {
			{
				HumanNPC npc = entry.getValue();
				npc.updateMovement();
				int entityID = entry.getKey();
				// Moves the NPC towards the player
				// (WALKING&Jumping)
				// entry.getValue().setTarget(p);
				for (Player p : plugin.getServer().getOnlinePlayers()) {
					String name = p.getName();// Moves the NPC towards the
												// player
					// (WALKING&Jumping)
					// entry.getValue().setTarget(p);
					if (npc.getNPCData().isLookClose()
							|| npc.getNPCData().isTalkClose()) {
						// If the player is within 'seeing' range
						if (checkLocation(npc.getLocation(), p.getLocation())) {
							// If auto-rotate is true, rotate
							if (npc.getNPCData().isLookClose()) {
								NPCManager.rotateNPCToPlayer(npc, p);
							}
							// If auto-talk is true
							if (npc.getNPCData().isTalkClose()
									// If we haven't already spoken to the
									// player.
									&& (hasSaidText.get(entityID) == null || (hasSaidText
											.get(entityID).get(name) == null || hasSaidText
											.get(entityID).get(name) == false))) {
								MessageUtils.sendText(npc, p, plugin);
								// Add the players to the sent text list.
								HashMap<String, Boolean> players = new HashMap<String, Boolean>();
								if (hasSaidText.get(entityID) != null)
									players = hasSaidText.get(entityID);
								players.put(name, true);
								hasSaidText.put(entityID, players);
							}
						}// We're out of range, so if player has been talked
							// to,
							// reset its talked-to state.
						else if (npc.getNPCData().isTalkClose()
								&& hasSaidText.get(entityID) != null
								&& hasSaidText.get(entityID).get(name) != null
								&& hasSaidText.get(entityID).get(name) == true) {
							hasSaidText.get(entityID).put(name, false);
						}
					}
				}
			}
		}
	}

	// Checks whether two locations are within seeing distance of each other.
	private boolean checkLocation(Location loc, Location playerLocation) {
		double pX = playerLocation.getX();
		double pY = playerLocation.getY();
		double pZ = playerLocation.getZ();
		double lX = loc.getX();
		double lY = loc.getY();
		double lZ = loc.getZ();
		if ((pX <= lX + range && pX >= lX - range)
				&& (pY >= lY - range && pY <= lY + range)
				&& (pZ >= lZ - range && pZ <= lZ + range))
			return true;
		else
			return false;
	}
}
