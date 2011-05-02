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
		if (!NPCManager.validateOwnership(npc.getUID(), player)) {
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
					if (!TraderPropertyPool.isTrader(npc.getUID()))
						buyTrader(npc, player);
					else
						toggleTrader(npc, player);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			TraderPropertyPool.saveTraderState(npc);
		}
		return returnval;
	}

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
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(
					Operation.TRADER_NPC_CREATE, player));
			return;
		}
	}

	private void toggleTrader(HumanNPC npc, Player player) {
		npc.setTrader(!npc.isTrader());
		if (npc.isTrader())
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName(),
					ChatColor.GREEN) + " is now a trader!");
		else
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName(),
					ChatColor.GREEN) + " has stopped being a trader.");
	}
}
