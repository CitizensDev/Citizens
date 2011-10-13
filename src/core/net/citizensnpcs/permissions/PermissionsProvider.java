package net.citizensnpcs.permissions;

import java.util.Set;

import org.bukkit.entity.Player;

public interface PermissionsProvider {
	public boolean inGroup(Player player, String group);

	public void grantGroup(Player player, String group, boolean take);

	public void grantPermission(Player player, String perm, boolean take);

	public void setGroup(Player player, String group);

	public void removeGroup(Player player, String group);

	public CitizensGroup getGroup(String group);

	public Set<CitizensGroup> getGroups(Player player);
}
