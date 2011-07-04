package com.citizens.Utils;

import org.bukkit.Location;

public class LocationUtils {

	/**
	 * Checks whether two locations are within range of each other.
	 * 
	 * @param loc
	 * @param pLoc
	 * @param range
	 * @return
	 */
	public static boolean checkLocation(Location loc, Location pLoc,
			double range) {
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
	public static boolean checkLocation(Location loc, Location pLoc, int range) {
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
}