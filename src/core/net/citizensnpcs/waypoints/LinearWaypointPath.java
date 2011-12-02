package net.citizensnpcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Settings;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LinearWaypointPath implements WaypointPath0 {
	private final HumanNPC npc;
	private final List<Waypoint> points = new ArrayList<Waypoint>();
	private int index = 0;

	public LinearWaypointPath(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void addWaypoint(Location location) {
		points.add(new Waypoint(location));
	}

	@Override
	public void clear() {
		points.clear();
		index = 0;
	}

	@Override
	public PathEditor newEditorSession(Player player) {
		return new LinearPathEditor(player);
	}

	@Override
	public void startPath() {
		if (!rangeCheck())
			return;
	}

	@Override
	public void restartPath() {
		index = 0;
		npc.teleport(points.get(0).getLocation());
		npc.getHandle().cancelPath();
	}

	private boolean rangeCheck() {
		if (index > points.size())
			index = points.size();
		if (0 > index)
			index = 0;
		return points.size() > 0;
	}

	private class LinearPathEditor implements PathEditor {
		private final Player player;
		private int index = Math.max(0, points.size() - 1);

		private LinearPathEditor(Player player) {
			this.player = player;
		}

		@Override
		public void onLeftClick(Block clicked) {
			Location loc = clicked.getLocation();
			if (!npc.getWorld().equals(player.getWorld())) {
				player.sendMessage(ChatColor.GRAY
						+ "Waypoints must be in the same world as the NPC.");
				return;
			}
			if (index - 1 > points.size()
					&& points.get(index - 1).getLocation().distance(loc) > Settings
							.getDouble("PathfindingRange")) {
				player.sendMessage(ChatColor.GRAY
						+ "Points can't be more than "
						+ StringUtils.wrap(
								Settings.getDouble("PathfindingRange"),
								ChatColor.GRAY)
						+ " blocks away from each other.");
				return;
			}
			player.sendMessage(StringUtils.wrap("Added")
					+ " waypoint at index " + StringUtils.wrap(index) + " ("
					+ StringUtils.wrap(loc.getBlockX()) + ", "
					+ StringUtils.wrap(loc.getBlockY()) + ", "
					+ StringUtils.wrap(loc.getBlockZ()) + ") ("
					+ StringUtils.wrap(points.size()) + " "
					+ StringUtils.pluralise("waypoint", points.size()) + ")");
			points.add(index++, new Waypoint(clicked.getLocation()));
		}

		@Override
		public void onRightClick(Block clicked) {
			if (index > points.size()) {
				points.remove(index--);
				player.sendMessage(StringUtils.wrap("Undid")
						+ " the last waypoint ("
						+ StringUtils.wrap(points.size()) + " remaining)");
				if (index == -1 && points.size() > 0)
					++index;
			} else
				player.sendMessage(ChatColor.GRAY + "No more waypoints.");
		}

		@Override
		public void setIndex(int index) {
			this.index = index;
		}

		@Override
		public void addModifier(WaypointModifier modifier) {
			points.get(index).addModifier(modifier);
		}

		@Override
		public void start() {
			player.sendMessage(ChatColor.AQUA
					+ StringUtils.listify("Waypoint Editing Controls"));
			player.sendMessage(StringUtils.wrap("Left")
					+ " click adds a waypoint, while "
					+ StringUtils.wrap("right") + " click acts as an undo.");
			player.sendMessage(StringUtils.wrap("Right clicking")
					+ " the NPC will cause him to restart from the current index.");
			player.sendMessage(StringUtils.wrap("Repeat")
					+ " this command to finish.");
		}

		@Override
		public void end() {
			player.sendMessage(StringUtils.wrap("Finished")
					+ " editing waypoints.");
		}
	}
}
