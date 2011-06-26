package com.citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.NPCTypes.Wizards.WizardNPC;
import com.citizens.NPCTypes.Wizards.WizardManager.WizardMode;
import com.citizens.Utils.HelpUtils;
import com.citizens.Utils.StringUtils;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;
import com.citizens.resources.sk89q.commands.Command;
import com.citizens.resources.sk89q.commands.CommandContext;
import com.citizens.resources.sk89q.commands.CommandPermissions;
import com.citizens.resources.sk89q.commands.CommandRequirements;

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
			WizardNPC wizard = npc.getToggleable("wizard");
			if (wizardMode != wizard.getMode()) {
				wizard.setMode(wizardMode);
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
		WizardNPC wizard = npc.getToggleable("wizard");
		player.sendMessage(ChatColor.BLUE + "Mode: " + ChatColor.GOLD
				+ wizard.getMode());
		player.sendMessage(ChatColor.BLUE + "Mana: " + ChatColor.GOLD
				+ wizard.getMana());
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
		WizardNPC wizard = npc.getToggleable("wizard");
		if (wizard.getMode() == WizardMode.TELEPORT) {
			player.sendMessage(ChatColor.GREEN + "Added current location to "
					+ StringUtils.wrap(npc.getStrippedName()) + ChatColor.GREEN
					+ " as " + StringUtils.wrap(args.getString(1)) + ".");
			wizard.addLocation(player.getLocation(), args.getString(1));
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
		WizardNPC wizard = npc.getToggleable("wizard");
		if (wizard.getMode() == WizardMode.TELEPORT) {
			String locations[] = wizard.getLocations().split(":");
			String newLoc = "";
			String removedName = "";
			for (int i = 0; i < locations.length; i++) {
				if (i + 1 != Integer.parseInt(args.getString(1))) {
					newLoc = newLoc + locations[i];
				} else {
					removedName = locations[i].split(",")[0].replace("(", "");
				}
			}
			wizard.cycle(npc, WizardMode.TELEPORT);
			wizard.setLocations(newLoc);
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
		WizardNPC wizard = npc.getToggleable("wizard");
		if (wizard.getMode() == WizardMode.TELEPORT) {
			player.sendMessage(ChatColor.GREEN
					+ "========== "
					+ StringUtils.wrap(npc.getStrippedName()
							+ "'s Wizard Locations") + " ==========");
			String locations[] = wizard.getLocations().split(":");
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