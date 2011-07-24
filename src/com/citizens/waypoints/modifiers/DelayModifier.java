package com.citizens.waypoints.modifiers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.interfaces.Storage;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.ConversationUtils.ConversationMessage;
import com.citizens.waypoints.Waypoint;
import com.citizens.waypoints.WaypointModifier;
import com.citizens.waypoints.WaypointModifierType;

public class DelayModifier extends WaypointModifier {
	private int delay;

	public DelayModifier(Waypoint waypoint) {
		super(waypoint);
	}

	@Override
	public void onReach(HumanNPC npc) {
	}

	@Override
	public void parse(Storage storage, String root) {
	}

	@Override
	public void save(Storage storage, String root) {
	}

	@Override
	public WaypointModifierType getType() {
		return WaypointModifierType.DELAY;
	}

	@Override
	public void begin(Player player) {
		player.sendMessage(ChatColor.GREEN + "Enter the delay in seconds.");
	}

	@Override
	public boolean converse(Player player, ConversationMessage message) {
		switch (step) {
		case 0:
			int temp = message.getInteger(0) * 20;
			if (temp <= -1) {
				player.sendMessage(ChatColor.GRAY + "That delay is too small.");
			}
			delay = temp;
			player.sendMessage(super.getMessage("delay", delay / 20));
			++step;
		default:
			player.sendMessage(endMessage);
			return false;
		}
	}

	@Override
	public boolean allowExit() {
		return step > 0;
	}

	@Override
	protected void onExit() {
		waypoint.setDelay(this.delay);
	}
}
