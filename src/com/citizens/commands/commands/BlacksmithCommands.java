package com.citizens.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.commands.CommandHandler;
import com.citizens.economy.EconomyManager;
import com.citizens.economy.EconomyOperation;
import com.citizens.economy.ItemInterface;
import com.citizens.economy.ServerEconomyInterface;
import com.citizens.npctypes.blacksmiths.BlacksmithManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.resources.sk89q.ServerCommand;
import com.citizens.utils.HelpUtils;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.Messaging;
import com.citizens.utils.StringUtils;

@CommandRequirements(requiredType = "blacksmith")
public class BlacksmithCommands implements CommandHandler {

	@CommandRequirements()
	@Command(
			aliases = "blacksmith",
			usage = "help",
			desc = "view the blacksmith help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("blacksmith.use.help")
	@ServerCommand()
	public static void blacksmithHelp(CommandContext args,
			CommandSender sender, HumanNPC npc) {
		HelpUtils.sendBlacksmithHelp(sender);
	}

	@Command(
			aliases = "blacksmith",
			usage = "status",
			desc = "view the status of your in-hand item",
			modifiers = "status",
			min = 1,
			max = 1)
	@CommandPermissions("blacksmith.use.status")
	public static void cost(CommandContext args, Player player, HumanNPC npc) {
		ItemStack item = player.getItemInHand();
		String op = "";
		if (BlacksmithManager.validateArmor(item)) {
			op = "blacksmith-armorrepair";
		} else if (BlacksmithManager.validateTool(item)) {
			op = "blacksmith-toolrepair";
		}
		if (op.isEmpty()) {
			Messaging.sendError(player,
					MessageUtils.getMaterialName(item.getTypeId())
							+ " is not a repairable item.");
			return;
		}
		EconomyOperation ecoOp = EconomyManager.getOperation(op);
		if (EconomyManager.useEconomy()) {
			double price = ItemInterface.getBlacksmithPrice(ecoOp, player);
			if (EconomyManager.useEconPlugin()) {
				price = ServerEconomyInterface
						.getBlacksmithPrice(player, ecoOp);
			}
			player.sendMessage(ChatColor.GREEN
					+ "Item: "
					+ StringUtils.wrap(MessageUtils.getMaterialName(item
							.getTypeId())));
			player.sendMessage(ChatColor.GREEN
					+ "Cost: "
					+ StringUtils.wrap(EconomyManager.getPaymentType(player,
							ecoOp, "" + price)));
			player.sendMessage(ChatColor.GREEN
					+ "Durability Remaining: "
					+ StringUtils.wrap(Material.getMaterial(item.getTypeId())
							.getMaxDurability() - item.getDurability()));
		} else {
			Messaging.sendError(player,
					"This server is not using an economy system.");
		}
	}
}