package com.fullwall.Citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Wizards.WizardManager.WizardMode;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.sk89q.commands.Command;
import com.fullwall.resources.sk89q.commands.CommandContext;
import com.fullwall.resources.sk89q.commands.CommandPermissions;
import com.fullwall.resources.sk89q.commands.CommandRequirements;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "wizard")
public class WizardCommands {

	@CommandRequirements()
	@Command(
			aliases = "wizard",
			usage = "help",
			desc = "view the wizard help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.wizard")
	public static void sendWizardHelp(CommandContext args, Player player,
			HumanNPC npc) {
		HelpUtils.sendWizardHelp(player);
	}

	@Command(
			aliases = "wizard",
			usage = "mode [mode]",
			desc = "change the mode of a wizard",
			modifiers = "mode",
			min = 2,
			max = 2)
	@CommandPermissions("modify.wizard")
	public static void changeMode(CommandContext args, Player player,
			HumanNPC npc) {
		WizardMode wizardMode;
		if (WizardMode.parse(args.getString(1)) != null) {
			wizardMode = WizardMode.parse(args.getString(1));
			if (wizardMode != npc.getWizard().getMode()) {
				npc.getWizard().setMode(wizardMode);
				player.sendMessage(StringUtils.wrap(npc.getStrippedName()
						+ "'s")
						+ " mode was set to "
						+ StringUtils.wrap(wizardMode + "") + ".");
			} else {
				player.sendMessage(ChatColor.RED + npc.getStrippedName()
						+ " is already that mode.");
			}
		} else {
			player.sendMessage(ChatColor.RED
					+ "That is not a valid wizard mode.");
		}
	}

	@Command(
			aliases = "wizard",
			usage = "status",
			desc = "display the status of a wizard",
			modifiers = "status",
			min = 1,
			max = 1)
	@CommandPermissions("use.wizard")
	public static void displayStatus(CommandContext args, Player player,
			HumanNPC npc) {
		player.sendMessage(ChatColor.BLUE + "========== " + ChatColor.GOLD
				+ npc.getStrippedName() + "'s Wizard Status" + ChatColor.BLUE
				+ " ==========");
		player.sendMessage(ChatColor.BLUE + "Mode: " + ChatColor.GOLD
				+ npc.getWizard().getMode());
		player.sendMessage(ChatColor.BLUE + "Mana: " + ChatColor.GOLD
				+ npc.getWizard().getMana());
	}

	@Command(
			aliases = "wizard",
			usage = "addloc [location]",
			desc = "add a location to a wizard",
			modifiers = "addloc",
			min = 2,
			max = 2)
	@CommandPermissions("modify.wizard")
	public static void addLocation(CommandContext args, Player player,
			HumanNPC npc) {
		if (npc.getWizard().getMode() == WizardMode.TELEPORT) {
			player.sendMessage(ChatColor.GREEN + "Added current location to "
					+ StringUtils.wrap(npc.getStrippedName()) + ChatColor.GREEN
					+ " as " + StringUtils.wrap(args.getString(1)) + ".");
			npc.getWizard()
					.addLocation(player.getLocation(), args.getString(1));
		} else {
			player.sendMessage(ChatColor.RED + npc.getStrippedName()
					+ " cannot perform that action in this mode.");
		}
	}

	@Command(
			aliases = "wizard",
			usage = "removeloc [location]",
			desc = "remove a location from a wizard",
			modifiers = "removeloc",
			min = 2,
			max = 2)
	@CommandPermissions("modify.wizard")
	public static void removeLocation(CommandContext args, Player player,
			HumanNPC npc) {
		if (npc.getWizard().getMode() == WizardMode.TELEPORT) {
			String locations[] = npc.getWizard().getLocations().split(":");
			String newLoc = "";
			String removedName = "";
			for (int i = 0; i < locations.length; i++) {
				if (i + 1 != Integer.parseInt(args.getString(1))) {
					newLoc = newLoc + locations[i];
				} else {
					removedName = locations[i].split(",")[0].replace("(", "");
				}
			}
			npc.getWizard().cycle(npc, WizardMode.TELEPORT);
			npc.getWizard().setLocations(newLoc);
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has amnesia and forgot about "
					+ StringUtils.wrap(removedName) + ".");
		} else {
			player.sendMessage(ChatColor.RED + npc.getStrippedName()
					+ " cannot perform that action in this mode.");
		}
	}

	@Command(
			aliases = "wizard",
			usage = "locations",
			desc = "view the locations of a wizard",
			modifiers = "locations",
			min = 1,
			max = 1)
	@CommandPermissions("use.wizard")
	public static void displayLocations(CommandContext args, Player player,
			HumanNPC npc) {
		if (npc.getWizard().getMode() == WizardMode.TELEPORT) {
			player.sendMessage(ChatColor.GREEN
					+ "========== "
					+ StringUtils.wrap(npc.getStrippedName()
							+ "'s Wizard Locations") + " ==========");
			String locations[] = npc.getWizard().getLocations().split(":");
			for (int i = 0; i < locations.length; i++) {
				player.sendMessage(ChatColor.YELLOW + "" + (i + 1)
						+ ChatColor.GREEN + ": "
						+ locations[i].split(",")[0].replace("(", ""));
			}
		} else {
			player.sendMessage(ChatColor.RED + npc.getStrippedName()
					+ " cannot perform that action in this mode.");
		}
	}
}