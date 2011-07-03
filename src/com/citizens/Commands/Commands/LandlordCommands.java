package com.citizens.Commands.Commands;

import org.bukkit.entity.Player;

import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Resources.sk89q.Command;
import com.citizens.Resources.sk89q.CommandContext;
import com.citizens.Resources.sk89q.CommandPermissions;
import com.citizens.Resources.sk89q.CommandRequirements;
import com.citizens.Utils.HelpUtils;

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