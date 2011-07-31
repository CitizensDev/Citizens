package com.citizens.npctypes.pirates;

import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.SettingsManager.Constant;
import com.citizens.misc.ActionManager;
import com.citizens.misc.ActionManager.CachedAction;
import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.LocationUtils;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.StringUtils;
import com.citizens.utils.Messaging;

public class PirateTask implements Runnable {
	public enum LootType {
		INVENTORY, CHEST, ECONOMY_PLUGIN;
	}

	private final Random random = new Random();

	@Override
	public void run() {
		HumanNPC npc;
		int UID;
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			{
				npc = entry.getValue();
				if (npc.isType("pirate")) {
					npc.doTick();
					UID = entry.getKey();
					for (Player p : online) {
						String name = p.getName();
						if (LocationUtils.withinRange(npc.getLocation(),
								p.getLocation(), 0)) {
							cachePlayerActions(p, npc, UID, name);
						} else {
							resetActions(UID, name, npc);
						}
					}
					for (BlockState block : npc.getLocation().getBlock()
							.getChunk().getTileEntities()) {
						if (block.getType() == Material.CHEST
								&& LocationUtils.withinRange(npc.getLocation(),
										block.getBlock().getLocation(), 10)) {

						}
					}
				}
			}
		}
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "takenItem",
				npc.isType("pirate"));
		ActionManager.resetAction(entityID, name, "robbedAccount",
				npc.isType("pirate"));
	}

	private void cachePlayerActions(Player player, HumanNPC npc, int entityID,
			String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		int rand = random.nextInt(1);
		if (rand == 0) {
			if (!cached.has("takenItem")) {
				steal(player, npc, LootType.INVENTORY);
				cached.set("takenItem");
			}
		} else {
			if (!cached.has("robbedAccount")) {
				steal(player, npc, LootType.ECONOMY_PLUGIN);
				cached.set("robbedAccount");
			}
		}
		ActionManager.putAction(entityID, name, cached);
	}

	/**
	 * Steal something from a player's inventory, economy-plugin account, or
	 * chest
	 * 
	 * @param player
	 * @param npc
	 * @param action
	 */
	private void steal(Player player, HumanNPC npc, LootType action) {
		switch (action) {
		case INVENTORY:
			int randomSlot;
			int count = 0;
			ItemStack item = null;
			int limit = player.getInventory().getSize();
			while (true) {
				randomSlot = random.nextInt(limit);
				item = player.getInventory().getItem(randomSlot);
				if (item != null) {
					player.getInventory().setItem(randomSlot, null);
					Messaging
							.send(player,
									npc,
									StringUtils.colourise(Constant.ChatFormat
											.getString().replace("%name%",
													npc.getStrippedName()))
											+ ChatColor.WHITE
											+ MessageUtils
													.getRandomMessage(Constant.PirateStealMessages
															.getString()));
					// may want to check if this returns a non-empty
					// hashmap (pirate didn't have enough room).
					npc.getInventory().addItem(item);
					break;
				} else {
					if (count >= limit) {
						break;
					}
					count += 1;
				}
			}
			break;
		case CHEST:
			// TODO loot from chests
			break;
		case ECONOMY_PLUGIN:
			// TODO steal player's economy money
			break;
		}
	}
}