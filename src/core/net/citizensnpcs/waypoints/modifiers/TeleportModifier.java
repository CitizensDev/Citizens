package net.citizensnpcs.waypoints.modifiers;

import net.citizensnpcs.misc.ConstructableLocation;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.ConversationUtils.ConversationMessage;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointModifier;
import net.citizensnpcs.waypoints.WaypointModifierType;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportModifier extends WaypointModifier {
	private Location loc;
	private final ConstructableLocation construct = new ConstructableLocation();
	private static final int X = 0, Y = 1, Z = 2, PITCH = 3, YAW = 4;

	public TeleportModifier(Waypoint waypoint) {
		super(waypoint);
	}

	@Override
	public void begin(Player player) {
		player.sendMessage(ChatColor.GREEN + "Type " + StringUtils.wrap("undo")
				+ " at any time to go back one step, or "
				+ StringUtils.wrap("current")
				+ " to use your current location.");
		player.sendMessage(ChatColor.GREEN
				+ "Enter the x coordinate of the place to teleport to.");
	}

	@Override
	public boolean converse(Player player, ConversationMessage message) {
		super.resetExit();
		if (message.getMessage().equalsIgnoreCase("current")) {
			Location pLoc = player.getLocation();
			if (pLoc.getWorld() != waypoint.getLocation().getWorld()) {
				player.sendMessage(ChatColor.GRAY
						+ "You must be in the same world as the waypoint.");
				return false;
			}
			construct.setValues(pLoc);
			step = YAW;
			player.sendMessage(endMessage);
			return false;
		}
		double value = message.getDouble(0);
		switch (step) {
		case X:
			construct.setX(value);
			step = Y;
			player.sendMessage(getMessage("x", value));
			player.sendMessage(ChatColor.GREEN + "Enter the y coordinate.");
			break;
		case Y:
			construct.setY(value);
			step = Z;
			player.sendMessage(getMessage("y", value));
			player.sendMessage(ChatColor.GREEN + "Enter the z coordinate.");
			break;
		case Z:
			construct.setZ(value);
			step = PITCH;
			player.sendMessage(getMessage("z", value));
			player.sendMessage(ChatColor.GREEN + "Enter the pitch, or type "
					+ StringUtils.wrap("finish") + " to end.");
			break;
		case PITCH:
			construct.setPitch((float) value);
			step = YAW;
			player.sendMessage(getMessage("pitch", value));
			player.sendMessage(ChatColor.GREEN + "Enter the yaw, or type "
					+ StringUtils.wrap("finish") + " to end.");
			break;
		case YAW:
			construct.setYaw((float) value);
			player.sendMessage(getMessage("yaw", value));
		default:
			player.sendMessage(endMessage);
		}
		++step;
		return false;
	}

	@Override
	public boolean allowExit() {
		return step >= PITCH;
	}

	@Override
	public void onExit() {
		loc = construct.construct();
		waypoint.addModifier(this);
	}

	@Override
	public void onReach(HumanNPC npc) {
		npc.teleport(loc);
	}

	@Override
	public WaypointModifierType getType() {
		return WaypointModifierType.TELEPORT;
	}

	@Override
	public void parse(Storage storage, String root) {
		loc = LocationUtils.loadLocation(storage, root, true);
	}

	@Override
	public void save(Storage storage, String root) {
		LocationUtils.saveLocation(storage, loc, root, true);
	}
}