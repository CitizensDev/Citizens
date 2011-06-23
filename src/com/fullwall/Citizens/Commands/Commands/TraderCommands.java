package com.fullwall.Citizens.Commands.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
//import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.Payment;
import com.fullwall.Citizens.Economy.ServerEconomyInterface;
import com.fullwall.Citizens.NPCTypes.Traders.ItemPrice;
import com.fullwall.Citizens.NPCTypes.Traders.Stockable;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.PageUtils;
import com.fullwall.Citizens.Utils.PageUtils.PageInstance;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandRequirements;

public class TraderCommands {
	
	@CommandRequirements(
			requiredType = "trader")
	@Command(
			aliases = "trader",
			usage = "help",
			desc = "view the trader help page",
			modifier = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.trader")
	public static void sendTraderHelp(CommandContext args, Player player, HumanNPC npc) {
		HelpUtils.sendTraderHelp(player);
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
		boolean returnval = false;
		if (NPCManager.validateSelected((Player) sender)) {
			npc = NPCManager.get(NPCManager.selectedNPCs.get(player.getName()));
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!npc.isTrader()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a trader yet.");
			return true;
		}
		if (args[0].equalsIgnoreCase("help")) {
			if (Permission.canUse(player, npc, "trader")) {
				HelpUtils.sendTraderHelp(sender);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if (args.length >= 2 && args[0].contains("list")
				&& (args[1].contains("s") || args[1].contains("b"))) {
			if (Permission.canUse(player, npc, "trader")) {
				displayList(player, npc, args, args[1].contains("s"));
			} else {
				player.sendMessage(MessageUtils.noPermissionsMessage);
			}
			returnval = true;
		} else if (args.length == 1 && args[0].contains("mon")) {
			if (Permission.canUse(player, npc, "trader")) {
				if (!EconomyHandler.useEcoPlugin())
					player.sendMessage(MessageUtils.noEconomyMessage);
				else
					displayMoney(player, npc);
			} else
				player.sendMessage(MessageUtils.noPermissionsMessage);
			returnval = true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			if (!returnval) {
				player.sendMessage(MessageUtils.notOwnerMessage);
			}
			return true;
		} else {
			if (args.length == 3 && args[0].contains("mon")) {
				if (Permission.canModify(player, npc, "trader")) {
					if (!EconomyHandler.useEcoPlugin())
						player.sendMessage(MessageUtils.noEconomyMessage);
					else
						changeBalance(player, npc, args);
				} else {
					player.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 3
					&& (args[0].contains("bu") || args[0].contains("sel"))) {
				if (Permission.canModify(player, npc, "trader")) {
					changeTraderStock(player, npc, args[1], args[2],
							args[0].contains("bu"));
				} else {
					player.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 2 && (args[0].contains("unl"))) {
				if (Permission.isAdmin(player)) {
					changeUnlimited(npc, sender, args[1]);
				} else {
					player.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			PropertyManager.save(npc);
		}
		return returnval;
	}

	/**
	 * Display a trader's balance
	 * 
	 * @param player
	 * @param npc
	 */
	private void displayMoney(Player player, HumanNPC npc) {
		player.sendMessage(StringUtils.wrap(npc.getName())
				+ " has "
				+ StringUtils.wrap(ServerEconomyInterface.format(npc
						.getBalance())) + ".");
	}

	/**
	 * Displays a list of buying/selling items for the selected trader npc.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 * @param selling
	 */
	private void displayList(Player player, HumanNPC npc, String[] args,
			boolean selling) {
		ArrayList<Stockable> stock = npc.getTrader().getStockables(!selling);
		int page = 1;
		String keyword = "Buying ";
		if (selling)
			keyword = "Selling ";
		if (stock.size() == 0) {
			player.sendMessage(ChatColor.GRAY + "This trader isn't "
					+ keyword.toLowerCase() + "any items.");
			return;
		}
		PageInstance instance = PageUtils.newInstance(player);
		for (Stockable stockable : stock) {
			if (stockable == null)
				continue;
			instance.push(ChatColor.GREEN
					+ keyword
					+ ": "
					+ MessageUtils.getStockableMessage(stockable,
							ChatColor.GREEN) + ".");
		}
		if (page <= instance.maxPages()) {
			instance.header(ChatColor.GOLD + "========== Trader " + keyword
					+ "List (Page %x/%y) ==========");
			instance.process(page);
		} else {
			player.sendMessage(MessageUtils.getMaxPagesMessage(page,
					instance.maxPages()));
		}
	}

	/**
	 * Sets whether the selected trader will have unlimited stock or not.
	 * 
	 * @param npc
	 * @param sender
	 * @param unlimited
	 */
	private void changeUnlimited(HumanNPC npc, CommandSender sender,
			String unlimited) {
		if (unlimited.equalsIgnoreCase("true")
				|| unlimited.equalsIgnoreCase("on")) {
			npc.getTrader().setUnlimited(true);
			sender.sendMessage(ChatColor.GREEN
					+ "The trader will now have unlimited stock!");
		} else if (unlimited.equalsIgnoreCase("false")
				|| unlimited.equalsIgnoreCase("off")) {
			npc.getTrader().setUnlimited(false);
			sender.sendMessage(ChatColor.GREEN
					+ "The trader has stopped having unlimited stock.");
		} else {
			sender.sendMessage(ChatColor.GREEN
					+ "Incorrect unlimited type entered. Valid values are true, on, false, off.");
		}
	}

	/**
	 * Adds an item to be stocked by the selected trader.
	 * 
	 * @param player
	 * @param npc
	 * @param item
	 * @param price
	 * @param selling
	 */
	private void changeTraderStock(Player player, HumanNPC npc, String item,
			String price, boolean selling) {
		if (item.contains("rem")) {
			ItemStack stack = parseItemStack(price.split(":"));
			if (stack == null) {
				player.sendMessage(ChatColor.RED
						+ "Invalid item ID or name specified.");
				return;
			}
			String keyword = "buying";
			if (!selling) {
				keyword = "selling";
			}
			if (npc.getTrader().getStockable(stack.getTypeId(),
					stack.getDurability(), selling) == null) {
				player.sendMessage(ChatColor.RED
						+ "The trader is not currently " + keyword
						+ " that item.");
				return;
			} else {
				npc.getTrader().removeStockable(stack.getTypeId(),
						stack.getDurability(), selling);
				player.sendMessage(ChatColor.GREEN + "Removed "
						+ StringUtils.wrap(stack.getType().name())
						+ " from the trader's " + keyword + " list.");
			}
			return;
		}
		selling = !selling;
		String[] split = item.split(":");
		ItemStack stack = parseItemStack(split);
		if (stack == null) {
			player.sendMessage(ChatColor.RED
					+ "Invalid item ID or name specified.");
			return;
		}
		split = price.split(":");
		ItemStack cost = null;
		if (split.length != 1) {
			cost = parseItemStack(split);
			if (cost == null) {
				player.sendMessage(ChatColor.RED
						+ "Invalid item ID or name specified.");
				return;
			}
		}
		if (cost == null && !EconomyHandler.useEcoPlugin()) {
			player.sendMessage(ChatColor.GRAY
					+ "This server is not using an economy plugin, so the price cannot be "
					+ "that kind of value. If you meant to use an item as currency, "
					+ "please format it in this format: item ID:amount(:data).");
			return;
		}
		boolean iConomy = false;
		if (cost == null) {
			iConomy = true;
		}
		ItemPrice itemPrice;
		if (!iConomy) {
			itemPrice = new ItemPrice(cost);
		} else {
			itemPrice = new ItemPrice(Double.parseDouble(split[0]));
		}
		itemPrice.setiConomy(iConomy);
		Stockable s = new Stockable(stack, itemPrice, true);
		String keyword = "buying";
		if (selling) {
			keyword = "selling";
			s.setSelling(false);
		}
		if (npc.getTrader().isStocked(s)) {
			player.sendMessage(ChatColor.RED
					+ "Already "
					+ keyword
					+ " that at "
					+ MessageUtils.getStockableMessage(npc.getTrader()
							.getStockable(s), ChatColor.RED) + ".");
			return;
		}
		npc.getTrader().addStockable(s);
		player.sendMessage(ChatColor.GREEN + "The trader is now " + keyword
				+ " " + MessageUtils.getStockableMessage(s, ChatColor.GREEN)
				+ ".");
	}

	/**
	 * Creates an ItemStack from the given string ItemStack format.
	 * 
	 * @param split
	 * @return
	 */
	private ItemStack parseItemStack(String[] split) {
		try {
			int amount = 1;
			short data = 0;
			Material mat = StringUtils.parseMaterial(split[0]);
			if (mat == null) {
				return null;
			}
			switch (split.length) {
			case 3:
				data = Short.parseShort(split[2]);
			case 2:
				amount = Integer.parseInt(split[1]);
			default:
				break;
			}
			ItemStack stack = new ItemStack(mat, amount);
			stack.setDurability(data);
			return stack;
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	/**
	 * Alters the selected trader's balance depending on the arguments given.
	 * Balance is used for iConomy.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 */
	private void changeBalance(Player player, HumanNPC npc, String[] args) {
		double amount;
		try {
			amount = Double.parseDouble(args[2]);
		} catch (NumberFormatException e) {
			player.sendMessage(ChatColor.RED
					+ "Invalid balance change amount entered.");
			return;
		}
		if (args[1].contains("g")) {
			if (EconomyHandler.canBuy(new Payment(amount, true), player)) {
				EconomyHandler.pay(new Payment(-amount, true), npc, -1);
				EconomyHandler.pay(new Payment(amount, true), player, -1);
				player.sendMessage(ChatColor.GREEN
						+ "Gave "
						+ StringUtils.wrap(ServerEconomyInterface
								.format(amount))
						+ " to "
						+ StringUtils.wrap(npc.getStrippedName())
						+ ". Your balance is now "
						+ StringUtils.wrap(ServerEconomyInterface
								.getFormattedBalance(player.getName()),
								ChatColor.GREEN) + ".");
			} else {
				player.sendMessage(ChatColor.RED
						+ "You don't have enough money for that! Need "
						+ StringUtils.wrap(
								ServerEconomyInterface.format(amount
										- ServerEconomyInterface
												.getBalance(player.getName())),
								ChatColor.RED) + " more.");
			}
		} else if (args[1].contains("t")) {
			if (EconomyHandler.canBuy(new Payment(amount, true), npc)) {
				EconomyHandler.pay(new Payment(amount, true), npc, -1);
				EconomyHandler.pay(new Payment(-amount, true), player, -1);
				player.sendMessage(ChatColor.GREEN
						+ "Took "
						+ StringUtils.wrap(ServerEconomyInterface
								.format(amount))
						+ " from "
						+ StringUtils.wrap(npc.getStrippedName())
						+ ". Your balance is now "
						+ StringUtils.wrap(ServerEconomyInterface
								.getFormattedBalance(player.getName())) + ".");
			} else {
				player.sendMessage(ChatColor.RED
						+ "The trader doesn't have enough money for that! It needs "
						+ StringUtils.wrap(
								ServerEconomyInterface.format(amount
										- npc.getBalance()), ChatColor.RED)
						+ " more in its balance.");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Invalid argument type "
					+ StringUtils.wrap(args[1], ChatColor.RED) + ".");
		}
	}
}
