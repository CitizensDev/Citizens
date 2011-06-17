package com.fullwall.Citizens;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.nijiko.permissions.Group;
import com.nijiko.permissions.User;
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
					"[Citizens]: A Permissions plugin isn't loaded, commands can only be used by ops.");
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
            return isAdmin(player) || permission(player, "citizens.create." + type);
        }
		return player.isOp();
	}

	public static boolean canModify(Player player, HumanNPC npc, String type) {
		if (permissionsEnabled) {
            return (isAdmin(player)) || (npc != null && NPCManager.validateOwnership(player, npc.getUID())) || permission(player, "citizens.modify." + type);
        }
		return player.isOp();
	}

	public static boolean canUse(Player player, HumanNPC npc, String type) {
		if (permissionsEnabled) {
            return (isAdmin(player)) || (npc != null && NPCManager.validateOwnership(player, npc.getUID())) || permission(player, "citizens.use." + type);
        }
		return player.isOp();
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
			User user = Permissions.Security.getUserObject(player.getWorld()
					.getName(), player.getName());
			if (user == null) {
				return;
			}
			Group group = Permissions.Security.getGroupObject(player.getWorld()
					.getName(), rank);
			if (group == null) {
				return;
			}
			user.addParent(group);
		}
	}

	public static void givePermission(Player player, String reward, boolean take) {
		if (permissionsEnabled) {
			User user = Permissions.Security.getUserObject(player.getWorld()
					.getName(), player.getName());
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