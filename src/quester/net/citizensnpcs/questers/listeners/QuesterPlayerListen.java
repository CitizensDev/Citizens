package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.listeners.Listener;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.utils.ConversationUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class QuesterPlayerListen extends PlayerListener implements Listener {

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, this, Event.Priority.Normal,
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
	public void onPlayerLogin(PlayerLoginEvent event) {
		QuestManager.load(event.getPlayer());
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		QuestManager.unload(event.getPlayer());
		ConversationUtils.verify();
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
		ConversationUtils.onChat(event);
	}
}