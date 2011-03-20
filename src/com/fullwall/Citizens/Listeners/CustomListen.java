package com.fullwall.Citizens.Listeners;

import org.bukkit.Location;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Utils.PropertyPool;

public class CustomListen extends CustomEventListener {
	private Citizens plugin;

	public CustomListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCustomEvent(Event ev) {
		if (!ev.getType().equals(Type.CUSTOM_EVENT)) {
			return;
		}

		if (!ev.getEventName().equals("CitizensBasicNPCEvent")) {
			return;
		}
		CitizensBasicNPCEvent e = (CitizensBasicNPCEvent) ev;
		if (e.isCancelled() == true)
			return;
		if (plugin.shouldShowText(e.getPlayer().getItemInHand().getTypeId()) == true) {
			if (!PropertyPool.getNPCLookWhenClose(e.getNPC().getUID())) {
				Location loc = e.getNPC().getBukkitEntity().getLocation();
				float yaw = e.getPlayer().getLocation().getYaw() % 360 - 180;
				e.getNPC().moveTo(loc.getX(), loc.getY(), loc.getZ(), yaw,
						loc.getPitch());
			}
			e.getPlayer().sendMessage(e.getText());
		}
	}
}
