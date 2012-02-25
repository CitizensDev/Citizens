package net.citizensnpcs.permissions;

import java.util.ArrayList;
import java.util.HashSet;
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

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;


public class PermissionManager {
	private static Permission provider;
	private static boolean permissionsEnabled;
	private static final List<String> permissions = new ArrayList<String>();

	public PermissionManager(PluginManager pm) {
        provider = Citizens.setupPermissions();
        if (provider == null) {
            permissionsEnabled = false;
            return;
        }
        permissionsEnabled = true;
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
				|| getMaxNPCs(player) > UtilityProperties.getNPCCount(player
						.getName());
	}

	private static int getMaxNPCs(Player player) {
		for (int x = 100; x > 0; --x) {
			if (hasPermission(player, "citizens.npccount." + x)) {
				return x;
			}
		}
		return 0;
	}

	public static void grantRank(Player player, String rank, boolean take) {
		if (permissionsEnabled) {
            if (take) {
                provider.playerRemoveGroup(player, rank);
            } else {
                provider.playerAddGroup(player, rank);
            }
        }
	}

	public static void setRank(Player player, String rank) {
		if (permissionsEnabled) {
            provider.playerAddGroup(player, rank);
        }
	}

	public static void removeRank(Player player, String rank) {
		if (permissionsEnabled) {
            provider.playerRemove(player, rank);
        }
	}

	public static boolean hasRank(Player player, String rank) {
        return permissionsEnabled && provider.playerInGroup(player, rank);
    }

	public static void givePermission(Player player, String reward, boolean take) {
		if (permissionsEnabled) {
            if (take) {
                provider.playerRemove(player, reward);
            } else {
                provider.playerAdd(player, reward);
            }
        }
	}

	public static CitizensGroup getGroup(String name) {
        World world = Bukkit.getServer().getWorlds().get(0);
        return permissionsEnabled ? new CitizensGroup(provider.getPrimaryGroup(world, name)) : null;
    }

	public static Set<CitizensGroup> getGroups(Player player) {
        Set<CitizensGroup> groups = new HashSet<CitizensGroup>();
        for (String group : provider.getPlayerGroups(player)) {
            groups.add(new CitizensGroup(group));
        }
        return permissionsEnabled ? groups : null;
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
	}
}