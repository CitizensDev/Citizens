package net.citizensnpcs.waypoints.modifiers;

import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.utils.ConversationUtils.ConversationMessage;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointModifier;
import net.citizensnpcs.waypoints.WaypointModifierType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DelayModifier extends WaypointModifier {
	private int delay;

	public DelayModifier(Waypoint waypoint) {
		super(waypoint);
	}

	@Override
	public boolean allowExit() {
		return step > 0;
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
	public WaypointModifierType getType() {
		return WaypointModifierType.DELAY;
	}

	@Override
	public void load(DataKey root) {
	}

	@Override
	protected void onExit() {
		waypoint.setDelay(this.delay);
	}

	@Override
	public void onReach(HumanNPC npc) {
	}

	@Override
	public void save(DataKey root) {
	}
}