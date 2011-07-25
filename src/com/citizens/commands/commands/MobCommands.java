package com.citizens.commands.commands;

import org.bukkit.entity.Player;

import com.citizens.commands.CommandHandler;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;

public class MobCommands implements CommandHandler {
	@CommandRequirements()
	@Command(
			aliases = "npcspawn",
			usage = "npcspawn [type]",
			desc = "spawn a mob npc",
			modifiers = "",
			min = 1,
			max = 1)
	@CommandPermissions("creature.spawn")
	public static void spawn(CommandContext args, Player player, HumanNPC npc) {

	}
}