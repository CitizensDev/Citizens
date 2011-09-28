package net.citizensnpcs.permissions;

import java.util.Set;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

public class GroupManagerProvider implements PermissionsProvider {
	private final WorldsHolder provider;

	public GroupManagerProvider(GroupManager provider) {
		this.provider = provider.getWorldsHolder();
	}

	@Override
	public boolean inGroup(Player player, String group) {
		if (provider.getWorldData(player) == null
				|| provider.getWorldData(player).getUser(player.getName()) == null)
			return false;
		OverloadedWorldHolder data = provider.getWorldData(player);
		if (data.getGroup(group) == null)
			return false;
		return provider.getWorldData(player).getUser(player.getName())
				.containsSubGroup(data.getGroup(group));
	}

	@Override
	public void grantGroup(Player player, String group, boolean take) {
		if (provider.getWorldData(player) == null
				|| provider.getWorldData(player).getUser(player.getName()) == null)
			return;
		OverloadedWorldHolder data = provider.getWorldData(player);
		if (data.getGroup(group) == null)
			return;
		if (take)
			data.getUser(player.getName()).removeSubGroup(data.getGroup(group));
		else
			data.getUser(player.getName()).addSubGroup(data.getGroup(group));
	}

	@Override
	public void grantPermission(Player player, String perm, boolean take) {
		if (provider.getWorldData(player) == null
				|| provider.getWorldData(player).getUser(player.getName()) == null)
			return;
		OverloadedWorldHolder data = provider.getWorldData(player);
		if (take)
			data.getUser(player.getName()).removePermission(perm);
		else
			data.getUser(player.getName()).addPermission(perm);
	}

	@Override
	public void setGroup(Player player, String group) {
		if (provider.getWorldData(player) == null
				|| provider.getWorldData(player).getUser(player.getName()) == null)
			return;
		OverloadedWorldHolder data = provider.getWorldData(player);
		if (data.getGroup(group) == null)
			return;
		data.getUser(player.getName()).setGroup(data.getGroup(group));
	}

	@Override
	public CitizensGroup getGroup(String group) {
		Group internalGroup = provider.getDefaultWorld().getGroup(group);
		if (internalGroup == null)
			return null;
		return new CitizensGroup(internalGroup.getName());
	}

	@Override
	public Set<CitizensGroup> getGroups(Player player) {
		if (provider.getWorldData(player) == null
				|| provider.getWorldData(player).getUser(player.getName()) == null)
			return null;
		Set<CitizensGroup> ret = Sets.newHashSet();
		User user = provider.getWorldData(player).getUser(player.getName());
		ret.add(new CitizensGroup(user.getGroupName()));
		for (String group : user.subGroupListStringCopy()) {
			ret.add(new CitizensGroup(group));
		}
		return ret;
	}
}
