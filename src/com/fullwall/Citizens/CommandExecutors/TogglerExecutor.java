package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
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
						buyTrader(npc, player);
					} else {
						toggleTrader(npc, player);
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
						buyHealer(npc, player);
					} else {
						toggleHealer(npc, player);
					}
					returnval = true;
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}

			} else if (args[0].equals("guard")) {
				sender.sendMessage("GUARDS AREN'T FINISHED YET! BE PATIENT! <3 the Citizens Team");
			} else if (args[0].equals("wizard")) {
				sender.sendMessage("WIZARDS AREN'T FINISHED YET! BE PATIENT! <3 the Citizens Team");
			} else if (args[0].equals("all")) {
				if (args[1].equals("on")) {
					toggleAllOn(npc, player);
				} else if (args[1].equals("off")) {
					toggleAllOff(npc, player);
				}
				returnval = true;
			} else {
				player.sendMessage(ChatColor.RED
						+ "Entered npc type was not recognised.");
				return true;
			}
			TraderPropertyPool.saveTraderState(npc);
		}
		return returnval;
	}

	/**
	 * Buys a trader using the economy interface. Toggles the npc to a trader
	 * afterwards.
	 * 
	 * @param npc
	 * @param player
	 */
	private void buyTrader(HumanNPC npc, Player player) {
		if (!EconomyHandler.useEconomy()
				|| EconomyHandler.canBuy(Operation.TRADER_NPC_CREATE, player)) {
			if (EconomyHandler.useEconomy()) {
				int paid = EconomyHandler.pay(Operation.TRADER_NPC_CREATE,
						player);
				if (paid > 0)
					player.sendMessage(MessageUtils.getPaidMessage(
							Operation.TRADER_NPC_CREATE, paid,
							npc.getStrippedName(), "trader", true));
				toggleTrader(npc, player);
				TraderPropertyPool.saveTrader(npc.getUID(), true);
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
				return;
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(
					Operation.TRADER_NPC_CREATE, player));
			return;
		}
	}

	/**
	 * Buys a healer using the economy interface. Toggles the npc to a healer
	 * afterwards.
	 * 
	 * @param npc
	 * @param player
	 */
	private void buyHealer(HumanNPC npc, Player player) {
		if (!EconomyHandler.useEconomy()
				|| EconomyHandler.canBuy(Operation.HEALER_NPC_CREATE, player)) {
			if (EconomyHandler.useEconomy()) {
				int paid = EconomyHandler.pay(Operation.HEALER_NPC_CREATE,
						player);
				if (paid > 0)
					player.sendMessage(MessageUtils.getPaidMessage(
							Operation.HEALER_NPC_CREATE, paid,
							npc.getStrippedName(), "healer", true));
				toggleHealer(npc, player);
				HealerPropertyPool.saveStrength(npc.getUID(), 20);
				HealerPropertyPool.saveHealer(npc.getUID(), true);
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(
					Operation.HEALER_NPC_CREATE, player));
			return;
		}
	}

	/**
	 * Toggles whether the selected npc is a trader or not.
	 * 
	 * @param npc
	 * @param player
	 */
	private void toggleTrader(HumanNPC npc, Player player) {
		npc.setTrader(!npc.isTrader());
		if (npc.isTrader())
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " is now a trader!");
		else
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " has stopped being a trader.");
	}

	/**
	 * Toggles whether the selected npc is a healer or not.
	 * 
	 * @param npc
	 * @param player
	 */
	private void toggleHealer(HumanNPC npc, Player player) {
		npc.setHealer(!npc.isHealer());
		if (npc.isHealer()) {
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " is now a healer!");
		} else {
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " has stopped being a healer.");
		}
	}

	/**
	 * Turns the selected NPC into all types of NPCs
	 * 
	 * @param npc
	 * @param player
	 */
	private void toggleAllOn(HumanNPC npc, Player player) {
		if (!npc.isTrader()) {
			toggleTrader(npc, player);
		}
		if (!npc.isHealer()) {
			toggleHealer(npc, player);
		}
	}

	/**
	 * Turns the selected NPC back to the basic type
	 * 
	 * @param npc
	 * @param player
	 */
	private void toggleAllOff(HumanNPC npc, Player player) {
		if (npc.isTrader()) {
			toggleTrader(npc, player);
		}
		if (npc.isHealer()) {
			toggleHealer(npc, player);
		}
	}
}