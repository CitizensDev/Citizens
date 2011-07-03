package com.citizens.Commands.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.citizens.NPCTypes.Guards.GuardNPC;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Resources.sk89q.Command;
import com.citizens.Resources.sk89q.CommandContext;
import com.citizens.Resources.sk89q.CommandPermissions;
import com.citizens.Resources.sk89q.CommandRequirements;
import com.citizens.Utils.HelpUtils;
import com.citizens.Utils.Messaging;
import com.citizens.Utils.StringUtils;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "guard")
public class GuardCommands {

	@CommandRequirements()
	@Command(
			aliases = "guard",
			usage = "help",
			desc = "view the guard help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.guard")
	public static void sendGuardHelp(CommandContext args, Player player,
			HumanNPC npc) {
		HelpUtils.sendGuardHelp(player);
	}

	@Command(
			aliases = "guard",
			usage = "type [type]",
			desc = "change a guard's type",
			modifiers = "type",
			min = 2,
			max = 2)
	@CommandPermissions("modify.guard")
	public static void changeType(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		if (args.getString(1).equalsIgnoreCase("bodyguard")) {
			if (!guard.isBodyguard()) {
				guard.setBodyguard();
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " is now a bodyguard.");
			} else {
				Messaging.sendError(player, npc.getStrippedName()
						+ " is already a bodyguard.");
			}
		} else if (args.getString(1).equalsIgnoreCase("bouncer")) {
			if (!guard.isBouncer()) {
				guard.setBouncer();
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " is now a bouncer.");
			} else {
				Messaging.sendError(player, npc.getStrippedName()
						+ " is already a bouncer.");
			}
		} else {
			Messaging.sendError(player, "That is not a valid guard type.");
		}
	}

	@Command(
			aliases = "guard",
			usage = "blacklist (mob)",
			desc = "add mobs to a guard's blacklist",
			modifiers = "blacklist",
			min = 1,
			max = 2)
	@CommandPermissions("modify.guard")
	public static void blacklist(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		if (args.argsLength() == 1) {
			player.sendMessage(ChatColor.GREEN
					+ "========== "
					+ StringUtils.wrap(npc.getStrippedName()
							+ "'s Blacklisted Mobs") + " ==========");
			List<String> list = guard.getBlacklist();
			if (list.isEmpty()) {
				player.sendMessage(ChatColor.RED + "No mobs blacklisted.");
			} else {
				for (String aList : list) {
					player.sendMessage(ChatColor.RED + aList);
				}
			}
		} else if (args.argsLength() == 2) {
			String mob = args.getString(1).toLowerCase();
			if (guard.getBlacklist().contains(mob)) {
				player.sendMessage(ChatColor.RED
						+ "That mob is already blacklisted.");
			} else if (CreatureType.fromName(StringUtils.capitalise(mob)) != null) {
				guard.addToBlacklist(mob);
				player.sendMessage(ChatColor.GREEN + "You added the mob type "
						+ StringUtils.wrap(mob) + " to "
						+ StringUtils.wrap(npc.getStrippedName() + "'s")
						+ " blacklist.");
			} else if (mob.equalsIgnoreCase("all")) {
				guard.addToBlacklist(mob);
				player.sendMessage(ChatColor.GREEN + "You added all mobs to "
						+ StringUtils.wrap(npc.getStrippedName() + "'s")
						+ " blacklist.");
			} else {
				player.sendMessage(ChatColor.RED + "Invalid mob type.");
			}
		}
	}

	@Command(
			aliases = "guard",
			usage = "whitelist (player)",
			desc = "add players to a guard's whitelist",
			modifiers = "whitelist",
			min = 1,
			max = 2)
	@CommandPermissions("modify.guard")
	public static void whitelist(CommandContext args, Player player,
			HumanNPC npc) {
		GuardNPC guard = npc.getToggleable("guard");
		if (args.argsLength() == 1) {
			player.sendMessage(ChatColor.GREEN
					+ "========== "
					+ StringUtils.wrap(npc.getStrippedName()
							+ "'s Whitelisted Players") + " ==========");
			List<String> list = guard.getWhitelist();
			if (list.isEmpty()) {
				player.sendMessage(ChatColor.RED + "No players whitelisted.");
			} else {
				for (String aList : list) {
					player.sendMessage(ChatColor.GREEN + aList);
				}
			}
		} else if (args.argsLength() == 2) {
			String allowed = args.getString(1).toLowerCase();
			if (guard.getWhitelist().contains(allowed)) {
				player.sendMessage(ChatColor.RED
						+ "That player is already whitelisted.");
			} else {
				String msg = ChatColor.GREEN + "You added ";
				if (args.getString(1).equalsIgnoreCase("all")) {
					List<String> emptyList = new ArrayList<String>();
					guard.setWhitelist(emptyList);
					emptyList.add("all");
					msg += "all players";
				} else {
					guard.addToWhitelist(allowed);
					msg += StringUtils.wrap(allowed);
					player.sendMessage(ChatColor.GREEN + "You added "
							+ StringUtils.wrap(allowed) + " to "
							+ StringUtils.wrap(npc.getStrippedName() + "'s")
							+ " whitelist.");
				}
				msg += " to " + StringUtils.wrap(npc.getStrippedName() + "'s")
						+ " whitelist";
				player.sendMessage(msg);
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