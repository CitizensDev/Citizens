package com.citizens.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.npctypes.wizards.WizardManager.WizardMode;
import com.citizens.npctypes.wizards.WizardNPC;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.utils.HelpUtils;
import com.citizens.utils.StringUtils;

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
	@CommandPermissions("wizard.use.help")
	public static void wizardHelp(CommandContext args, Player player,
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
	@CommandPermissions("wizard.modify.mode")
	public static void mode(CommandContext args, Player player, HumanNPC npc) {
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
	@CommandPermissions("wizard.use.status")
	public static void status(CommandContext args, Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.YELLOW
				+ StringUtils.listify(ChatColor.GREEN + npc.getStrippedName()
						+ "'s Wizard Status" + ChatColor.YELLOW));
		WizardNPC wizard = npc.getToggleable("wizard");
		player.sendMessage(ChatColor.GREEN + "    Mode: "
				+ StringUtils.wrap(wizard.getMode()));
		String mana = "" + wizard.getMana();
		if (wizard.hasUnlimitedMana()) {
			mana = "unlimited";
		}
		player.sendMessage(ChatColor.GREEN + "    Mana: "
				+ StringUtils.wrap(mana));
	}

	@Command(
			aliases = "wizard",
			usage = "addloc [location]",
			desc = "add a location to a wizard",
			modifiers = "addloc",
			min = 2,
			max = 2)
	@CommandPermissions("wizard.modify.addloc")
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
	@CommandPermissions("wizard.modify.removeloc")
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
	@CommandPermissions("wizard.use.locations")
	public static void locations(CommandContext args, Player player,
			HumanNPC npc) {
		WizardNPC wizard = npc.getToggleable("wizard");
		if (wizard.getMode() == WizardMode.TELEPORT) {
			player.sendMessage(ChatColor.GREEN
					+ StringUtils.listify(StringUtils.wrap(npc
							.getStrippedName() + "'s Wizard Locations")));
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

	@Command(
			aliases = "wizard",
			usage = "unlimited",
			desc = "toggle a wizard's mana as unlimited",
			modifiers = { "unlimited", "unlim", "unl" },
			min = 1,
			max = 1)
	@CommandPermissions("wizard.modify.unlimited")
	public static void unlimited(CommandContext args, Player player,
			HumanNPC npc) {
		WizardNPC wizard = npc.getToggleable("wizard");
		wizard.setUnlimitedMana(!wizard.hasUnlimitedMana());
		if (wizard.hasUnlimitedMana()) {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " now has unlimited mana.");
		} else {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " no longer has unlimited mana.");
		}
	}
}