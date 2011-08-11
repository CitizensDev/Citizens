package net.citizensnpcs.traders;

import java.util.ArrayList;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.resources.sk89q.ServerCommand;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PageUtils;
import net.citizensnpcs.utils.PageUtils.PageInstance;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "trader")
public class TraderCommands extends CommandHandler {

	@CommandRequirements()
	@Command(
			aliases = "trader",
			usage = "help",
			desc = "view the trader help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("trader.use.help")
	@ServerCommand()
	public static void traderHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		HelpUtils.sendHelp((Trader) npc.getType("trader"), sender, 1);
	}

	/**
	 * Display a trader's balance
	 * 
	 * @param player
	 * @param npc
	 */
	@Command(
			aliases = "trader",
			usage = "money (give|take) (amount)",
			desc = "control a trader's balance",
			modifiers = "money",
			min = 1,
			max = 3)
	public static void money(CommandContext args, Player player, HumanNPC npc) {
		if (!EconomyManager.useEconPlugin()) {
			player.sendMessage(MessageUtils.noEconomyMessage);
			return;
		}
		switch (args.argsLength()) {
		case 1:
			if (PermissionManager.generic(player,
					"citizens.trader.use.showmoney")) {
				player.sendMessage(StringUtils.wrap(npc.getName())
						+ " has "
						+ StringUtils.wrap(EconomyManager.format(npc
								.getBalance())) + ".");
			} else {
				player.sendMessage(MessageUtils.noPermissionsMessage);
			}
			break;
		case 3:
			if (!PermissionManager.generic(player,
					"citizens.trader.modify.money")) {
				player.sendMessage(MessageUtils.noPermissionsMessage);
				return;
			}
			double amount;
			try {
				amount = Double.parseDouble(args.getString(2));
			} catch (NumberFormatException e) {
				player.sendMessage(ChatColor.RED
						+ "Invalid balance change amount entered.");
				return;
			}
			/*
			 * if (args.getString(1).contains("g")) { if
			 * (EconomyManager.canBuy(new Payment(amount, true), player)) {
			 * EconomyManager.pay(new Payment(-amount, true), npc, -1);
			 * EconomyManager.pay(new Payment(amount, true), player, -1);
			 * player.sendMessage(ChatColor.GREEN + "Gave " +
			 * StringUtils.wrap(ServerEconomyInterface .format(amount)) + " to "
			 * + StringUtils.wrap(npc.getStrippedName()) +
			 * ". Your balance is now " +
			 * StringUtils.wrap(ServerEconomyInterface
			 * .getFormattedBalance(player.getName()), ChatColor.GREEN) + ".");
			 * } else { player.sendMessage(ChatColor.RED +
			 * "You don't have enough money for that! Need " +
			 * StringUtils.wrap(ServerEconomyInterface .format(amount -
			 * ServerEconomyInterface .getBalance(player .getName())),
			 * ChatColor.RED) + " more."); } } else if
			 * (args.getString(1).contains("t")) { if (EconomyManager.canBuy(new
			 * Payment(amount, true), npc)) { EconomyManager.pay(new
			 * Payment(amount, true), npc, -1); EconomyManager.pay(new
			 * Payment(-amount, true), player, -1);
			 * player.sendMessage(ChatColor.GREEN + "Took " +
			 * StringUtils.wrap(ServerEconomyInterface .format(amount)) +
			 * " from " + StringUtils.wrap(npc.getStrippedName()) +
			 * ". Your balance is now " +
			 * StringUtils.wrap(ServerEconomyInterface
			 * .getFormattedBalance(player.getName())) + "."); } else {
			 * player.sendMessage(ChatColor.RED +
			 * "The trader doesn't have enough money for that! It needs " +
			 * StringUtils.wrap( ServerEconomyInterface.format(amount -
			 * npc.getBalance()), ChatColor.RED) + " more in its balance."); } }
			 * else { player.sendMessage(ChatColor.RED +
			 * "Invalid argument type " + StringUtils.wrap(args.getString(1),
			 * ChatColor.RED) + "."); }
			 */
			break;
		default:
			Messaging.sendError(player, "Incorrect syntax. See /trader help");
			break;
		}
	}

	/**
	 * Displays a list of buying/selling items for the selected trader npc.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 * @param selling
	 */
	@CommandRequirements(requiredType = "trader", requireSelected = true)
	@Command(
			aliases = "trader",
			usage = "list [buy|sell]",
			desc = "view a trader's buying/selling list",
			modifiers = "list",
			min = 2,
			max = 3)
	@CommandPermissions("trader.use.list")
	public static void list(CommandContext args, Player player, HumanNPC npc) {
		if (!args.getString(1).contains("s")
				&& !args.getString(1).contains("b")) {
			player.sendMessage(ChatColor.RED + "Not a valid list type.");
			return;
		}
		boolean selling = args.getString(1).contains("s");
		Trader trader = npc.getType("trader");
		ArrayList<Stockable> stock = trader.getStockables(!selling);
		int page = 1;
		if (args.argsLength() == 3)
			page = args.getInteger(2);
		String keyword = "Buying ";
		if (selling)
			keyword = "Selling ";
		if (stock.size() == 0) {
			player.sendMessage(ChatColor.GRAY + "This trader isn't "
					+ keyword.toLowerCase() + "any items.");
			return;
		}
		PageInstance instance = PageUtils.newInstance(player);
		instance.push("");
		for (Stockable stockable : stock) {
			if (stockable == null)
				continue;
			instance.push(ChatColor.GREEN
					+ keyword
					+ TraderMessageUtils.getStockableMessage(stockable,
							ChatColor.GREEN) + ".");
		}
		if (page <= instance.maxPages()) {
			instance.header(ChatColor.YELLOW
					+ StringUtils.listify(ChatColor.GREEN + "Trader "
							+ StringUtils.wrap(keyword) + "List (Page %x/%y)"
							+ ChatColor.YELLOW));
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
	@Command(
			aliases = "trader",
			usage = "unlimited",
			desc = "change the unlimited status of a trader",
			modifiers = { "unlimited", "unlim", "unl" },
			min = 1,
			max = 1)
	@CommandPermissions("trader.modify.unlimited")
	public static void unlimited(CommandContext args, Player player,
			HumanNPC npc) {
		Trader trader = npc.getType("trader");
		trader.setUnlimited(!trader.isUnlimited());
		if (trader.isUnlimited()) {
			player.sendMessage(ChatColor.GREEN
					+ "The trader will now have unlimited stock!");
		} else {
			player.sendMessage(ChatColor.GREEN
					+ "The trader has stopped having unlimited stock.");
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
	@Command(
			aliases = "trader",
			usage = "buy/sell [item] [price]",
			desc = "change the stock of a trader",
			modifiers = { "buy", "sell" },
			min = 3,
			max = 4)
	@CommandPermissions("trader.modify.stock")
	public static void stock(CommandContext args, Player player, HumanNPC npc) {
		// TODO this is horrible, clean it up
		String item = args.getString(1);
		String price = args.getString(2);
		boolean selling = args.getString(0).contains("bu");
		Trader trader = npc.getType("trader");
		String keyword = "buying";
		if (!selling) {
			keyword = "selling";
		}

		if (args.length() == 4 && item.contains("edit")) {
			ItemStack stack = parseItemStack(player, price, false);
			if (stack == null)
				return;
			if (trader.getStockable(stack.getTypeId(), stack.getDurability(),
					selling) == null) {
				player.sendMessage(ChatColor.RED
						+ "The trader is not currently " + keyword
						+ " that item.");
				return;
			} else {
				String cost = args.getString(3);
				trader.getStockable(stack.getTypeId(), stack.getDurability(),
						selling).setPrice(createItemPrice(player, cost));
				player.sendMessage(ChatColor.GREEN
						+ "Edited "
						+ StringUtils.wrap(StringUtils.capitalise(stack
								.getType().name().toLowerCase())) + "'s price.");
			}
			return;
		}
		if (item.contains("rem")) {
			ItemStack stack = parseItemStack(player, price, false);
			if (stack == null)
				return;
			if (trader.getStockable(stack.getTypeId(), stack.getDurability(),
					selling) == null) {
				player.sendMessage(ChatColor.RED
						+ "The trader is not currently " + keyword
						+ " that item.");
				return;
			} else {
				trader.removeStockable(stack.getTypeId(),
						stack.getDurability(), selling);
				player.sendMessage(ChatColor.GREEN + "Removed "
						+ StringUtils.wrap(stack.getType().name())
						+ " from the trader's " + keyword + " list.");
			}
			return;
		}
		if (item.contains("clear")) {
			int count = 0;
			for (Check check : trader.getStocking().keySet()) {
				if (check.isSelling() == selling) {
					trader.removeStockable(check);
					++count;
				}
			}
			player.sendMessage(ChatColor.GREEN + "Cleared "
					+ StringUtils.wrap(count)
					+ StringUtils.pluralise(" item", count)
					+ " from the trader's " + StringUtils.wrap(keyword)
					+ " list.");
			return;
		}
		selling = !selling;
		ItemStack stack = parseItemStack(player, item, false);
		if (stack == null)
			return;
		ItemPrice itemPrice = createItemPrice(player, price);
		if (itemPrice == null)
			return;
		Stockable s = new Stockable(stack, itemPrice, true);
		keyword = "buying";
		if (selling) {
			keyword = "selling";
			s.setSelling(false);
		}
		if (trader.isStocked(s)) {
			player.sendMessage(ChatColor.RED
					+ "Already "
					+ keyword
					+ " that at "
					+ TraderMessageUtils.getStockableMessage(
							trader.getStockable(s), ChatColor.RED) + ".");
			return;
		}
		trader.addStockable(s);
		player.sendMessage(ChatColor.GREEN + "The trader is now " + keyword
				+ " "
				+ TraderMessageUtils.getStockableMessage(s, ChatColor.GREEN)
				+ ".");
	}

	@Command(
			aliases = "trader",
			usage = "clear [buy|sell]",
			desc = "clear the stock of a trader",
			modifiers = { "clear" },
			min = 2,
			max = 2)
	@CommandPermissions("trader.modify.clearstock")
	public static void clear(CommandContext args, Player player, HumanNPC npc) {
		boolean selling = args.getString(1).contains("bu");
		Trader trader = npc.getType("trader");
		String keyword = "buying";
		if (!selling) {
			keyword = "selling";
		}
		int count = 0;
		for (Check check : trader.getStocking().keySet()) {
			if (check.isSelling() == selling) {
				trader.removeStockable(check);
				++count;
			}
		}
		player.sendMessage(ChatColor.GREEN + "Cleared "
				+ StringUtils.wrap(count)
				+ StringUtils.pluralise(" item", count) + " from the trader's "
				+ StringUtils.wrap(keyword) + " list.");
		return;
	}

	private static ItemPrice createItemPrice(Player player, String price) {
		ItemStack cost = parseItemStack(player, price, true);
		boolean econPlugin = false;
		if (cost == null) {
			econPlugin = true;
		}
		ItemPrice itemPrice;
		if (!econPlugin) {
			itemPrice = new ItemPrice(cost);
		} else {
			if (Double.parseDouble(price) < 0) {
				player.sendMessage(ChatColor.GRAY
						+ "Negative prices are not allowed.");
				return null;
			}
			itemPrice = new ItemPrice(Double.parseDouble(price));
		}
		itemPrice.setEconPlugin(econPlugin);
		return itemPrice;
	}

	private static ItemStack parseItemStack(Player player, String item,
			boolean price) {
		String[] split = item.split(":");
		ItemStack stack = null;
		if ((price && split.length != 1) || !price) {
			stack = parseItemStack(split);
			if (!price && stack == null) {
				player.sendMessage(ChatColor.RED
						+ "Invalid item ID or name specified.");
			}
		}
		if (price && stack == null && !EconomyManager.useEconPlugin()) {
			player.sendMessage(ChatColor.GRAY
					+ "This server is not using an economy plugin, so the price cannot be "
					+ "that kind of value. If you meant to use an item as currency, "
					+ "please format it like so: item ID:amount(:data).");
			return null;
		}
		return stack;
	}

	/**
	 * Creates an ItemStack from the given string ItemStack format.
	 * 
	 * @param split
	 * @return
	 */
	private static ItemStack parseItemStack(String[] split) {
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
				if (amount <= 0 || amount > 64) {
					return null;
				}
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

	@Override
	public void addPermissions() {
		PermissionManager.addPerm("trader.use.help");
		PermissionManager.addPerm("trader.use.showmoney");
		PermissionManager.addPerm("trader.modify.money");
		PermissionManager.addPerm("trader.use.list");
		PermissionManager.addPerm("trader.modify.unlimited");
		PermissionManager.addPerm("trader.modify.clearstock");
		PermissionManager.addPerm("trader.modify.stock");
		PermissionManager.addPerm("trader.use.trade");
	}

	@Override
	public void sendHelp(CommandSender sender, int page) {
		HelpUtils.header(sender, "Trader", 1, 1);
		HelpUtils.format(sender, "trader", "list [buy|sell] (page)",
				"list a trader's buy/sell list");
		HelpUtils.format(sender, "trader",
				"[buy|sell] [itemID(:amount:data)] [itemID(:amount:data)]",
				"add an item to a trader's stock");
		HelpUtils.format(sender, "trader", "[buy|sell] remove [itemID:data]",
				"remove item from a trader's stock");
		HelpUtils
				.format(sender,
						"trader",
						"[buy|sell] edit [itemID(:amount:data)] [itemID(:amount:data)]",
						"edit a trader's stock");
		HelpUtils.format(sender, "trader", "unlimited [true|false]",
				"set whether a trader has unlimited stock");
		HelpUtils.format(sender, "trader", "money [give|take] (amount)",
				"control a trader's money");
		HelpUtils.format(sender, "trader", "clear [buy|sell]",
				"clear a trader's stock");
		HelpUtils.footer(sender);
	}
}