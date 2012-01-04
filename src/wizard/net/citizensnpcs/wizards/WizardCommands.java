package net.citizensnpcs.wizards;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.sk89q.Command;
import net.citizensnpcs.sk89q.CommandContext;
import net.citizensnpcs.sk89q.CommandPermissions;
import net.citizensnpcs.sk89q.CommandRequirements;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.wizards.WizardManager.WizardMode;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "wizard")
public class WizardCommands extends CommandHandler {
	private WizardCommands() {
	}

	@Override
	public void addPermissions() {
		PermissionManager.addPermission("wizard.use.help");
		PermissionManager.addPermission("wizard.modify.mode");
		PermissionManager.addPermission("wizard.use.status");
		PermissionManager.addPermission("wizard.modify.addloc");
		PermissionManager.addPermission("wizard.modify.removeloc");
		PermissionManager.addPermission("wizard.use.locations");
		PermissionManager.addPermission("wizard.modify.unlimited");
		PermissionManager.addPermission("wizard.use.interact");
	}

	@Override
	public void sendHelpPage(CommandSender sender) {
		HelpUtils.header(sender, "Wizard", 1, 1);
		HelpUtils.format(sender, "wizard", "locations",
				"view the tp locations of a wizard");
		HelpUtils.format(sender, "wizard", "addloc [name]",
				"add a tp location to the wizard");
		HelpUtils.format(sender, "wizard", "removeloc [id]",
				"remove the tp location");
		HelpUtils.format(sender, "wizard", "mode [mode]",
				"change the mode of a wizard");
		HelpUtils.format(sender, "wizard", "status",
				"display the status of a wizard");
		HelpUtils.format(sender, "wizard", "unlimited",
				"toggle a wizard's mana as unlimited");
		HelpUtils.footer(sender);
	}

	public static final WizardCommands INSTANCE = new WizardCommands();

	@Command(
			aliases = "wizard",
			usage = "addloc [location]",
			desc = "add a location to a wizard",
			modifiers = "addloc",
			min = 2)
	@CommandPermissions("wizard.modify.addloc")
	public static void addLocation(CommandContext args, Player player,
			HumanNPC npc) {
		Wizard wizard = npc.getType("wizard");
		if (wizard.getMode() != WizardMode.TELEPORT) {
			Messaging.sendError(player, npc.getName()
					+ " is not in teleport mode.");
			return;
		}
		player.sendMessage(ChatColor.GREEN + "Added current location to "
				+ StringUtils.wrap(npc.getName()) + " as "
				+ StringUtils.wrap(args.getJoinedStrings(1)) + ".");
		wizard.addLocation(player.getLocation(), args.getJoinedStrings(1));
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
		Wizard wizard = npc.getType("wizard");
		if (wizard.getMode() != WizardMode.TELEPORT) {
			Messaging.sendError(player, npc.getName()
					+ " is not in teleport mode.");
			return;
		}
		player.sendMessage(ChatColor.GREEN
				+ StringUtils.listify(StringUtils.wrap(npc.getName()
						+ "'s Wizard Locations")));
		int i = 1;
		for (String location : wizard.getLocations()) {
			player.sendMessage(StringUtils.wrap(i) + ": "
					+ location.split(",")[0]);
			++i;
		}
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
		WizardMode wizardMode = WizardMode.parse(args.getString(1));
		if (wizardMode != null) {
			Wizard wizard = npc.getType("wizard");
			if (wizardMode != wizard.getMode()) {
				wizard.setMode(wizardMode);
				player.sendMessage(StringUtils.wrap(npc.getName() + "'s")
						+ " mode was set to "
						+ StringUtils
								.wrap(wizardMode.name().toLowerCase() + "")
						+ ".");
			} else {
				player.sendMessage(ChatColor.RED + npc.getName()
						+ " is already that mode.");
			}
		} else {
			player.sendMessage(ChatColor.RED
					+ "That is not a valid wizard mode.");
		}
	}

	@Command(
			aliases = "wizard",
			usage = "removeloc [location]",
			desc = "remove a location from a wizard",
			modifiers = "removeloc",
			min = 2,
			max = -1)
	@CommandPermissions("wizard.modify.removeloc")
	public static void removeLocation(CommandContext args, Player player,
			HumanNPC npc) {
		Wizard wizard = npc.getType("wizard");
		if (wizard.getMode() != WizardMode.TELEPORT) {
			Messaging.sendError(player, npc.getName()
					+ " is not in teleport mode.");
			return;
		}
		boolean success = wizard.removeLocation(args.getJoinedStrings(1));
		wizard.cycle();
		player.sendMessage((success ? StringUtils.wrap(npc.getName())
				+ " has amnesia and forgot about "
				+ StringUtils.wrap(args.getString(1)) + "."
				: "No location by that name found."));
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
				+ StringUtils.listify(ChatColor.GREEN + npc.getName()
						+ "'s Wizard Status" + ChatColor.YELLOW));
		Wizard wizard = npc.getType("wizard");
		player.sendMessage(ChatColor.GREEN + "    Mode: "
				+ StringUtils.wrap(wizard.getMode().name().toLowerCase()));
		String mana = "" + wizard.getMana();
		if (wizard.hasUnlimitedMana()) {
			mana = "unlimited";
		}
		player.sendMessage(ChatColor.GREEN + "    Mana: "
				+ StringUtils.wrap(mana));
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
		Wizard wizard = npc.getType("wizard");
		wizard.setUnlimitedMana(!wizard.hasUnlimitedMana());
		if (wizard.hasUnlimitedMana()) {
			player.sendMessage(StringUtils.wrap(npc.getName())
					+ " now has unlimited mana.");
		} else {
			player.sendMessage(StringUtils.wrap(npc.getName())
					+ " no longer has unlimited mana.");
		}
	}

	@CommandRequirements()
	@Command(
			aliases = "wizard",
			usage = "help",
			desc = "view the wizard help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("wizard.use.help")
	public static void wizardHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		INSTANCE.sendHelpPage(sender);
	}
}