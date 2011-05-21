package com.fullwall.Citizens.Utils;

import org.bukkit.Location;

public class LocationUtils {

	/**
	 * Checks whether two locations are within a certain distance of each other.
	 * 
	 * @param loc
	 * @param playerLocation
	 * @param range
	 * @return
	 */
	public static boolean checkLocation(Location loc, Location playerLocation,
			double range) {
		double pX = playerLocation.getX();
		double pY = playerLocation.getY();
		double pZ = playerLocation.getZ();
		double lX = loc.getX();
		double lY = loc.getY();
		double lZ = loc.getZ();
		if ((pX <= lX + range && pX >= lX - range)
				&& (pY >= lY - range && pY <= lY + range)
				&& (pZ >= lZ - range && pZ <= lZ + range)) {
			return true;
		} else {
			return false;
		}
	}
}
