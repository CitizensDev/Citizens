package com.citizens.npctypes.guards;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.citizens.SettingsManager.Constant;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.PathUtils;

public class GuardManager {
	public enum GuardType {
		/**
		 * Protects land
		 */
		BOUNCER,
		/**
		 * Protects and follows players
		 */
		BODYGUARD,
		/**
		 * Default guard type
		 */
		NULL;

		public static GuardType parse(String string) {
			try {
				return GuardType.valueOf(string.toUpperCase());
			} catch (Exception ex) {
				return NULL;
			}
		}
	}

	/**
	 * Return a bouncer to its original position
	 * 
	 * @param guard
	 * 
	 * @param npc
	 */
	public static void returnToBase(Guard guard, HumanNPC npc) {
		Location loc;
		if (guard.isBodyguard()) {
			Player owner = Bukkit.getServer().getPlayer(npc.getOwner());
			if (owner != null) {
				loc = owner.getLocation();
			} else {
				return;
			}
		} else {
			if (npc.getWaypoints().size() > 0) {
				loc = npc.getWaypoints().current().getLocation();
			} else {
				loc = npc.getNPCData().getLocation();
			}
		}
		PathUtils.createPath(npc, loc, -1, -1,
				Constant.PathfindingRange.toDouble());
	}
}