package com.fullwall.Citizens;

import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	private HashMap<String, HashMap<Integer, Boolean>> hasTakenItem = new HashMap<String, HashMap<Integer, Boolean>>();

	public TickTask(Citizens plugin, double range) {
		this.plugin = plugin;
		// range is checked in both directions, so we halve the passed range.
		this.range = range / 2;
	}

	@Override
	public void run() {
		HumanNPC npc;
		int entityID;
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList()
				.entrySet()) {
			{
				npc = entry.getValue();
				npc.updateMovement();
				entityID = entry.getKey();
				// Moves the NPC towards the player
				// (WALKING&Jumping)
				// entry.getValue().setTarget(p);
				for (Player p : plugin.getServer().getOnlinePlayers()) {
					String name = p.getName();
					// Moves the NPC towards the player
					// (WALKING&Jumping)
					// entry.getValue().setTarget(p);
					if (npc.getNPCData().isLookClose()
							|| npc.getNPCData().isTalkClose()) {
						// If the player is within 'seeing' range
						if (checkLocation(npc.getLocation(), p.getLocation(),
								range)) {
							if (npc.getNPCData().isLookClose()) {
								NPCManager.facePlayer(npc, p);
							}
							if (npc.getNPCData().isTalkClose()
									// If we haven't already spoken to the
									// player.
									&& (hasSaidText.get(entityID) == null || (hasSaidText
											.get(entityID).get(name) == null || hasSaidText
											.get(entityID).get(name) == false))) {
								MessageUtils.sendText(npc, p, plugin);
								HashMap<String, Boolean> players = new HashMap<String, Boolean>();
								if (hasSaidText.get(entityID) != null) {
									players = hasSaidText.get(entityID);
								}
								players.put(name, true);
								hasSaidText.put(entityID, players);
							}
							if (hasTakenItem.get(name) == null
									|| hasTakenItem.get(name).get(npc.getUID()) == null
									|| hasTakenItem.get(name).get(npc.getUID()) == false) {
								removeRandomItem(p, npc);
								HashMap<Integer, Boolean> npcs = new HashMap<Integer, Boolean>();
								if (hasTakenItem.get(name) != null) {
									npcs = hasTakenItem.get(name);
								}
								npcs.put(npc.getUID(), true);
								hasTakenItem.put(name, npcs);
							}
						}
						// We're out of range -> reset talked-to state.
						else if (npc.getNPCData().isTalkClose()
								&& hasSaidText.get(entityID) != null
								&& hasSaidText.get(entityID).get(name) != null
								&& hasSaidText.get(entityID).get(name) == true) {
							hasSaidText.get(entityID).put(name, false);
						}
						// Player is not within range of bandit, reset
						// taken-item state
						else if (npc.isBandit()
								&& hasTakenItem.get(name) != null
								&& hasTakenItem.get(name).get(npc.getUID()) != null
								&& hasTakenItem.get(name).get(npc.getUID()) == true) {
							hasTakenItem.get(name).put(npc.getUID(), false);
						}
					}
				}
			}
		}
	}

	/**
	 * Checks whether two locations are within a certain distance of each other.
	 * 
	 * @param loc
	 * @param playerLocation
	 * @param range
	 * @return
	 */
	private boolean checkLocation(Location loc, Location playerLocation,
			double range) {
		double pX = playerLocation.getX();
		double pY = playerLocation.getY();
		double pZ = playerLocation.getZ();
		double lX = loc.getX();
		double lY = loc.getY();
		double lZ = loc.getZ();
		this.range = range;
		if ((pX <= lX + range && pX >= lX - range)
				&& (pY >= lY - range && pY <= lY + range)
				&& (pZ >= lZ - range && pZ <= lZ + range)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clears a player's inventory
	 * 
	 * @param player
	 */
	private void removeRandomItem(Player player, HumanNPC npc) {
		Random random = new Random();
		int randomSlot;
		int count = 0;
		ItemStack item = null;
		if (npc.isBandit()) {
			if (!NPCManager.validateOwnership(player, npc.getUID())) {
				int limit = player.getInventory().getSize();
				while (true) {
					randomSlot = random.nextInt(limit);
					item = player.getInventory().getItem(randomSlot);
					if (item != null) {
						player.getInventory().removeItem(item);
						player.sendMessage(ChatColor.RED
								+ npc.getStrippedName()
								+ " has stolen from your inventory!");
						break;
					} else {
						if (count >= limit) {
							break;
						}
						count += 1;
					}
				}
			}
		}
	}
}