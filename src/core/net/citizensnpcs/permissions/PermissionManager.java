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
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.google.common.collect.Sets;

public class PermissionManager {
    public void init() {
        try {
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServicesManager().getRegistration(
                    Permission.class);
            if (permissionProvider != null) {
                provider = permissionProvider.getProvider();
                permissionsEnabled = true;
            }
        } catch (NoClassDefFoundError ex) {
        }
        addPermissions();
    }
    private static final List<String> permissions = new ArrayList<String>();
    private static boolean permissionsEnabled;

    private static Permission provider = null;

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
            Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission("citizens." + permission));
        }
    }

    public static boolean canCreate(Player player) {
        return hasPermission(player, "citizens.admin") || hasPermission(player, "citizens.npccount.unlimited")
                || getMaxNPCs(player) > UtilityProperties.getNPCCount(player.getName());
    }

    public static Set<CitizensGroup> getGroups(Player player) {
        if (!permissionsEnabled)
            return null;
        Set<CitizensGroup> groups = Sets.newHashSet();
        for (String group : provider.getPlayerGroups(player)) {
            groups.add(new CitizensGroup(group));
        }
        return groups;
    }

    private static int getMaxNPCs(Player player) {
        for (int x = 100; x > 0; --x) {
            if (hasPermission(player, "citizens.npccount." + x)) {
                return x;
            }
        }
        return 0;
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

    public static void grantRank(Player player, String rank, boolean take) {
        if (permissionsEnabled) {
            if (take) {
                provider.playerRemoveGroup(player, rank);
            } else {
                provider.playerAddGroup(player, rank);
            }
        }
    }

    public static boolean groupExists(String name) {
        if (!permissionsEnabled)
            return false;
        for (String group : provider.getGroups()) {
            if (group.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public static boolean hasBackend() {
        return permissionsEnabled;
    }

    public static boolean hasPermission(Player player, String string) {
        return player.hasPermission(string);
    }

    public static boolean hasRank(Player player, String rank) {
        return permissionsEnabled && provider.playerInGroup(player, rank);
    }

    public static void removeRank(Player player, String rank) {
        if (permissionsEnabled) {
            provider.playerRemoveGroup(player, rank);
        }
    }

    public static void setRank(Player player, String rank) {
        if (permissionsEnabled) {
            for (String group : provider.getPlayerGroups(player)) {
                provider.playerRemoveGroup(player, group);
            }
            provider.playerAddGroup(player, rank);
        }
    }
}