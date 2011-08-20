package net.citizensnpcs.guards.listeners;

import net.citizensnpcs.guards.GuardTask;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class GuardPlayerListen extends PlayerListener {

	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		GuardTask.checkRespawn(event.getPlayer());
	}
}