package com.Citizens.resources.sk89q.commands;

import org.bukkit.entity.Player;

import com.Citizens.Permission;
import com.Citizens.resources.redecouverte.NPClib.HumanNPC;

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
		return false;
	}
}