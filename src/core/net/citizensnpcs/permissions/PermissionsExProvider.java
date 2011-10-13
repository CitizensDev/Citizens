package net.citizensnpcs.permissions;

import java.util.Set;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.google.common.collect.Sets;

public class PermissionsExProvider implements PermissionsProvider {
	ru.tehkode.permissions.PermissionManager provider = PermissionsEx
			.getPermissionManager();

	@Override
	public boolean inGroup(Player player, String group) {
		if (provider.getUser(player) == null)
			return false;
		return provider.getUser(player).inGroup(group);
	}

	@Override
	public void grantGroup(Player player, String group, boolean take) {
		if (provider.getUser(player) == null)
			return;
		if (take)
			provider.getUser(player).removeGroup(group);
		else
			provider.getUser(player).addGroup(group);
	}

	@Override
	public void grantPermission(Player player, String perm, boolean take) {
		if (provider.getUser(player) == null)
			return;
		if (take)
			provider.getUser(player).removePermission(perm);
		else
			provider.getUser(player).addPermission(perm);
	}

	@Override
	public void setGroup(Player player, String group) {
		if (provider.getUser(player) == null)
			return;
		provider.getUser(player).setGroups(new String[] { group });
	}

	@Override
	public CitizensGroup getGroup(String group) {
		return provider.getGroup(group) == null ? null : new CitizensGroup(
				provider.getGroup(group).getName());
	}

	@Override
	public Set<CitizensGroup> getGroups(Player player) {
		if (provider.getUser(player) != null) {
			Set<CitizensGroup> groups = Sets.newHashSet();
			for (PermissionGroup group : provider.getUser(player).getGroups()) {
				CitizensGroup temp = new CitizensGroup(group.getName());
				for (PermissionUser user : group.getUsers()) {
					temp.addMember(user.getName());
				}
				groups.add(temp);
			}
			return groups;
		}
		return null;
	}

	@Override
	public void removeGroup(Player player, String group) {
		if (provider.getUser(player) != null
				&& provider.getGroup(group) != null) {
			provider.getUser(player).removeGroup(group);
		}
	}
}
