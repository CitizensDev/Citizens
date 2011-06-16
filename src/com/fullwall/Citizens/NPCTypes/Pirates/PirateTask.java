package com.fullwall.Citizens.NPCTypes.Pirates;

import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Misc.ActionManager;
import com.fullwall.Citizens.Misc.CachedAction;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.util.Messaging;

public class PirateTask implements Runnable {
	@SuppressWarnings("unused")
	private final Citizens plugin;

	public PirateTask(Citizens plugin) {
		this.plugin = plugin;
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
					if (LocationUtils.checkLocation(npc.getLocation(),
							p.getLocation(), 0)) {
						cacheActions(p, npc, UID, name);
					} else {
						resetActions(UID, name, npc);
					}
				}
			}
		}
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "takenItem", npc.isPirate());
	}

	private void cacheActions(Player p, HumanNPC npc, int entityID, String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("takenItem") && npc.isPirate()) {
			stealItem(p, npc);
			cached.set("takenItem");
		}
		ActionManager.putAction(entityID, name, cached);
	}

	/**
	 * Steal an item from a player's inventory and put it in a pirate's
	 * inventory
	 * 
	 * @param player
	 * @param npc
	 */
	private void stealItem(Player player, HumanNPC npc) {
		Random random = new Random();
		int randomSlot;
		int count = 0;
		ItemStack item = null;
		if (npc.isPirate()) {
			int limit = player.getInventory().getSize();
			while (true) {
				randomSlot = random.nextInt(limit);
				item = player.getInventory().getItem(randomSlot);
				if (item != null) {
					player.getInventory().setItem(randomSlot, null);
					Messaging
							.send(player,
									ChatColor.RED
											+ "["
											+ npc.getStrippedName()
											+ "] "
											+ ChatColor.WHITE
											+ MessageUtils
													.getRandomMessage(Constants.pirateStealMessages));
					// may want to check if this returns a non-empty
					// hashmap (bandit didn't have enough room).
					npc.getInventory().addItem(item);
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