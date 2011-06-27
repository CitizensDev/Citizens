package com.citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.Interfaces.NPCPurchaser;
import com.citizens.Interfaces.NPCType;
import com.citizens.Interfaces.Toggleable;
import com.citizens.NPCs.NPCManager;
import com.citizens.Properties.PropertyManager;
import com.citizens.Utils.StringUtils;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;
import com.citizens.resources.sk89q.commands.Command;
import com.citizens.resources.sk89q.commands.CommandContext;
import com.citizens.resources.sk89q.commands.CommandPermissions;
import com.citizens.resources.sk89q.commands.CommandRequirements;
import com.iConomy.util.Messaging;

public class ToggleCommands {

	@CommandRequirements(requireSelected = true, requireOwnership = true)
	@Command(
			aliases = { "toggle", "tog", "t" },
			usage = "[type]",
			desc = "toggle an NPC type",
			modifiers = { "blacksmith", "guard", "healer", "quester", "trader",
					"wizard" },
			min = 1,
			max = 1)
	@CommandPermissions("create.")
	public static void toggleNPC(CommandContext args, Player player,
			HumanNPC npc) {
		String type = args.getString(0).toLowerCase();
		if (!NPCManager.validType(type)) {
			player.sendMessage(ChatColor.GRAY + "Invalid toggle type.");
			return;
		}
		if (!PropertyManager.npcHasType(npc, type)) {
			buyState(player, npc, NPCManager.getType(type));
		} else {
			toggleState(player, npc, NPCManager.getType(type), false);
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
	@CommandPermissions("admin")
	public static void toggleAllNPCTypes(CommandContext args, Player player,
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
	 * @param register
	 * @param toggleable
	 */
	private static void toggleState(Player player, HumanNPC npc, NPCType type,
			boolean register) {
		if (register)
			type.factory().create(npc).register();
		if (!npc.isType(type.getType())) {
			npc.addType(type.getType(), type.factory());
			player.sendMessage(StringUtils.wrap(npc.getName()) + " is now a "
					+ type.getType() + "!");
		} else {
			npc.removeType(type.getType());
			player.sendMessage(StringUtils.wrap(npc.getName())
					+ " has stopped being a " + type.getType() + ".");
		}
	}

	/**
	 * Buys an NPC state.
	 * 
	 * @param player
	 * @param toggleable
	 */
	private static void buyState(Player player, HumanNPC npc, NPCType type) {
		NPCPurchaser purchaser = type.getPurchaser();
		String toggle = type.getType();
		if (purchaser.canBuy(player, toggle)) {
			double paid = purchaser.pay(player, toggle);
			if (paid > 0) {
				String message = purchaser.getPaidMessage(player, npc, paid,
						toggle);
				Messaging.send(player, message);
			}
			toggleState(player, npc, type, true);
		} else {
			String message = purchaser.getNoMoneyMessage(player, npc, toggle);
			Messaging.send(player, message);
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
			for (NPCType t : NPCManager.types.values()) {
				if (!npc.isType(t.getType())) {
					toggleState(player, npc, t, false);
				}
			}
		} else {
			for (Toggleable t : npc.types()) {
				toggleState(player, npc, NPCManager.getType(t.getType()), false);
			}
		}
	}
}