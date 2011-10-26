package net.citizensnpcs.commands;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.npc.NPCToggleTypeEvent;
import net.citizensnpcs.economy.Economy;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.resources.sk89q.ServerCommand;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PageUtils;
import net.citizensnpcs.utils.PageUtils.PageInstance;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleCommands extends CommandHandler {

	@Command(
			aliases = { "toggle", "tog", "t" },
			usage = "list (page)",
			desc = "view list of toggles",
			modifiers = { "list", "help" },
			min = 1,
			max = 2)
	@ServerCommand()
	@CommandPermissions("toggle.help")
	@CommandRequirements()
	public static void toggleHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		PageInstance instance = PageUtils.newInstance(sender);
		int page = 1;
		if (args.argsLength() == 2) {
			page = args.getInteger(1);
		}
		instance.header(ChatColor.YELLOW
				+ StringUtils.listify(ChatColor.GREEN
						+ "NPC Toggle List <%x/%y>" + ChatColor.YELLOW));
		for (String type : NPCTypeManager.getTypes().keySet()) {
			instance.push(ChatColor.GREEN
					+ "    - "
					+ StringUtils.wrap(StringUtils.capitalise(type
							.toLowerCase())));
		}
		instance.process(page);
	}

	@CommandRequirements(requireSelected = true, requireOwnership = true)
	@Command(
			aliases = { "toggle", "tog", "t" },
			usage = "[type]",
			desc = "toggle an NPC type",
			modifiers = "*",
			min = 1,
			max = 1)
	public static void toggle(CommandContext args, Player player, HumanNPC npc) {
		String type = args.getString(0).toLowerCase();
		if (!NPCTypeManager.validType(type)) {
			Messaging.sendError(player, MessageUtils.invalidNPCTypeMessage);
			return;
		}
		if (!PropertyManager.npcHasType(npc, type)) {
			buyState(player, npc, NPCTypeManager.getType(type));
		} else {
			toggleState(player, npc, NPCTypeManager.getType(type).getName());
		}
	}

	@CommandRequirements(requireSelected = true, requireOwnership = true)
	@Command(
			aliases = { "toggle", "tog", "t" },
			usage = "all [on|off]",
			desc = "toggle all NPC types",
			modifiers = "all",
			min = 2,
			max = 2)
	@CommandPermissions("toggle.all")
	public static void toggleAll(CommandContext args, Player player,
			HumanNPC npc) {
		if (args.getString(1).equalsIgnoreCase("on")) {
			toggleAll(player, npc, true);
		} else if (args.getString(1).equalsIgnoreCase("off")) {
			toggleAll(player, npc, false);
		}
	}

	// Toggles an NPC state.
	private static void toggleState(Player player, HumanNPC npc, String type) {
		if (!npc.isType(type)) {
			PropertyManager.get(type).setEnabled(npc, true);
			npc.addType(type);
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is now a " + type + "!");
		} else {
			PropertyManager.get(type).setEnabled(npc, false);
			npc.removeType(type);
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has stopped being a " + type + ".");
		}
		Bukkit.getServer().getPluginManager()
				.callEvent(new NPCToggleTypeEvent(npc, type, npc.isType(type)));
	}

	// Buys an NPC state.
	private static void buyState(Player player, HumanNPC npc,
			CitizensNPCType type) {
		String toggle = type.getName();
		if (!PermissionManager.hasPermission(player, "citizens.toggle." + toggle)) {
			Messaging.send(player, npc, MessageUtils.noPermissionsMessage);
			return;
		}
		if (Economy.hasEnough(player,
				UtilityProperties.getPrice(toggle + ".creation"))) {
			double paid = Economy.pay(player,
					UtilityProperties.getPrice(toggle + ".creation"));
			if (paid > 0) {
				Messaging.send(
						player,
						MessageUtils.getPaidMessage(player, toggle, toggle
								+ ".creation", npc.getStrippedName(), true));
			}
			toggleState(player, npc, type.getName());
		} else {
			Messaging.send(player, npc, MessageUtils.getNoMoneyMessage(player,
					toggle + ".creation"));
		}
	}

	// Toggles all types of NPCs
	private static void toggleAll(Player player, HumanNPC npc, boolean on) {
		if (on) {
			for (CitizensNPCType entry : NPCTypeManager.getTypes().values()) {
				if (!npc.isType(entry.getName())) {
					toggleState(player, npc, entry.getName());
				}
			}
		} else {
			for (CitizensNPC type : npc.types()) {
				toggleState(player, npc, type.getType().getName());
			}
		}
	}

	@Override
	public void addPermissions() {
		PermissionManager.addPermission("toggle.help");
		PermissionManager.addPermission("toggle.all");
		for (String type : Citizens.loadedTypes) {
			PermissionManager.addPermission("toggle." + type);
		}
	}
}