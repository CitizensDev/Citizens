package com.fullwall.Citizens;

import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
						}
					} else if (ActionManager.actions.get(UID) != null
							&& ActionManager.actions.get(UID).get(name) != null) {
						resetActions(UID, name, npc);
					}
				}
			}
		}
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "saidText", npc.getNPCData()
				.isTalkClose());
		ActionManager.resetAction(entityID, name, "takenItem", npc.isBandit());
	}

	private void cacheActions(Player p, HumanNPC npc, int entityID, String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("saidText") && npc.getNPCData().isTalkClose()) {
			MessageUtils.sendText(npc, p, plugin);
			cached.set("saidText");
		}
		if (!cached.has("takenItem") && npc.isBandit()) {
			removeRandomItem(p, npc);
			cached.set("takenItem");
		}
		ActionManager.putAction(entityID, name, cached);
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