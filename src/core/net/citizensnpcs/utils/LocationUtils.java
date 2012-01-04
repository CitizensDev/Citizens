package net.citizensnpcs.utils;

import net.citizensnpcs.properties.DataKey;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.base.Joiner;

public class LocationUtils {

	public static Location loadLocation(DataKey root, boolean shortened) {
		String world;
		double x, y, z;
		float pitch, yaw;
		if (shortened) {
			String[] read = root.getString("location").split(",");
			world = read[0];
			x = Double.parseDouble(read[1]);
			y = Double.parseDouble(read[2]);
			z = Double.parseDouble(read[3]);
			pitch = Float.parseFloat(read[4]);
			yaw = Float.parseFloat(read[5]);
		} else {
			root = root.getRelative("location");
			world = root.getString("world");
			x = root.getDouble("x");
			y = root.getDouble("y");
			z = root.getDouble("z");
			pitch = (float) root.getDouble("pitch");
			yaw = (float) root.getDouble("yaw");
		}
		return new Location(Bukkit.getServer().getWorld(world), x, y, z, pitch,
				yaw);
	}

	public static void saveLocation(DataKey root, Location loc,
			boolean shortened) {
		if (shortened) {
			root.setString(
					"location",
					Joiner.on(",").join(
							new Object[] { loc.getWorld().getName(),
									loc.getX(), loc.getY(), loc.getZ(),
									loc.getPitch(), loc.getYaw() }));
		} else {
			root = root.getRelative("location");
			root.setString("world", loc.getWorld().getName());
			root.setDouble("x", loc.getX());
			root.setDouble("y", loc.getY());
			root.setDouble("z", loc.getZ());
			root.setDouble("pitch", loc.getPitch());
			root.setDouble("yaw", loc.getYaw());
		}
	}

	/**
	 * Checks whether two locations are within range of each other.
	 * 
	 * @param loc
	 * @param pLoc
	 * @param range
	 * @return
	 */
	public static boolean withinRange(Location loc, Location pLoc, double range) {
		if (loc == null || pLoc == null || loc.getWorld() != pLoc.getWorld()) {
			return false;
		}
		return Math.pow(range, 2) > loc.distanceSquared(pLoc);
	}
}