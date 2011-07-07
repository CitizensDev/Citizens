package com.citizens.npctypes.pirates;

import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.Constants;
import com.citizens.misc.ActionManager;
import com.citizens.misc.CachedAction;
import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.LocationUtils;
import com.citizens.utils.MessageUtils;
import com.iConomy.util.Messaging;

public class PirateTask implements Runnable {
	@Override
	public void run() {
		HumanNPC npc;
		int UID;
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			{
				npc = entry.getValue();
				npc.doTick();
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
		ActionManager.resetAction(entityID, name, "takenItem",
				npc.isType("pirate"));
	}

	private void cacheActions(Player player, HumanNPC npc, int entityID,
			String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("takenItem") && npc.isType("pirate")) {
			steal(player, npc);
			cached.set("takenItem");
		}
		ActionManager.putAction(entityID, name, cached);
	}

	/**
	 * Steal something from a player's inventory, economy-plugin account, or
	 * chest
	 * 
	 * @param player
	 * @param npc
	 */
	private void steal(Player player, HumanNPC npc) {
		Random random = new Random();
		int randomSlot;
		int count = 0;
		ItemStack item = null;
		if (npc.isType("pirate")) {
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