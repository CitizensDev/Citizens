package com.citizens.Resources.sk89q;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.Resources.NPClib.HumanNPC;

public class CitizensCommandsManager<T extends Player> extends
		CommandsManager<T> {

	@Override
	public boolean hasPermission(T player, HumanNPC npc, String perm) {
		if (perm.equalsIgnoreCase("admin")) {
			return Permission.isAdmin(player);
		}
		if (perm.contains("use.")) {
			return Permission.canUse(player, npc, perm.replace("use.", ""));
		}
		if (perm.contains("modify.")) {
			return Permission.canModify(player, npc,
					perm.replace("modify.", ""));
		}
		if (perm.contains("create.")) {
			return Permission.canCreate(player, perm.replace("create.", ""));
		}
		if (perm.equals("any")) {
			return true;
		}
		return false;
	}

	public String[] getAllCommandModifiers(String command) {
		Set<String> cmds = new HashSet<String>();
		for (Map<CommandIdentifier, Method> enclosing : super.commands.values()) {
			for (CommandIdentifier identifier : enclosing.keySet()) {
				if (identifier.getCommand().equals(command)) {
					cmds.add(identifier.getModifier());
				}
			}
		}
		return cmds.toArray(new String[cmds.size()]);
	}
}