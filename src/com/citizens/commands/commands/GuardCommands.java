package com.citizens.commands.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.citizens.npctypes.guards.GuardManager;
import com.citizens.npctypes.guards.GuardNPC;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.resources.sk89q.ServerCommand;
import com.citizens.utils.HelpUtils;
import com.citizens.utils.Messaging;
import com.citizens.utils.StringUtils;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "guard")
public class GuardCommands {

	@CommandRequirements()
	@ServerCommand()
	@Command(
			aliases = "guard",
			usage = "help",
			desc = "view the guard help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.guard")
	public static void sendGuardHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		HelpUtils.sendGuardHelp(sender);
	}

	@Command(
			aliases = "guard",
			usage = "[type]",
			desc = "change a guard's type",
			modifiers = { "bodyguard", "bouncer" },
			min = 1,
			max = 1)
	@CommandPermissions("modify.guard")
	public static void changeType(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		if (args.getString(0).equalsIgnoreCase("bodyguard")) {
			if (!guard.isBodyguard()) {
				guard.setBodyguard();
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " is now a bodyguard.");
			} else {
				guard.clear();
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " has stopped being a bodyguard.");
			}
		} else if (args.getString(0).equalsIgnoreCase("bouncer")) {
			if (!guard.isBouncer()) {
				guard.setBouncer();
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " is now a bouncer.");
			} else {
				guard.clear();
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " has stopped being a bouncer.");
			}
		} else {
			Messaging.sendError(player, "That is not a valid guard type.");
		}
	}

	@Command(
			aliases = "guard",
			usage = "blacklist (add|remove) (mob)",
			desc = "control a guard's blacklist",
			modifiers = "blacklist",
			min = 1,
			max = 3)
	@CommandPermissions("modify.guard")
	public static void blacklist(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		switch (args.argsLength()) {
		case 2:
			player.sendMessage(ChatColor.GRAY
					+ "Insufficient or too many arguments.");
			break;
		case 1:
			player.sendMessage(ChatColor.GREEN
					+ StringUtils.listify(StringUtils.wrap(npc
							.getStrippedName() + "'s Blacklisted Mobs")));
			Set<String> list = guard.getBlacklist();
			if (list.isEmpty()) {
				player.sendMessage(ChatColor.RED + "No mobs blacklisted.");
			} else {
				for (String aList : list) {
					player.sendMessage(ChatColor.RED + aList);
				}
			}
			break;
		case 3:
			String mob = args.getString(2).toLowerCase();
			if (CreatureType.fromName(StringUtils.capitalise(mob)) == null
					&& !mob.equalsIgnoreCase("all")) {
				player.sendMessage(ChatColor.RED + "Invalid mob type.");
				return;
			}
			boolean add = false;
			if (args.getString(1).equalsIgnoreCase("add")) {
				add = true;
			}
			if (add) {
				if (guard.getBlacklist().contains(mob)) {
					player.sendMessage(ChatColor.RED
							+ "That mob is already blacklisted.");
					return;
				}
				GuardManager.addToBlacklist(guard, mob);
				player.sendMessage(ChatColor.GREEN + "Added "
						+ StringUtils.wrap(mob) + " to "
						+ StringUtils.wrap(npc.getStrippedName() + "'s")
						+ " blacklist.");
			} else {
				if (!guard.getBlacklist().contains(mob)) {
					player.sendMessage(ChatColor.RED
							+ "That mob is not blacklisted.");
					return;
				}
				GuardManager.removeFromBlacklist(guard, mob);
				player.sendMessage(ChatColor.GREEN + "Removed "
						+ StringUtils.wrap(mob) + " from "
						+ StringUtils.wrap(npc.getStrippedName() + "'s")
						+ " blacklist.");
			}
			break;
		}
	}

	@Command(
			aliases = "guard",
			usage = "whitelist (add|remove) (player)",
			desc = "control a guard's whitelist",
			modifiers = "whitelist",
			min = 1,
			max = 3)
	@CommandPermissions("modify.guard")
	public static void whitelist(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		if (args.argsLength() == 1) {
			player.sendMessage(ChatColor.GREEN
					+ StringUtils.listify(StringUtils.wrap(npc
							.getStrippedName() + "'s Whitelisted Players")));
			Set<String> list = guard.getWhitelist();
			if (list.isEmpty()) {
				player.sendMessage(ChatColor.RED + "No players whitelisted.");
			} else {
				for (String aList : list) {
					player.sendMessage(ChatColor.GREEN + aList);
				}
			}
		} else if (args.argsLength() == 3) {
			String allowed = args.getString(2).toLowerCase();
			boolean add = false;
			if (args.getString(1).equalsIgnoreCase("add")) {
				add = true;
			}
			if (add) {
				if (guard.getWhitelist().contains(allowed)) {
					player.sendMessage(ChatColor.RED
							+ "That player is already whitelisted.");
					return;
				}
				GuardManager.addToWhitelist(guard, allowed);
				player.sendMessage(ChatColor.GREEN + "Added "
						+ StringUtils.wrap(allowed) + " to "
						+ StringUtils.wrap(npc.getStrippedName() + "'s")
						+ " whitelist.");
			} else {
				if (!guard.getWhitelist().contains(allowed)) {
					player.sendMessage(ChatColor.RED
							+ "That player is not whitelisted.");
					return;
				}
				GuardManager.removeFromWhitelist(guard, allowed);
				player.sendMessage(ChatColor.GREEN + "Removed "
						+ StringUtils.wrap(allowed) + " from "
						+ StringUtils.wrap(npc.getStrippedName() + "'s")
						+ " whitelist.");
			}
		}
	}

	@Command(
			aliases = "guard",
			usage = "radius [radius]",
			desc = "change the protection radius of a bouncer",
			modifiers = "radius",
			min = 2,
			max = 2)
	@CommandPermissions("modify.guard")
	public static void changeRadius(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		if (guard.isBouncer()) {
			guard.setProtectionRadius(Double.parseDouble(args.getString(1)));
			player.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s")
					+ " protection radius has been set to "
					+ StringUtils.wrap(args.getString(1)) + ".");
		} else {
			Messaging.sendError(player, npc.getStrippedName()
					+ " must be a bouncer first.");
		}
	}
}