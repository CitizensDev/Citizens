package net.citizensnpcs.guards;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.PathUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GuardManager {
	public enum GuardState {
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

		public static GuardState parse(String string) {
			try {
				return GuardState.valueOf(string.toUpperCase());
			} catch (Exception ex) {
				return NULL;
			}
		}
	}

	// Return a bouncer to its original position
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
			loc = npc.getWaypoints().size() > 0 ? npc.getWaypoints().current()
					.getLocation() : npc.getNPCData().getLocation();
		}
		if (npc.getLocation().distance(loc) > guard.getProtectionRadius()) {
			npc.teleport(loc);
			return;
		}
		guard.setReturning(true);
		PathUtils.createPath(npc, loc, -1, -1,
				SettingsManager.getDouble("PathfindingRange"));
	}
}