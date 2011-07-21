package com.citizens;

import java.util.Map;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.citizens.SettingsManager.Constant;
import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;
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

	private static boolean permission(Player player, String string) {
		return useSuperperms ? player.hasPermission(string) : handler.has(
				player, string);
	}

	public static void initialize(Server server) {
		if (Constant.UseSuperPerms.toBoolean()) {
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

	public static boolean isAdmin(Player player) {
		if (permissionsEnabled) {
			return permission(player, "citizens.admin");
		}
		return player.isOp();
	}

	public static boolean canCreate(Player player, String type) {
		if (permissionsEnabled) {
			String permission = useSuperperms ? "citizens." + type + ".create"
					: "citizens.create." + type;
			return isAdmin(player) || permission(player, permission);
		}
		return player.isOp();
	}

	public static boolean canModify(Player player, HumanNPC npc, String type) {
		if (permissionsEnabled) {
			String permission = useSuperperms ? "citizens." + type + ".modify"
					: "citizens.modify." + type;
			return (isAdmin(player))
					|| (npc != null && NPCManager.validateOwnership(player,
							npc.getUID(), true))
					|| permission(player, permission);
		}
		return player.isOp();
	}

	public static boolean canUse(Player player, HumanNPC npc, String type) {
		if (permissionsEnabled) {
			String permission = useSuperperms ? "citizens." + type + ".use"
					: "citizens.use." + type;
			return (isAdmin(player))
					|| (npc != null && NPCManager.validateOwnership(player,
							npc.getUID(), true))
					|| permission(player, permission);
		}
		return player.isOp();
	}

	public static boolean generic(Player player, String string) {
		if (permissionsEnabled) {
			return permission(player, string);
		}
		return player.isOp();
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
}