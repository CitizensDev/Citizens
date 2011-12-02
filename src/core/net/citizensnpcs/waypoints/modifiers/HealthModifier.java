package net.citizensnpcs.waypoints.modifiers;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.utils.ConversationUtils.ConversationMessage;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointModifier;
import net.citizensnpcs.waypoints.WaypointModifierType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HealthModifier extends WaypointModifier {
	private boolean take;
	private int amount;
	private static final int AMOUNT = 0, TAKE = 1;

	public HealthModifier(Waypoint waypoint) {
		super(waypoint);
	}

	@Override
	public void onReach(HumanNPC npc) {
		int health = npc.getPlayer().getHealth();
		health = take ? health - amount : health + amount;
		if (health > 20) {
			health = 20;
		} else if (health < 0) {
			health = 0;
		}
		npc.getPlayer().setHealth(health);
	}

	@Override
	public void parse(Storage storage, String root) {
		take = storage.getBoolean(root + ".take");
		amount = storage.getInt(root = ".amount");
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setBoolean(root + ".take", take);
		storage.setInt(root + ".amount", amount);
	}

	@Override
	public WaypointModifierType getType() {
		return WaypointModifierType.HEALTH;
	}

	@Override
	public void begin(Player player) {
		player.sendMessage(ChatColor.GREEN
				+ "Enter the amount of health to change.");

	}

	@Override
	public boolean converse(Player player, ConversationMessage message) {
		super.resetExit();
		switch (step) {
		case AMOUNT:
			amount = message.getInteger(0);
			player.sendMessage(getMessage("health amount", amount));
			player.sendMessage(ChatColor.GREEN
					+ "Enter in whether taking health is "
					+ StringUtils.wrap("on") + "or " + StringUtils.wrap("off")
					+ ".");
			break;
		case TAKE:
			String bool = message.getString(0);
			if (bool.equals("off")) {
				take = false;
			} else {
				take = true;
			}
			player.sendMessage(ChatColor.GREEN
					+ (take ? "Taking health." : "Not taking health."));
		default:
			player.sendMessage(endMessage);
		}
		++step;
		return false;
	}

	@Override
	public boolean allowExit() {
		return false;
	}

	@Override
	public void onExit() {
		waypoint.addModifier(this);
	}
}