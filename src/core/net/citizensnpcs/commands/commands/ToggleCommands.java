package net.citizensnpcs.commands.commands;

import java.util.Map.Entry;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCManager;
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
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.utils.PageUtils.PageInstance;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleCommands implements CommandHandler {

	@Command(
			aliases = "toggle",
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
		for (String type : CitizensNPCManager.getTypes().keySet()) {
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
		if (!CitizensNPCManager.validType(type)) {
			Messaging.sendError(player, MessageUtils.invalidNPCTypeMessage);
			return;
		}
		if (!PropertyManager.npcHasType(npc, type)) {
			buyState(player, npc, CitizensNPCManager.getType(type));
		} else {
			toggleState(player, npc, CitizensNPCManager.getType(type));
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
	@CommandPermissions("admin.toggleall")
	public static void toggleAll(CommandContext args, Player player,
			HumanNPC npc) {
		if (args.getString(1).equalsIgnoreCase("on")) {
			toggleAll(player, npc, true);
		} else if (args.getString(1).equalsIgnoreCase("off")) {
			toggleAll(player, npc, false);
		}
	}

	/**
	 * Toggles an NPC state.
	 * 
	 * @param player
	 * @param npc
	 * @param type
	 */
	private static void toggleState(Player player, HumanNPC npc,
			CitizensNPC type) {
		if (!npc.isType(type.getType())) {
			PropertyManager.get(type.getType()).setEnabled(npc, true);
			npc.addType(type.getType());
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is now a " + type.getType() + "!");
		} else {
			PropertyManager.get(type.getType()).setEnabled(npc, false);
			npc.removeType(type.getType());
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has stopped being a " + type.getType() + ".");
		}
	}

	/**
	 * Buys an NPC state.
	 * 
	 * @param player
	 * @param npc
	 * @param type
	 */
	private static void buyState(Player player, HumanNPC npc, CitizensNPC type) {
		String toggle = type.getType();
		if (!PermissionManager.generic(player, "citizens.toggle." + toggle)) {
			Messaging.send(player, npc, MessageUtils.noPermissionsMessage);
			return;
		}
		if (EconomyManager.hasEnough(player,
				UtilityProperties.getPrice(toggle + ".creation"))) {
			double paid = EconomyManager.pay(player,
					UtilityProperties.getPrice(toggle + ".creation"));
			if (paid > 0) {
				Messaging.send(
						player,
						MessageUtils.getPaidMessage(player, toggle, toggle
								+ ".creation", npc.getStrippedName(), true));
			}
			toggleState(player, npc, type);
		} else {
			Messaging.send(player, npc, MessageUtils.getNoMoneyMessage(player,
					toggle + ".creation"));
		}
	}

	/**
	 * Toggles all types of NPCs
	 * 
	 * @param player
	 * @param npc
	 * @param on
	 */
	private static void toggleAll(Player player, HumanNPC npc, boolean on) {
		if (on) {
			for (Entry<String, CitizensNPC> entry : CitizensNPCManager
					.getTypes().entrySet()) {
				if (!npc.isType(entry.getValue().getType())) {
					toggleState(player, npc, entry.getValue());
				}
			}
		} else {
			for (CitizensNPC type : npc.types()) {
				toggleState(player, npc, type);
			}
		}
	}

	@Override
	public void addPermissions() {
		PermissionManager.addPerm("toggle.help");
		PermissionManager.addPerm("admin.toggleall");
		for (String type : Citizens.loadedTypes) {
			PermissionManager.addPerm("toggle." + type);
		}
	}
}