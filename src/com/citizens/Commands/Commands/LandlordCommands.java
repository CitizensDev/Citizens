package com.citizens.commands.commands;

import org.bukkit.entity.Player;

import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.utils.HelpUtils;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "landlord")
public class LandlordCommands {

	@CommandRequirements()
	@Command(
			aliases = "landlord",
			usage = "help",
			desc = "view the landlord help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.landlord")
	public static void sendLandlordHelp(CommandContext args, Player player,
			HumanNPC npc) {
		HelpUtils.sendLandlordHelp(player);
	}
}