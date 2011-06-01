package com.fullwall.Citizens;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

public class Permission {
	@SuppressWarnings("unused")
	private static Permissions permissionsPlugin;
	private static boolean permissionsEnabled = false;

	public static void initialize(Server server) {
		Plugin test = server.getPluginManager().getPlugin("Permissions");
		if (test != null) {
			Logger log = Logger.getLogger("Minecraft");
			permissionsPlugin = (Permissions) test;
			permissionsEnabled = true;
			log.log(Level.INFO, "[Citizens]: Permissions enabled.");
		} else {
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.SEVERE,
					"[Citizens]: The Permissions plugin isn't loaded, commands can only be used by ops.");
		}
	}

	public static boolean isAdmin(Player player) {
		if (permissionsEnabled) {
			return permission(player, "citizens.admin");
		}
		return player.isOp();
	}

	public static boolean canCreate(Player player, String type) {
		if (permissionsEnabled) {
			if (isAdmin(player)) {
				return true;
			}
			return permission(player, "citizens.create." + type);
		}
		return false;
	}

	public static boolean canModify(Player player, String type) {
		if (permissionsEnabled) {
			if (isAdmin(player)) {
				return true;
			}
			return permission(player, "citizens.modify." + type);
		}
		return false;
	}

	public static boolean canUse(Player player, String type) {
		if (permissionsEnabled) {
			if (isAdmin(player)) {
				return true;
			}
			return permission(player, "citizens.use." + type);
		}
		return false;
	}

	private static boolean permission(Player player, String string) {
		return Permissions.Security.has(player, string);
	}

	public static boolean generic(Player player, String string) {
		if (permissionsEnabled) {
			return permission(player, string);
		}
		return player.isOp();
	}

	public static void grantRank(Player player, String rank) {
		if (permissionsEnabled) {
		}
	}
}