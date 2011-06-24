package com.fullwall.Citizens.Commands.Commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.sk89q.commands.Command;
import com.fullwall.resources.sk89q.commands.CommandContext;
import com.fullwall.resources.sk89q.commands.CommandPermissions;
import com.fullwall.resources.sk89q.commands.CommandRequirements;

public class ToggleCommands {

	// Note: Having a class annotation for these commands still gives an NPE, 
	// so I had to use the requirements for each method
	@CommandRequirements(
			requireSelected = true,
			requireOwnership = true)
	@Command(
			aliases = { "toggle", "tog", "t" },
			usage = "[type]",
			desc = "toggle an NPC type",
			modifiers = { "blacksmith", "guard", "healer", "quester", "trader", "wizard" },
			min = 1,
			max = 1)
	@CommandPermissions("create.")
	public static void toggleNPC(CommandContext args, Player player,
			HumanNPC npc) {
		String type = args.getString(0).toLowerCase();
		if (!PropertyManager.typeExists(npc, type)) {
			buyState(player, npc.getType(type),
					Operation.valueOf(type.toUpperCase() + "_CREATION"));
		} else {
			toggleState(player, npc.getType(type));
		}
	}

	@CommandRequirements(
			requireSelected = true,
			requireOwnership = true)
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
	 * @param toggleable
	 */
	private static void toggleState(Player player, Toggleable toggleable) {
		toggleable.toggle();
		toggleable.saveState();
		if (toggleable.getToggle()) {
			player.sendMessage(StringUtils.wrap(toggleable.getName())
					+ " is now a " + toggleable.getType() + "!");
		} else {
			player.sendMessage(StringUtils.wrap(toggleable.getName())
					+ " has stopped being a " + toggleable.getType() + ".");
		}
	}

	/**
	 * Buys an NPC state.
	 * 
	 * @param player
	 * @param toggleable
	 * @param op
	 */
	private static void buyState(Player player, Toggleable toggleable,
			Operation op) {
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(op, player);
				if (paid > 0) {
					player.sendMessage(MessageUtils.getPaidMessage(op, paid,
							toggleable.getName(), toggleable.getType(), true));
				}
				toggleable.register();
				toggleState(player, toggleable);
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
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
		Map<String, Toggleable> toggle = new HashMap<String, Toggleable>();
		toggle.put("blacksmith", npc.getBlacksmith());
		toggle.put("guard", npc.getGuard());
		toggle.put("healer", npc.getHealer());
		toggle.put("quester", npc.getQuester());
		toggle.put("trader", npc.getTrader());
		toggle.put("wizard", npc.getWizard());
		if (on) {
			for (Toggleable t : toggle.values()) {
				if (!t.getToggle()) {
					toggleState(player, toggle.get(t.getType()));
				}
			}
		} else {
			for (Toggleable t : toggle.values()) {
				if (t.getToggle()) {
					toggleState(player, toggle.get(t.getType()));
				}
			}
		}
	}
}