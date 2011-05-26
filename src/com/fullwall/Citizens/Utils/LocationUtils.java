package com.fullwall.Citizens.Utils;

import org.bukkit.Location;

public class LocationUtils {

	/**
	 * Checks whether two locations are within a certain distance of each other.
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
		double pX = pLoc.getX(), pY = pLoc.getY(), pZ = pLoc.getZ();
		double lX = loc.getX(), lY = loc.getY(), lZ = loc.getZ();
		if ((pX <= lX + range && pX >= lX - range)
				&& (pY >= lY - range && pY <= lY + range)
				&& (pZ >= lZ - range && pZ <= lZ + range)) {
			return true;
		} else {
			return false;
		}
	}
}