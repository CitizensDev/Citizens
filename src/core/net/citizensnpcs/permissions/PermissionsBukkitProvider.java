package net.citizensnpcs.permissions;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class PermissionsBukkitProvider implements PermissionsProvider {
	private final PermissionsPlugin provider;

	public PermissionsBukkitProvider(PermissionsPlugin plugin) {
		this.provider = plugin;
	}

	@Override
	public boolean inGroup(Player player, String group) {
		if (provider.getGroup(group) == null
				|| provider.getPlayerInfo(player.getName()) == null)
			return false;
		return provider.getPlayerInfo(player.getName()).getGroups()
				.contains(provider.getGroup(group));
	}

	@Override
	public void grantGroup(Player player, String group, boolean take) {
		if (provider.getGroup(group) == null)
			return;
		String modifier = take ? "removegroup" : "addgroup";
		Bukkit.getServer().dispatchCommand(
				new ConsoleCommandSender(Bukkit.getServer()),
				"perm player " + modifier + " " + player.getName() + " "
						+ group);
	}

	@Override
	public void grantPermission(Player player, String perm, boolean take) {
		Bukkit.getServer().dispatchCommand(
				new ConsoleCommandSender(Bukkit.getServer()),
				"perm player setperm " + player.getName() + " " + perm + " "
						+ take);
	}

	@Override
	public void setGroup(Player player, String group) {
		Bukkit.getServer().dispatchCommand(
				new ConsoleCommandSender(Bukkit.getServer()),
				"perm player setgroup " + player.getName() + " " + group);
	}

	@Override
	public CitizensGroup getGroup(String group) {
		return provider.getGroup(group) == null ? null : new CitizensGroup(
				provider.getGroup(group).getName());
	}

	@Override
	public Set<CitizensGroup> getGroups(Player player) {
		if (provider.getPlayerInfo(player.getName()) != null) {
			Set<CitizensGroup> groups = Sets.newHashSet();
			for (com.platymuus.bukkit.permissions.Group group : provider
					.getPlayerInfo(player.getName()).getGroups()) {
				CitizensGroup temp = new CitizensGroup(group.getName());
				temp.addAllMembers(group.getPlayers());
				groups.add(temp);
			}
			return groups;
		}
		return null;
	}
}
