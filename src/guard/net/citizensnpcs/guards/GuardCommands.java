package net.citizensnpcs.guards;

import java.util.Set;

import net.citizensnpcs.Permission;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.guards.flags.FlagInfo;
import net.citizensnpcs.guards.flags.FlagList.FlagType;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.resources.sk89q.ServerCommand;
import net.citizensnpcs.utils.EntityUtils;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.platymuus.bukkit.permissions.Group;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "guard")
public class GuardCommands implements CommandHandler {

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
		Guard guard = npc.getType("guard");
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
			usage = "flags",
			desc = "view a guard's flags",
			modifiers = "flags",
			min = 1,
			max = 1)
	@CommandPermissions("guard.use.flags")
	public static void flags(CommandContext args, Player player, HumanNPC npc) {
		// TODO display a guard's current flags
	}

	@Command(
			aliases = "guard",
			usage = "addflag (-i [priority]) [target] (-a -g -m -p)",
			desc = "add a flag to a guard",
			modifiers = { "addflag", "af", },
			flags = "agmpi",
			min = 1)
	@CommandPermissions("guard.modify.flags")
	public static void addFlag(CommandContext args, Player player, HumanNPC npc) {
		if (!args.hasFlag('a') && !args.hasFlag('g') && !args.hasFlag('m')
				&& !args.hasFlag('p')) {
			player.sendMessage(ChatColor.GRAY + "No type flags specified.");
			return;
		}

		Guard guard = npc.getType("guard");
		int flagOffset = 1, priority = 1;
		if (args.hasFlag('i')) {
			++flagOffset;
			priority = args.getInteger(1);
		}
		boolean isSafe = args.getString(flagOffset).charAt(0) == '-';
		if (args.hasFlag('a')) {
			guard.getFlags().addToAll(args.getFlags(),
					FlagInfo.newInstance("all", priority, isSafe));
		} else if (args.argsLength() == 1 || flagOffset == 2) {
			player.sendMessage(ChatColor.GRAY + "No name given.");
			return;
		}

		String name = isSafe ? args.getJoinedStrings(flagOffset).replaceFirst(
				"-", "") : args.getJoinedStrings(flagOffset);
		name = name.toLowerCase();

		FlagType type = FlagType.PLAYER;
		if (args.hasFlag('g')) {
			if (!Permission.useSuperPerms()) {
				player.sendMessage(ChatColor.GRAY
						+ "Group flags require bukkit's permission system to be used.");
				return;
			}
			Group group = Permission.getGroup(name);
			if (group == null) {
				player.sendMessage(ChatColor.GRAY + "Group not recognised.");
				return;
			}
			type = FlagType.GROUP;
		}
		if (args.hasFlag('m')) {
			if (!EntityUtils.validType(name, true)) {
				player.sendMessage(ChatColor.GRAY + "Mob type not recognised.");
				return;
			}
			type = FlagType.MOB;
		}
		String prefix = guard.getFlags().contains(type, name) ? "Updated"
				: "Added";
		guard.getFlags().addFlag(type,
				FlagInfo.newInstance(name, priority, isSafe));
		player.sendMessage(ChatColor.GREEN + prefix + " flag entry for "
				+ StringUtils.wrap(name) + ".");
	}

	@Command(
			aliases = "guard",
			usage = "delflag [name] [-p, -m, -g] (-a)",
			desc = "deletes a flag from a guard",
			modifiers = { "delflag", "df", },
			flags = "agmp",
			min = 1)
	@CommandPermissions("guard.modify.flags")
	public static void deleteFlag(CommandContext args, Player player,
			HumanNPC npc) {
		if (!args.hasFlag('a') && !args.hasFlag('g') && !args.hasFlag('m')
				&& !args.hasFlag('p')) {
			player.sendMessage(ChatColor.GRAY + "No type flags specified.");
			return;
		}
		Guard guard = npc.getType("guard");
		Set<Character> flags = args.getFlags();
		if (flags.contains('a') && flags.size() == 1) {
			guard.getFlags().clear();
			player.sendMessage(ChatColor.GREEN + "All flags cleared.");
			return;
		} else if (flags.contains('a')) {
			for (Character character : flags) {
				guard.getFlags().getFlags(FlagType.fromCharacter(character))
						.clear();
			}
			player.sendMessage(ChatColor.GREEN
					+ "All flags from specified types cleared.");
			return;
		}
		if (args.argsLength() == 1) {
			player.sendMessage(ChatColor.GRAY + "No flag specified.");
			return;
		}
		FlagType type = flags.contains('g') ? FlagType.GROUP : flags
				.contains('m') ? FlagType.MOB : FlagType.PLAYER;

		if (!guard.getFlags().contains(type, args.getJoinedStrings(1))) {
			player.sendMessage(ChatColor.GRAY + "Specified flag not found.");
			return;
		}
		guard.getFlags().getFlags(type).remove(args.getJoinedStrings(1));
		player.sendMessage(StringUtils.wrap(args.getJoinedStrings(1))
				+ " removed.");
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
		Guard guard = npc.getType("guard");
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
		Guard guard = npc.getType("guard");
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