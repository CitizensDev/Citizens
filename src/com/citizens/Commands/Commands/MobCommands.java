package com.citizens.Commands.Commands;

import org.bukkit.entity.Player;

import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Resources.sk89q.Command;
import com.citizens.Resources.sk89q.CommandContext;
import com.citizens.Resources.sk89q.CommandPermissions;
import com.citizens.Resources.sk89q.CommandRequirements;

public class MobCommands {
	@CommandRequirements()
	@Command(
			aliases = "npcspawn",
			usage = "npcspawn [type]",
			desc = "spawn a mob npc",
			modifiers = "",
			min = 1,
			max = 1)
	@CommandPermissions("admin")
	public static void mobCommands(CommandContext args, Player player,
			HumanNPC npc) {

	}
}
