package net.citizensnpcs.permissions;

import java.util.Collection;
import java.util.Set;

import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

public class CitizensGroup {
	private final String name;
	private final Set<String> members = Sets.newHashSet();

	public CitizensGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Set<String> getMembers() {
		return this.members;
	}

	public void addMember(String member) {
		this.members.add(member);
	}

	public void addMember(Player player) {
		this.members.add(player.getName());
	}

	public void addAllMembers(Collection<String> other) {
		this.members.addAll(other);
	}
}
