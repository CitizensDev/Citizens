package com.fullwall.Citizens;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.PropertyPool;
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
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList()
					.entrySet()) {
				{
					// If the player is within 'seeing' range
					if (checkLocation(entry.getValue().getBukkitEntity()
							.getLocation(), p.getLocation())) {
						// If auto-rotate is true, rotate
						if (PropertyPool.getNPCLookWhenClose(entry.getValue()
								.getUID()) == true) {
							NPCManager.rotateNPCToPlayer(entry.getValue(), p);
						}
						// If auto-talk is true
						if (PropertyPool.getNPCTalkWhenClose(entry.getValue()
								.getUID())
						// If we haven't already spoken to the player.
								&& (!hasSaidText.containsKey(entry.getKey()) || (!hasSaidText
										.get(entry.getKey()).containsKey(
												p.getName()) || hasSaidText
										.get(entry.getKey()).get(p.getName()) == false))) {
							MessageUtils.sendText(entry.getValue(), p, plugin);
							// Add the players to the sent text list.
							HashMap<String, Boolean> players = new HashMap<String, Boolean>();
							if (hasSaidText.containsKey(entry.getKey()))
								players = hasSaidText.get(entry.getKey());
							players.put(p.getName(), true);
							hasSaidText.put(entry.getKey(), players);
						}
						// Moves the NPC towards the player (WALKING&Jumping)
						// entry.getValue().setTarget(p);

					} // We're out of range, so if player has been talked to,
						// reset its talked-to state.
					else if (PropertyPool.getNPCTalkWhenClose(entry.getValue()
							.getUID())
							&& hasSaidText.containsKey(entry.getKey())
							&& hasSaidText.get(entry.getKey()).containsKey(
									p.getName())
							&& hasSaidText.get(entry.getKey()).get(p.getName()) == true) {
						hasSaidText.get(entry.getKey()).put(p.getName(), false);
					}
				}
			}
		}
		// Obviously outside the player for loop, otherwise gravity would be
		// applied more then once per tick.
		for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList()
				.entrySet()) {
			HumanNPC NPC = entry.getValue();
			// This also updates gravity, don't remove.
			NPC.updateMovement();
		}
	}

	// Checks whether two locations are within seeing distance of each other.
	private boolean checkLocation(Location loc, Location playerLocation) {
		if ((playerLocation.getX() <= loc.getX() + range && playerLocation
				.getX() >= loc.getX() - range)
				&& (playerLocation.getY() >= loc.getY() - range && playerLocation
						.getY() <= loc.getY() + range)
				&& (playerLocation.getZ() >= loc.getZ() - range && playerLocation
						.getZ() <= loc.getZ() + range))
			return true;
		else
			return false;
	}
}
