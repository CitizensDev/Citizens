package com.citizens.commands.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.npctypes.guards.GuardManager;
import com.citizens.npctypes.guards.GuardNPC;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.npclib.creatures.CreatureNPCType;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.resources.sk89q.ServerCommand;
import com.citizens.utils.HelpUtils;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.Messaging;
import com.citizens.utils.PathUtils;
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
	@CommandPermissions("guard.use.help")
	public static void guardHelp(CommandContext args, CommandSender sender,
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
	@CommandPermissions("guard.modify.type")
	public static void type(CommandContext args, Player player, HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		PathUtils.cancelTarget(npc);
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
			usage = "blacklist (add|remove) (entry)",
			desc = "control a guard's blacklist",
			modifiers = { "blacklist", "bl" },
			min = 1,
			max = 3)
	public static void blacklist(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		switch (args.argsLength()) {
		case 2:
			player.sendMessage(ChatColor.GRAY
					+ "Insufficient or too many arguments.");
			break;
		case 1:
			if (!Permission.generic(player, "citizens.guard.use.blacklist")) {
				player.sendMessage(MessageUtils.noPermissionsMessage);
				return;
			}
			player.sendMessage(ChatColor.GREEN
					+ StringUtils.listify(StringUtils.wrap(npc
							.getStrippedName() + "'s Blacklisted Mobs")));
			Set<String> list = guard.getBlacklist();
			if (list.isEmpty()) {
				player.sendMessage(ChatColor.RED + "No mobs blacklisted.");
			} else {
				for (String aList : list) {
					if (aList.isEmpty()) {
						continue;
					}
					if (CreatureType.fromName(StringUtils.capitalise(aList
							.toLowerCase())) != null) {
						aList = StringUtils.capitalise(aList.toLowerCase());
					}
					player.sendMessage(ChatColor.GREEN + "    - "
							+ StringUtils.wrap(aList));
				}
			}
			break;
		case 3:
			if (!Permission.generic(player, "citizens.guard.modify.blacklist")) {
				player.sendMessage(MessageUtils.noPermissionsMessage);
				return;
			}
			String mob = args.getString(2).toLowerCase();
			if (CreatureType.fromName(StringUtils.capitalise(mob)) == null
					&& !mob.equalsIgnoreCase("all")
					&& CreatureNPCType.fromName(mob) == null) {
				player.sendMessage(ChatColor.GRAY + "Invalid mob type.");
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
			usage = "radius [radius]",
			desc = "change the protection radius of a bouncer",
			modifiers = "radius",
			min = 2,
			max = 2)
	@CommandPermissions("guard.modify.radius")
	public static void radius(CommandContext args, Player player, HumanNPC npc) {
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

	@Command(
			aliases = "guard",
			usage = "aggro",
			desc = "set a guard to be aggressive",
			modifiers = "aggro",
			min = 1,
			max = 1)
	@CommandPermissions("guard.modify.aggro")
	public static void aggro(CommandContext args, Player player, HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		guard.setAggressive(!guard.isAggressive());
		if (guard.isAggressive()) {
			player.sendMessage(StringUtils.wrap(npc.getName())
					+ " is now aggressive.");
		} else {
			player.sendMessage(StringUtils.wrap(npc.getName())
					+ " has stopped being aggressive.");
		}
	}
}