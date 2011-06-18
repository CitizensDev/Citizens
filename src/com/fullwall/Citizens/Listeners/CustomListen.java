package com.fullwall.Citizens.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.Messaging;

public class CustomListen extends CustomEventListener implements Listener {

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.CUSTOM_EVENT, this, Event.Priority.Normal,
				Citizens.plugin);
	}

	@Override
	public void onCustomEvent(Event ev) {
		if (!ev.getType().equals(Type.CUSTOM_EVENT)) {
			return;
		}
		if (ev.getEventName().equals("CitizensBasicNPCEvent")) {
			CitizensBasicNPCEvent e = (CitizensBasicNPCEvent) ev;
			if (e.isCancelled()) {
				return;
			}
			if (!e.getNPC().getNPCData().isLookClose()) {
				NPCManager.facePlayer(e.getNPC(), e.getPlayer());
			}
			if (!e.getText().isEmpty()) {
				Messaging.send(e.getPlayer(), e.getText());
			}
		}
	}
}