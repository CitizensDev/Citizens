package net.citizensnpcs.permissions;

import java.util.Set;

import org.bukkit.entity.Player;

public class GroupManagerProvider implements PermissionsProvider {

	@Override
	public boolean inGroup(Player player, String group) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void grantGroup(Player player, String group, boolean take) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grantPermission(Player player, String perm, boolean take) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGroup(Player player, String group) {
		// TODO Auto-generated method stub

	}

	@Override
	public CitizensGroup getGroup(String group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CitizensGroup> getGroups(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

}
