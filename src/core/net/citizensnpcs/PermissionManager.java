package net.citizensnpcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.commands.BasicCommands;
import net.citizensnpcs.commands.ToggleCommands;
import net.citizensnpcs.commands.WaypointCommands;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class PermissionManager {
	private static boolean permissionsEnabled = false;
	private static PermissionsPlugin superperms;

	private static List<String> permissions = new ArrayList<String>();

	private static boolean hasPermission(Player player, String string) {
		return player.hasPermission(string);
	}

	public static void initialize(Server server) {
		Plugin test = server.getPluginManager().getPlugin("PermissionsBukkit");
		if (test != null) {
			permissionsEnabled = true;
			superperms = (PermissionsPlugin) test;
			addPermissions();
			Messaging.log("Hooked into PermissionsBukkit.");
		} else {
			Messaging
					.log("PermissionsBukkit isn't loaded, some features are unavailable.");
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

	public static void grantRank(Player player, String rank, boolean take) {
		if (permissionsEnabled
				&& superperms.getPlayerInfo(player.getName()) != null
				&& superperms.getGroup(rank) != null) {
			if (take) {
				superperms.getPlayerInfo(player.getName()).getGroups()
						.remove(superperms.getGroup(rank));
			} else {
				superperms.getPlayerInfo(player.getName()).getGroups()
						.add(superperms.getGroup(rank));
			}
		}
	}

	public static boolean hasRank(Player player, String rank) {
		if (permissionsEnabled && superperms.getGroup(rank) != null
				&& superperms.getPlayerInfo(player.getName()) != null) {
			return superperms.getPlayerInfo(player.getName()).getGroups()
					.contains(superperms.getGroup(rank));
		}
		return false;
	}

	public static void givePermission(Player player, String reward, boolean take) {
		if (permissionsEnabled) {
			Map<String, Boolean> permissions = superperms.getPlayerInfo(
					player.getName()).getPermissions();
			if (take) {
				permissions.remove(reward);
			} else {
				permissions.put(reward, true);
			}
		}
	}

	public static Group getGroup(String name) {
		return permissionsEnabled ? superperms.getGroup(name) : null;
	}

	public static List<Group> getGroups(Player player) {
		if (permissionsEnabled
				&& superperms.getPlayerInfo(player.getName()) != null) {
			return superperms.getPlayerInfo(player.getName()).getGroups();
		}
		return null;
	}

	// TODO: is this needed?
	public static void addPermission(String perm) {
		permissions.add(perm);
	}

	private static void addPermissions() {
		new BasicCommands().addPermissions();
		new ToggleCommands().addPermissions();
		new WaypointCommands().addPermissions();
		addPermission("citizens.evils.immune");
		for (String loaded : Citizens.loadedTypes) {
			CitizensNPCType type = NPCTypeManager.getType(loaded);
			if (type != null) {
				type.getCommands().addPermissions();
			}
		}
		// TODO: investigate whether this is needed.
		for (String permission : permissions) {
			Bukkit.getServer().getPluginManager()
					.addPermission(new Permission("citizens." + permission));
		}
	}

	public static boolean superPermsEnabled() {
		return superperms != null;
	}
}