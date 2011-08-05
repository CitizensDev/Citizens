package net.citizensnpcs.npctypes.guards;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.listeners.Listener;

public class GuardPlayerListen extends PlayerListener implements Listener {

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, this, Event.Priority.Normal,
				Citizens.plugin);
	}

	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		GuardTask.checkRespawn(event.getPlayer());
	}
}