package com.fullwall.Citizens.Listeners;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager;

public class PlayerListen extends PlayerListener implements Listener {
	private Citizens plugin;
	private PluginManager pm;

	public PlayerListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, this,
				Event.Priority.Normal, plugin);
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (QuestManager.hasQuest(event.getPlayer()))
			QuestManager.incrementQuest(event.getPlayer(), event);
	}
}
