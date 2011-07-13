package com.citizens;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.Messaging;
import com.nijiko.permissions.Group;
import com.nijiko.permissions.PermissionHandler;
import com.nijiko.permissions.User;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Permission {
	private static boolean permissionsEnabled = false;
	private static PermissionHandler handler;

	public static void initialize(Server server) {
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
			return isAdmin(player)
					|| permission(player, "citizens.create." + type);
		}
		return player.isOp();
	}

	public static boolean canModify(Player player, HumanNPC npc, String type) {
		if (permissionsEnabled) {
			return (isAdmin(player))
					|| (npc != null && NPCManager.validateOwnership(player,
							npc.getUID(), true))
					|| permission(player, "citizens.modify." + type);
		}
		return player.isOp();
	}

	public static boolean canUse(Player player, HumanNPC npc, String type) {
		if (permissionsEnabled) {
			return (isAdmin(player))
					|| (npc != null && NPCManager.validateOwnership(player,
							npc.getUID(), true))
					|| permission(player, "citizens.use." + type);
		}
		return player.isOp();
	}

	private static boolean permission(Player player, String string) {
		return handler.has(player, string);
	}

	public static boolean generic(Player player, String string) {
		if (permissionsEnabled) {
			return permission(player, string);
		}
		return player.isOp();
	}

	public static void grantRank(Player player, String rank) {
		if (permissionsEnabled) {
			User user = handler.getUserObject(player.getWorld().getName(),
					player.getName());
			if (user == null) {
				return;
			}
			Group group = handler.getGroupObject(player.getWorld().getName(),
					rank);
			if (group == null) {
				return;
			}
			user.addParent(group);
		}
	}

	public static void givePermission(Player player, String reward, boolean take) {
		if (permissionsEnabled) {
			User user = handler.getUserObject(player.getWorld().getName(),
					player.getName());
			if (user == null) {
				return;
			}
			if (take) {
				user.removePermission(reward);
			} else {
				user.addPermission(reward);
			}
		}
	}
}