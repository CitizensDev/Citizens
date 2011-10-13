package net.citizensnpcs.permissions;

import java.util.Set;

import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

import de.bananaco.permissions.Permissions;
import de.bananaco.permissions.interfaces.PermissionSet;
import de.bananaco.permissions.worlds.WorldPermissionsManager;

public class BPermissionsProvider implements PermissionsProvider {
	WorldPermissionsManager provider = Permissions.getWorldPermissionsManager();

	private PermissionSet getPermissions(Player player) {
		return provider.getPermissionSet(player.getWorld().getName());
	}

	@Override
	public boolean inGroup(Player player, String group) {
		return getPermissions(player).getGroups(player).contains(group);
	}

	@Override
	public void grantGroup(Player player, String group, boolean take) {
		if (take)
			getPermissions(player).removeGroup(player, group);
		else
			getPermissions(player).addGroup(player, group);
	}

	@Override
	public void grantPermission(Player player, String perm, boolean take) {
		return; // bPermissions doesn't support node adding/removing ?
	}

	@Override
	public void setGroup(Player player, String group) {
		getPermissions(player).setGroup(player, group);
	}

	@Override
	public CitizensGroup getGroup(String group) {
		return null; // bPermissions API needs multiworld for some reason.
	}

	@Override
	public Set<CitizensGroup> getGroups(Player player) {
		Set<CitizensGroup> groups = Sets.newHashSet();
		for (String name : getPermissions(player).getGroups(player)) {
			groups.add(new CitizensGroup(name));
		}
		return groups;
	}

	@Override
	public void removeGroup(Player player, String group) {
		getPermissions(player).removeGroup(player, group);
	}
}
