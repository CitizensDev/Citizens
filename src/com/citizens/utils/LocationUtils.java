package com.citizens.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.citizens.interfaces.Storage;
import com.google.common.base.Joiner;

public class LocationUtils {

	/**
	 * Checks whether two locations are within range of each other.
	 * 
	 * @param loc
	 * @param pLoc
	 * @param range
	 * @return
	 */
	public static boolean withinRange(Location loc, Location pLoc, double range) {
		if (!loc.getWorld().getName().equals(pLoc.getWorld().getName())) {
			return false;
		}
		double halved = range / 2;
		double pX = pLoc.getX(), pY = pLoc.getY(), pZ = pLoc.getZ();
		double lX = loc.getX(), lY = loc.getY(), lZ = loc.getZ();
		return (pX <= lX + halved && pX >= lX - halved)
				&& (pY >= lY - range && pY <= lY + range)
				&& (pZ >= lZ - halved && pZ <= lZ + halved);
	}

	/**
	 * Checks whether two locations are within range of each other.
	 * 
	 * @param loc
	 * @param pLoc
	 * @param range
	 * @return
	 */
	public static boolean withinRange(Location loc, Location pLoc, int range) {
		if (!loc.getWorld().getName().equals(pLoc.getWorld().getName())) {
			return false;
		}
		int halved = range / 2;
		double pX = pLoc.getBlockX(), pY = pLoc.getBlockY(), pZ = pLoc
				.getBlockZ();
		double lX = loc.getBlockX(), lY = loc.getBlockY(), lZ = loc.getBlockZ();
		return (pX <= lX + halved && pX >= lX - halved)
				&& (pY >= lY - range && pY <= lY + range)
				&& (pZ >= lZ - halved && pZ <= lZ + halved);
	}

	public static boolean withinRange(Location loc, Location second) {
		return withinRange(loc, second, 0);
	}

	public static Location loadLocation(Storage storage, String path,
			boolean shortened) {
		String world;
		double x, y, z;
		float pitch, yaw;
		if (shortened) {
			String[] read = storage.getString(path + ".location").split(",");
			world = read[0];
			x = Double.parseDouble(read[1]);
			y = Double.parseDouble(read[2]);
			z = Double.parseDouble(read[3]);
			pitch = Float.parseFloat(read[4]);
			yaw = Float.parseFloat(read[5]);
		} else {
			world = storage.getString(path + ".world");
			x = storage.getDouble(path + ".x");
			y = storage.getDouble(path + ".y");
			z = storage.getDouble(path + ".z");
			pitch = (float) storage.getDouble(path + ".pitch");
			yaw = (float) storage.getDouble(path + ".yaw");
		}
		return new Location(Bukkit.getServer().getWorld(world), x, y, z, pitch,
				yaw);
	}

	public static void saveLocation(Storage storage, Location loc, String path,
			boolean shortened) {
		if (shortened) {
			storage.setString(
					path + ".location",
					Joiner.on(",").join(
							new Object[] { loc.getWorld().getName(),
									loc.getX(), loc.getY(), loc.getZ(),
									loc.getPitch(), loc.getYaw() }));
		} else {
			storage.setString(path + ".world", loc.getWorld().getName());
			storage.setDouble(path + ".x", loc.getX());
			storage.setDouble(path + ".y", loc.getY());
			storage.setDouble(path + ".z", loc.getZ());
			storage.setDouble(path + ".pitch", loc.getPitch());
			storage.setDouble(path + ".yaw", loc.getYaw());
		}
	}
}