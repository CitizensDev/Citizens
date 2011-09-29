package net.citizensnpcs.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.commands.BasicCommands;
import net.citizensnpcs.commands.ToggleCommands;
import net.citizensnpcs.commands.WaypointCommands;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.utils.Messaging;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class PermissionManager {
	private static PermissionsProvider provider = null;
	private static boolean permissionsEnabled;
	private static List<String> permissions = new ArrayList<String>();

	public PermissionManager(PluginManager pm) {
		String permPlugin = "";
		if (pm.getPlugin("PermissionsBukkit") != null) {
			permPlugin = "PermissionsBukkit";
			provider = new PermissionsBukkitProvider(
					(PermissionsPlugin) pm.getPlugin("PermissionsBukkit"));
		} else if (pm.getPlugin("PermissionsEx") != null) {
			permPlugin = "PermissionsEx";
			provider = new PermissionsExProvider();
		} else if (pm.getPlugin("bPermissions") != null) {
			permPlugin = "bPermissions";
			provider = new BPermissionsProvider();
		} else if (pm.getPlugin("GroupManager") != null) {
			permPlugin = "GroupManager";
			provider = new GroupManagerProvider(
					(GroupManager) pm.getPlugin("GroupManager"));
		} else {
			return;
		}
		permissionsEnabled = true;
		Messaging.log("Permissions system found (" + permPlugin + " v"
				+ pm.getPlugin(permPlugin).getDescription().getVersion() + ")");
		addPermissions();
	}

	public static boolean hasBackend() {
		return permissionsEnabled;
	}

	public static boolean hasPermission(Player player, String string) {
		return player.hasPermission(string);
	}

	public static boolean canCreate(Player player) {
		return hasPermission(player, "citizens.admin")
				|| hasPermission(player, "citizens.npccount.unlimited")
				|| UtilityProperties.getNPCCount(player.getName()) < getMaxNPCs(player);
	}

	private static int getMaxNPCs(Player player) {
		for (int x = 100; x > 0; --x) {
			if (hasPermission(player, "citizens.npccount." + x)) {
				return x;
			}
		}
		return 0;
	}

	public static void grantRank(Player player, String rank, boolean replace,
			boolean take) {
		if (permissionsEnabled) {
			if (replace)
				provider.setGroup(player, rank);
			else
				provider.grantGroup(player, rank, take);
		}
	}

	public static boolean hasRank(Player player, String rank) {
		return permissionsEnabled && provider.inGroup(player, rank);
	}

	public static void givePermission(Player player, String reward, boolean take) {
		if (permissionsEnabled) {
			provider.grantPermission(player, reward, take);
		}
	}

	public static CitizensGroup getGroup(String name) {
		return permissionsEnabled ? provider.getGroup(name) : null;
	}

	public static Set<CitizensGroup> getGroups(Player player) {
		return permissionsEnabled ? provider.getGroups(player) : null;
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
}