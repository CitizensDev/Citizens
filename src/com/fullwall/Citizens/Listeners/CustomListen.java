package com.fullwall.Citizens.Listeners;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;

public class CustomListen extends CustomEventListener implements Listener {
	private Citizens plugin;
	private PluginManager pm;

	public CustomListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.CUSTOM_EVENT, this, Event.Priority.Normal,
				plugin);
	}

	@Override
	public void onCustomEvent(Event ev) {
		if (!ev.getType().equals(Type.CUSTOM_EVENT)) {
			return;
		}
		if (ev.getEventName().equals("CitizensBasicNPCEvent")) {
			CitizensBasicNPCEvent e = (CitizensBasicNPCEvent) ev;
			if (e.isCancelled() == true) {
				return;
			}
			if (!PropertyManager.getBasic().getLookWhenClose(
					e.getNPC().getUID())) {
				NPCManager.facePlayer(e.getNPC(), e.getPlayer());
			}
			if (!e.getText().isEmpty()) {
				e.getPlayer().sendMessage(e.getText());
			}
		}
	}
}