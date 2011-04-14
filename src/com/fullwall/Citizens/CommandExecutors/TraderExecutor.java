package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.IconomyInterface;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderExecutor implements CommandExecutor {

	private Citizens plugin;

	public TraderExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player player = (Player) sender;
		HumanNPC npc = null;
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
		if (!npc.isTrader()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a trader yet.");
			return true;
		} else {// trader balance add/subtract [amt]
			if (args.length == 3 && args[0].equals("balance")) {
				if (BasicExecutor.hasPermission("citizens.trader.balance",
						sender)) {
					try {
						if (!EconomyHandler.useIconomy())
							player.sendMessage(ChatColor.GRAY
									+ "This server is not using an economy plugin.");
						else
							changeNPCBalance(player, npc, args);
					} catch (NumberFormatException e) {
						player.sendMessage(ChatColor.RED
								+ "Invalid balance change amount entered.");
					}
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				return true;
			}
		}
		return false;
	}

	private void changeNPCBalance(Player player, HumanNPC npc, String[] args)
			throws NumberFormatException {
		int amount = Integer.valueOf(args[2]);
		if (args[1].equals("give")) {
			if (IconomyInterface.playerHasEnough(player.getName(), amount)) {
				npc.getTraderNPC().setBalance(
						npc.getTraderNPC().getBalance() + amount);
				IconomyInterface.pay(player, amount);
				player.sendMessage(ChatColor.GREEN
						+ "Gave "
						+ StringUtils.yellowify("" + amount, ChatColor.YELLOW)
						+ StringUtils.yellowify(
								" " + IconomyInterface.getCurrency(),
								ChatColor.GREEN)
						+ "(s) to "
						+ StringUtils.yellowify(npc.getStrippedName(),
								ChatColor.GREEN)
						+ ". Your balance is now "
						+ StringUtils.yellowify(
								""
										+ IconomyInterface.getBalance(player
												.getName()), ChatColor.GREEN)
						+ ".");
			} else {
				player.sendMessage(ChatColor.RED
						+ "You don't have enough money for that! Need "
						+ StringUtils.yellowify(
								""
										+ (amount - IconomyInterface
												.getBalance(player.getName())),
								ChatColor.RED)
						+ " more "
						+ StringUtils.yellowify(IconomyInterface.getCurrency(),
								ChatColor.RED) + "(s).");
			}
		} else if (args[1].equals("take")) {
			if (npc.getTraderNPC().getBalance() - amount > 0) {
				npc.getTraderNPC().setBalance(
						npc.getTraderNPC().getBalance() - amount);
				IconomyInterface.give(player, amount);
				player.sendMessage(ChatColor.GREEN
						+ "Took "
						+ StringUtils.yellowify("" + amount, ChatColor.YELLOW)
						+ StringUtils.yellowify(
								" " + IconomyInterface.getCurrency(),
								ChatColor.GREEN)
						+ "(s) from "
						+ StringUtils.yellowify(npc.getStrippedName(),
								ChatColor.GREEN)
						+ ". Your balance is now "
						+ StringUtils.yellowify(
								""
										+ IconomyInterface.getBalance(player
												.getName()), ChatColor.GREEN)
						+ ".");
			} else {
				player.sendMessage(ChatColor.RED
						+ "The NPC doesn't have enough money for that! It needs "
						+ StringUtils.yellowify(""
								+ (amount - npc.getTraderNPC().getBalance()),
								ChatColor.RED)
						+ " more "
						+ StringUtils.yellowify(IconomyInterface.getCurrency(),
								ChatColor.RED) + "(s).");
			}
		} else
			player.sendMessage(ChatColor.RED + "Invalid argument type "
					+ StringUtils.yellowify(args[1], ChatColor.RED) + ".");
	}
}
