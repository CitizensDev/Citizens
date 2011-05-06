package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TogglerExecutor implements CommandExecutor {

	@SuppressWarnings("unused")
	private Citizens plugin;

	public TogglerExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	// perhaps can be removed. EG: buy a feature, now the
	// chat syntax gets unlocked, and you can do things like
	// "Hi, I want to trade" etc. without having to toggle functionality.
	// this is good for now I guess.
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player player = (Player) sender;
		HumanNPC npc = null;
		boolean returnval = false;
		if (NPCManager.validateSelected((Player) sender))
			npc = NPCManager
					.getNPC(NPCManager.NPCSelected.get(player.getName()));
		else {
			player.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			player.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED
					+ "You didn't specify an NPC type to toggle.");
			return true;
		} else {
			if (args[0].equals("trader")) {
				if (BasicExecutor.hasPermission("citizens.trader.create",
						sender)) {
					if (!TraderPropertyPool.isTrader(npc.getUID())) {
						buyState(player, npc.getTrader(),
								Operation.TRADER_NPC_CREATE);
					} else {
						toggleState(player, npc.getTrader());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args[0].equals("quester")) {
				sender.sendMessage("QUESTS AREN'T FINISHED YET! BE PATIENT! <3 the Citizens Team");
			} else if (args[0].equals("healer")) {
				if (BasicExecutor.hasPermission("citizens.healer.create",
						sender)) {
					if (!HealerPropertyPool.isHealer(npc.getUID())) {
						buyState(player, npc.getHealer(),
								Operation.HEALER_NPC_CREATE);
					} else {
						toggleState(player, npc.getHealer());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args[0].equals("guard")) {
				sender.sendMessage("GUARDS AREN'T FINISHED YET! BE PATIENT! <3 the Citizens Team");
				returnval = true;
			} else if (args[0].equals("wizard")) {
				sender.sendMessage("WIZARDS AREN'T FINISHED YET! BE PATIENT! <3 the Citizens Team");
				returnval = true;
			} else if (args.length == 2 && args[0].equals("all")) {
				if (args[1].equals("on"))
					toggleAll(npc, player, true);
				else if (args[1].equals("off"))
					toggleAll(npc, player, false);
				returnval = true;
			} else {
				player.sendMessage(ChatColor.RED
						+ "Entered npc type was not recognized.");
				return true;
			}
		}
		return returnval;
	}

	/**
	 * Toggles an NPC state.
	 * 
	 * @param player
	 * @param toggleable
	 */
	private void toggleState(Player player, Toggleable toggleable) {
		toggleable.toggle();
		toggleable.saveState();
		if (toggleable.getToggle()) {
			player.sendMessage(StringUtils.yellowify(toggleable.getName())
					+ " is now a " + toggleable.getType() + "!");
		} else {
			player.sendMessage(StringUtils.yellowify(toggleable.getName())
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
	private void buyState(Player player, Toggleable toggleable, Operation op) {
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				int paid = EconomyHandler.pay(op, player);
				if (paid > 0)
					player.sendMessage(MessageUtils.getPaidMessage(op, paid,
							toggleable.getName(), toggleable.getType(), true));
				toggleable.registerState();
				toggleState(player, toggleable);
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
			return;
		}
	}

	/**
	 * Turns the selected NPC into all types of NPCs
	 * 
	 * @param npc
	 * @param player
	 */
	private void toggleAll(HumanNPC npc, Player player, boolean on) {
		if (on) {
			if (!npc.isTrader()) {
				toggleState(player, npc.getTrader());
			}
			if (!npc.isHealer()) {
				toggleState(player, npc.getHealer());
			}
		} else {
			if (npc.isTrader()) {
				toggleState(player, npc.getTrader());
			}
			if (npc.isHealer()) {
				toggleState(player, npc.getHealer());
			}
		}
	}
}