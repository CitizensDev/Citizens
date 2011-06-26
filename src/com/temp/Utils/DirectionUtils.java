package com.temp.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public abstract class DirectionUtils {
	public enum CompassDirection {
		NO_DIRECTION(-1), NORTH(0), NORTH_EAST(1), EAST(2), SOUTH_EAST(3), SOUTH(
				4), SOUTH_WEST(5), WEST(6), NORTH_WEST(7);
		private final int id;
		private static Map<Integer, CompassDirection> map;

		private CompassDirection(int id) {
			this.id = id;
			add(id, this);
		}

		private static void add(int type, CompassDirection name) {
			if (map == null) {
				map = new HashMap<Integer, CompassDirection>();
			}

			map.put(type, name);
		}

		public int getType() {
			return id;
		}

		public static CompassDirection fromId(final int type) {
			return map.get(type);
		}
	}

	private static boolean isFacingNorth(double degrees, double leeway) {
		return ((0 <= degrees) && (degrees < 45 + leeway))
				|| ((315 - leeway <= degrees) && (degrees <= 360));
	}

	private static boolean isFacingEast(double degrees, double leeway) {
		return (45 - leeway <= degrees) && (degrees < 135 + leeway);
	}

	private static boolean isFacingSouth(double degrees, double leeway) {
		return (135 - leeway <= degrees) && (degrees < 225 + leeway);
	}

	private static boolean isFacingWest(double degrees, double leeway) {
		return (225 - leeway <= degrees) && (degrees < 315 + leeway);
	}

	public static CompassDirection getDirectionFromRotation(double degrees) {
		while (degrees < 0D)
			degrees += 360D;
		while (degrees > 360D)
			degrees -= 360D;
		if (isFacingNorth(degrees, 0)) {
			return CompassDirection.NORTH;
		}
		if (isFacingEast(degrees, 0)) {
			return CompassDirection.EAST;
		}
		if (isFacingSouth(degrees, 0)) {
			return CompassDirection.SOUTH;
		}
		if (isFacingWest(degrees, 0)) {
			return CompassDirection.WEST;
		}
		return CompassDirection.NO_DIRECTION;
	}

	public static Block getBlockBehind(Location loc, CompassDirection facing) {
		World w = loc.getWorld();
		int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
		if (facing == CompassDirection.NORTH)
			return w.getBlockAt(x + 1, y, z);
		if (facing == CompassDirection.EAST)
			return w.getBlockAt(x, y, z + 1);
		if (facing == CompassDirection.SOUTH)
			return w.getBlockAt(x - 1, y, z);
		if (facing == CompassDirection.WEST)
			return w.getBlockAt(x, y, z - 1);
		return null;
	}
}
