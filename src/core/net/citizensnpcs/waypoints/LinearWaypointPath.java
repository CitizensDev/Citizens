package net.citizensnpcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.pathfinding.PathCallback;
import net.citizensnpcs.lib.pathfinding.PathController;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LinearWaypointPath implements WaypointPath {
	private final HumanNPC npc;
	private final List<Waypoint> points = new ArrayList<Waypoint>();
	private final LinearPathCallback callback = new LinearPathCallback();
	private int index = 0;

	public LinearWaypointPath(HumanNPC npc) {
		this.npc = npc;
		npc.getPathController().registerCallback(callback);
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
	public PathEditor createEditor(Player player) {
		return new LinearPathEditor(player);
	}

	@Override
	public void delay(int ticks) {
		if (callback.executing
				&& npc.getPathController().getCurrentPath() != null) {
			npc.getPathController().getCurrentPath().pause(ticks);
		}
	}

	@Override
	public void load(DataKey root) {
		points.clear();
		for (DataKey key : root.getIntegerSubKeys()) {
			Waypoint waypoint = new Waypoint(LocationUtils.loadLocation(key,
					true));
			waypoint.setDelay(key.getInt("delay", 0));

			if (key.keyExists("modifiers")) {
				key = key.getRelative("modifiers");
				for (DataKey innerKey : key.getIntegerSubKeys()) {
					WaypointModifier modifier = WaypointModifierType.valueOf(
							innerKey.getString("type")).create(waypoint);
					modifier.load(innerKey);
					waypoint.addModifier(modifier);
				}
			}
			if (waypoint != null)
				points.add(waypoint);
		}
	}

	private boolean rangeCheck() {
		if (index > points.size())
			index = points.size();
		if (0 > index)
			index = 0;
		return points.size() > 0;
	}

	@Override
	public void restart() {
		if (points.size() == 0)
			return;
		index = 0;
		npc.teleport(points.get(0).getLocation());
	}

	@Override
	public void save(DataKey root) {
		root.removeKey("waypoints"); // clear old waypoints.
		int i = 0;
		for (Waypoint waypoint : points) {
			DataKey key = root.getRelative("waypoints." + i);
			LocationUtils.saveLocation(key, waypoint.getLocation(), true);
			key.setInt("delay", waypoint.getDelay());
			key = key.getRelative("modifiers");

			int i2 = 0;
			for (WaypointModifier modifier : waypoint.getModifiers()) {
				DataKey inner = key.getRelative(Integer.toString(i2));
				inner.setString("type", modifier.getType().name());
				modifier.save(inner);
				++i2;
			}
			++i;
		}
	}

	private class LinearPathCallback extends PathCallback {
		private boolean executing = true;

		@Override
		public boolean onPathBegin(PathController controller) {
			return false;
		}

		@Override
		public boolean onPathCancel(PathController controller,
				PathCancelReason reason) {
			if (reason == PathCancelReason.REPLACE
					|| reason == PathCancelReason.PLUGIN)
				executing = !executing;
			return false;
		}

		@Override
		public boolean onPathCompletion(final PathController controller) {
			if (points.size() == 0)
				return false;
			if (!executing) {
				executing = true;
				npc.teleport(points.get(index).getLocation());
			} else {
				points.get(index++).onReach(npc);
			}
			if (points.size() == 0 && index == 1) {
				controller.pathTo(npc.getNPCData().getLocation());
				index = 0;
			} else {
				rangeCheck();
				Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
						new Runnable() {
							@Override
							public void run() {
								controller.pathTo(points.get(index)
										.getLocation());
							}
						}, points.get(index).getDelay());
			}
			return false;
		}
	}

	private class LinearPathEditor implements PathEditor {
		private final Player player;
		private int index = Math.max(0, points.size() - 1);

		private LinearPathEditor(Player player) {
			this.player = player;
		}

		@Override
		public Waypoint getWaypointForModifier() {
			return points.get(index);
		}

		@Override
		public void end() {
			player.sendMessage(StringUtils.wrap("Finished")
					+ " editing waypoints.");
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
	}
}
