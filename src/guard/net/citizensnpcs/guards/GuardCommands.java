package net.citizensnpcs.guards;

import java.util.Map;
import java.util.Set;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.guards.flags.FlagInfo;
import net.citizensnpcs.guards.flags.FlagList;
import net.citizensnpcs.guards.flags.FlagList.FlagType;
import net.citizensnpcs.permissions.CitizensGroup;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.resources.sk89q.ServerCommand;
import net.citizensnpcs.utils.EntityUtils;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PageUtils;
import net.citizensnpcs.utils.PageUtils.PageInstance;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "guard")
public class GuardCommands extends CommandHandler {
	public static final GuardCommands INSTANCE = new GuardCommands();

	private GuardCommands() {
	}

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
		INSTANCE.sendHelpPage(sender);
	}

	@Command(
			aliases = "guard",
			usage = "[type]",
			desc = "change a guard's type",
			modifiers = { "soldier", "bodyguard", "bouncer" },
			min = 1,
			max = 1)
	@CommandPermissions("guard.modify.type")
	public static void type(CommandContext args, Player player, HumanNPC npc) {
		Guard guard = npc.getType("guard");
		PathUtils.cancelTarget(npc);
		GuardState state = GuardState.parse(args.getString(0));
		if (state == GuardState.NULL) {
			Messaging.sendError(player, "That is not a valid guard type.");
			return;
		}
		if (!guard.isState(state)) {
			guard.setGuardState(state);
			player.sendMessage(StringUtils.wrap(npc.getName()) + " is now a "
					+ state.name().toLowerCase() + ".");
		} else {
			guard.setGuardState(GuardState.NULL);
			player.sendMessage(StringUtils.wrap(npc.getName())
					+ " has stopped being a " + state.name().toLowerCase()
					+ ".");
		}
	}

	@Command(
			aliases = "guard",
			usage = "flags [-g,m,p] (page)",
			desc = "view a guard's flags",
			modifiers = "flags",
			flags = "gmp",
			min = 1,
			max = 2)
	@CommandPermissions("guard.use.flags")
	public static void flags(CommandContext args, Player player, HumanNPC npc) {
		int page = 1;
		if (args.argsLength() == 2) {
			if (!StringUtils.isNumber(args.getString(1))) {
				Messaging.sendError(player, "That is not a valid number.");
				return;
			}
			page = args.getInteger(1);
		}
		if (args.getFlags().isEmpty()) {
			Messaging.sendError(player, "No flag specified.");
			return;
		}
		Guard guard = npc.getType("guard");
		FlagList flagList = guard.getFlags();
		Map<String, FlagInfo> flags;
		String header = npc.getName() + "'s ";
		if (args.hasFlag('g')) {
			flags = flagList.getFlags(FlagType.GROUP);
			header += "Group Flags";
		} else if (args.hasFlag('m')) {
			flags = flagList.getFlags(FlagType.MOB);
			header += "Mob Flags";
		} else if (args.hasFlag('p')) {
			flags = flagList.getFlags(FlagType.PLAYER);
			header += "Player Flags";
		} else {
			Messaging.sendError(player, "Specified flag not found.");
			return;
		}
		PageInstance instance = PageUtils.newInstance(player);
		instance.header(ChatColor.GREEN
				+ StringUtils.listify(StringUtils.wrap(header + ChatColor.WHITE
						+ " <%x/%y>")));
		for (String entry : flags.keySet()) {
			instance.push(StringUtils.wrap("  - ") + entry);
		}
		instance.process(page);
	}

	@Command(
			aliases = "guard",
			usage = "addflag (priority) [target] (-a,g,m,p)",
			desc = "add a flag to a guard",
			modifiers = { "addflag", "af" },
			flags = "agmp",
			min = 1)
	@CommandPermissions("guard.modify.flags")
	public static void addFlag(CommandContext args, Player player, HumanNPC npc) {
		if (args.getFlags().size() == 0) {
			player.sendMessage(ChatColor.GRAY + "No type flags specified.");
			return;
		}

		Guard guard = npc.getType("guard");
		int offset = 1, priority = 1;
		if (args.argsLength() > 1 && StringUtils.isNumber(args.getString(1))) {
			priority = args.getInteger(offset);
			++offset;
		}
		if (priority < 1 || priority > 20) {
			player.sendMessage(ChatColor.GRAY
					+ "Priority must be between 1-20.");
			return;
		}
		if (args.hasFlag('a')) {
			guard.getFlags().addToAll(
					args.getFlags(),
					FlagInfo.newInstance("all", priority,
							args.argsLength() > offset ? args.getString(offset)
									.charAt(0) == '-' : false));
			player.sendMessage(ChatColor.GREEN
					+ "Added specified flags to specified types.");
			return;
		} else if (args.argsLength() == 1
				|| (args.argsLength() == 2 && offset == 2)) {
			player.sendMessage(ChatColor.GRAY + "No name given.");
			return;
		}
		boolean isSafe = args.getString(offset).charAt(0) == '-';

		String name = isSafe ? args.getJoinedStrings(offset).replaceFirst("-",
				"") : args.getJoinedStrings(offset);
		name = name.toLowerCase().trim();

		FlagType type = FlagType.PLAYER;
		if (args.hasFlag('g')) {
			if (!PermissionManager.hasBackend()) {
				player.sendMessage(ChatColor.GRAY
						+ "Group flags require a Permissions backend.");
				return;
			}
			CitizensGroup group = PermissionManager.getGroup(name);
			if (group == null) {
				player.sendMessage(ChatColor.GRAY + "Group not recognized.");
				return;
			}
			type = FlagType.GROUP;
		}
		if (args.hasFlag('m')) {
			if (!EntityUtils.validType(name, true)) {
				player.sendMessage(ChatColor.GRAY + "Mob type not recognized.");
				return;
			}
			type = FlagType.MOB;
		}
		if (args.hasFlag('p')) {
			if (name.contains(" ")) {
				player.sendMessage(ChatColor.GRAY
						+ "Player names can't have spaces in them.");
			}
			if (player.getName().equals(name)) {
				player.sendMessage(ChatColor.GRAY + "You can't flag yourself!");
				return;
			}
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
			usage = "delflag [name] [-p,m,g] (-a)",
			desc = "deletes a flag from a guard",
			modifiers = { "delflag", "df" },
			flags = "agmp",
			min = 1)
	@CommandPermissions("guard.modify.flags")
	public static void deleteFlag(CommandContext args, Player player,
			HumanNPC npc) {
		if (args.getFlags().size() == 0) {
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
		guard.getFlags().removeFlag(type, args.getJoinedStrings(1));
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
		guard.setProtectionRadius(Double.parseDouble(args.getString(1)));
		player.sendMessage(StringUtils.wrap(npc.getName() + "'s")
				+ " protection radius has been set to "
				+ StringUtils.wrap(args.getString(1)) + ".");
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

	@Override
	public void addPermissions() {
		PermissionManager.addPermission("guard.use.flags");
		PermissionManager.addPermission("guard.use.help");
		PermissionManager.addPermission("guard.modify.type");
		PermissionManager.addPermission("guard.modify.flags");
		PermissionManager.addPermission("guard.modify.aggro");
		PermissionManager.addPermission("guard.modify.radius");
	}

	@Override
	public void sendHelpPage(CommandSender sender) {
		HelpUtils.header(sender, "Guard", 1, 1);
		HelpUtils.format(sender, "guard", "[type]",
				"toggle the type of guard that an NPC is");
		HelpUtils.format(sender, "guard", "flags [-g,m,p] (page)",
				"view a guard's flags");
		HelpUtils.format(sender, "guard",
				"addflag (priority) [target] (-a,g,m,p)",
				"add a flag to a guard");
		HelpUtils.format(sender, "guard", "delflag [name] [-g,m,p] (-a)",
				"delete a flag from a guard");
		HelpUtils.format(sender, "guard", "radius [amount]",
				"set the radius of a bouncer's zone");
		HelpUtils.format(sender, "guard", "aggro", "toggle aggro");
		HelpUtils.footer(sender);
	}
}