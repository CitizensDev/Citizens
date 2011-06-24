package com.fullwall.Citizens.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.CreatureTask;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCTypes.Guards.GuardTask;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.ChatManager;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;

public class PlayerListen extends PlayerListener implements Listener {

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_MOVE, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, this,
				Event.Priority.Normal, Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_CHAT, this, Event.Priority.Normal,
				Citizens.plugin);
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		QuestManager.load(event.getPlayer());
		GuardTask.checkRespawn(event.getPlayer());
		CreatureTask.setDirty();
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		QuestManager.unload(event.getPlayer());
		CreatureTask.setDirty();
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		String name = event.getPlayer().getName();
		if (!ChatManager.hasEditMode(name)) {
			return;
		}
		// TODO do stuff
	}
}