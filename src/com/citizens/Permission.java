package com.citizens;

import java.util.List;
import java.util.Map;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.citizens.properties.SettingsManager;
import com.citizens.properties.properties.UtilityProperties;
import com.citizens.utils.Messaging;
import com.nijiko.permissions.Group;
import com.nijiko.permissions.PermissionHandler;
import com.nijiko.permissions.User;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class Permission {
	private static boolean useSuperperms;
	private static boolean permissionsEnabled = false;
	private static PermissionHandler handler;
	private static PermissionsPlugin superperms;

	private static boolean hasPermission(Player player, String string) {
		return useSuperperms ? player.hasPermission(string) : handler.has(
				player, string);
	}

	public static void initialize(Server server) {
		if (SettingsManager.getBoolean("general.use-bukkit-permissions")) {
			Plugin test = server.getPluginManager().getPlugin(
					"PermissionsBukkit");
			if (test != null) {
				useSuperperms = true;
				permissionsEnabled = true;
				superperms = (PermissionsPlugin) test;
				Messaging.log("Permissions enabled.");
			} else {
				Messaging
						.log("PermissionsBukkit isn't loaded, commands can only be used by ops.");
			}
			return;
		}
		Plugin test = server.getPluginManager().getPlugin("Permissions");
		if (test != null) {
			permissionsEnabled = true;
			handler = ((Permissions) test).getHandler();
			Messaging.log("Permissions enabled.");
		} else {
			Messaging
					.log("A Permissions plugin isn't loaded, commands can only be used by ops.");
		}
	}

	public static boolean generic(Player player, String string) {
		if (permissionsEnabled) {
			return hasPermission(player, string);
		}
		return player.isOp();
	}

	public static boolean canCreate(Player player) {
		return generic(player, "citizens.admin")
				|| generic(player, "citizens.npccount.unlimited")
				|| UtilityProperties.getNPCCount(player.getName()) < getMaxNPCs(player);
	}

	private static int getMaxNPCs(Player player) {
		for (int x = 1; x <= 100; x++) {
			if (hasPermission(player, "citizens.npccount." + x)) {
				return x;
			}
		}
		return 0;
	}

	public static void grantRank(Player player, String rank) {
		if (permissionsEnabled) {
			if (useSuperperms) {
				superperms.getPlayerInfo(player.getName()).getGroups()
						.add(superperms.getGroup(rank));
			} else {
				User user = handler.getUserObject(player.getWorld().getName(),
						player.getName());
				if (user == null) {
					return;
				}
				Group group = handler.getGroupObject(player.getWorld()
						.getName(), rank);
				if (group == null) {
					return;
				}
				user.addParent(group);
			}
		}
	}

	public static void givePermission(Player player, String reward, boolean take) {
		if (permissionsEnabled) {
			if (useSuperperms) {
				Map<String, Boolean> permissions = superperms.getPlayerInfo(
						player.getName()).getPermissions();
				if (take)
					permissions.remove(reward);
				else
					permissions.put(reward, true);
			} else {
				User user = handler.getUserObject(player.getWorld().getName(),
						player.getName());
				if (user == null) {
					return;
				}
				if (take)
					user.removePermission(reward);
				else
					user.addPermission(reward);
			}
		}
	}

	public static List<com.platymuus.bukkit.permissions.Group> getGroups(
			Player player) {
		if (permissionsEnabled) {
			if (useSuperperms) {
				return superperms.getPlayerInfo(player.getName()).getGroups();
			}
		}
		return null;
	}

	public static com.platymuus.bukkit.permissions.Group getGroup(String name) {
		if (permissionsEnabled) {
			if (useSuperperms) {
				return superperms.getGroup(name);
			}
		}
		return null;
	}

	public static boolean useSuperPerms() {
		return useSuperperms;
	}
}