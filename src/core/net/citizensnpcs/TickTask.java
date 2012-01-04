package net.citizensnpcs;

import java.util.Map;

import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.lib.NPCSpawner;
import net.citizensnpcs.misc.Actions;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

public class TickTask implements Runnable {
	@Override
	public void run() {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (HumanNPC npc : NPCManager.getNPCs()) {
			npc.doTick();
			NPCSpawner.removeNPCFromPlayerList(npc);
			if (!npc.getNPCData().isLookClose()
					&& !npc.getNPCData().isTalkClose())
				continue;
			for (Player player : online) {
				// If the player is within 'seeing' range
				if (LocationUtils.withinRange(npc.getLocation(),
						player.getLocation(), Settings.getDouble("NPCRange"))) {
					if (!npc.getPathController().isPathing()
							&& npc.getNPCData().isLookClose()) {
						NPCManager.faceEntity(npc.getPlayer(), player);
					}
					cacheActions(npc, player);
				} else {
					clearActions(npc, player);
				}
			}
		}
	}

	public static class RespawnTask implements Runnable {
		private final HumanNPC npc;
		private final String owner;

		public RespawnTask(HumanNPC npc) {
			this.npc = npc;
			this.owner = npc.getOwner();
		}

		public void register(int delay) {
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(Citizens.plugin, this, delay);
		}

		@Override
		public void run() {
			npc.spawn();
			Messaging.sendUncertain(owner, StringUtils.wrap(npc.getName())
					+ " has respawned.");
		}
	}

	private static final Map<HumanNPC, Actions> cachedActions = Maps
			.newHashMap();

	private static void cacheActions(HumanNPC npc, Player player) {
		Actions actions = cachedActions.get(npc);
		if (actions == null) {
			cachedActions.put(npc, new Actions());
			return;
		}
		if (!actions.has("saidText", player.getName())
				&& npc.getNPCData().isTalkClose()) {
			MessageUtils.sendText(npc, player);
			actions.set("saidText", player.getName());
		}
	}

	private static void clearActions(HumanNPC npc, Player player) {
		Actions actions = cachedActions.get(npc);
		if (actions == null) {
			cachedActions.put(npc, new Actions());
			return;
		}
		actions.clear("saidText", player.getName());
	}

	public static void clearActions(Player player) {
		for (HumanNPC npc : cachedActions.keySet()) {
			clearActions(npc, player);
		}
	}

	public static void scheduleRespawn(HumanNPC npc, int delay) {
		npc.despawn();
		new RespawnTask(npc).register(delay);
	}
}