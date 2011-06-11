package com.fullwall.Citizens.Listeners;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCTypes.Guards.GuardTask;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;

public class PlayerListen extends PlayerListener implements Listener {
	private final Citizens plugin;
	private PluginManager pm;

	public PlayerListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.PLAYER_MOVE, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, this,
				Event.Priority.Normal, plugin);
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		QuestManager.load(event.getPlayer());
		GuardTask.checkRespawn(event.getPlayer());
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		QuestManager.unload(event.getPlayer());
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (QuestManager.hasQuest(event.getPlayer())) {
			QuestManager.incrementQuest(event.getPlayer(), event);
		}
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		if (QuestManager.hasQuest(event.getPlayer())) {
			QuestManager.incrementQuest(event.getPlayer(), event);
		}
	}
}