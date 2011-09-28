package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.utils.ConversationUtils;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuesterPlayerListen extends PlayerListener {

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		QuestManager.unload(event.getPlayer());
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	@Override
	public void onPlayerDropItem(PlayerDropItemEvent event) {
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