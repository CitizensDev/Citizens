package net.citizensnpcs.traders;

import java.util.List;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.npctypes.NPCTypeManager;
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
	public static final TraderCommands INSTANCE = new TraderCommands();

	private TraderCommands() {
	}

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
		NPCTypeManager.getType("trader").getCommands().sendHelpPage(sender);
	}

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
				Messaging.sendError(player,
						"Invalid balance change amount entered.");
				return;
			}
			String keyword = "Took ";
			if (args.getString(1).contains("g")) {
				if (EconomyManager.hasEnough(player, amount)) {
					keyword = "Gave ";
					EconomyManager.pay(npc, -amount);
					EconomyManager.pay(player, amount);
				} else {
					player.sendMessage(ChatColor.RED
							+ "You don't have enough money for that! Need "
							+ StringUtils.wrap(
									EconomyManager.format(amount
											- EconomyManager.getBalance(player
													.getName())), ChatColor.RED)
							+ " more.");
					return;
				}
			} else if (args.getString(1).contains("t")) {
				if (EconomyManager.hasEnough(npc, amount)) {
					EconomyManager.pay(npc, amount);
					EconomyManager.pay(player, -amount);
				} else {
					player.sendMessage(ChatColor.RED
							+ "The trader doesn't have enough money for that! It needs "
							+ StringUtils.wrap(
									EconomyManager.format(amount
											- npc.getBalance()), ChatColor.RED)
							+ " more in its balance.");
					return;
				}
			} else {
				player.sendMessage(ChatColor.RED + "Invalid argument type "
						+ StringUtils.wrap(args.getString(1), ChatColor.RED)
						+ ".");
				return;
			}
			player.sendMessage(ChatColor.GREEN
					+ keyword
					+ StringUtils.wrap(EconomyManager.format(amount))
					+ " to "
					+ StringUtils.wrap(npc.getStrippedName())
					+ ". Your balance is now "
					+ StringUtils.wrap(EconomyManager
							.getFormattedBalance(player.getName())) + ".");
			break;
		default:
			Messaging.sendError(player, "Incorrect syntax. See /trader help");
			break;
		}
	}

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
			Messaging.sendError(player, "Not a valid list type.");
			return;
		}
		boolean selling = args.getString(1).contains("s");
		Trader trader = npc.getType("trader");
		List<Stockable> stock = trader.getStockables(!selling);
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

	@Command(
			aliases = "trader",
			usage = "buy|sell [item] [price]",
			desc = "change the stock of a trader",
			modifiers = { "buy", "sell" },
			min = 3,
			max = 4)
	@CommandPermissions("trader.modify.stock")
	public static void stock(CommandContext args, Player player, HumanNPC npc) {
		// TODO this is horrible, clean it up
		String item = args.getString(1), price = args.getString(2);
		boolean selling = args.getString(0).contains("bu");
		Trader trader = npc.getType("trader");
		String keyword = "buying";
		if (!selling) {
			keyword = "selling";
		}

		if (args.argsLength() == 3 && item.contains("edit")) {
			ItemStack stack = parseItemStack(player, price.split(":"));
			if (stack == null)
				return;
			if (trader.getStockable(stack.getTypeId(), stack.getDurability(),
					selling) == null) {
				Messaging.sendError(player, "The trader is not currently "
						+ keyword + " that item.");
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
			ItemStack stack = parseItemStack(player, price.split(":"));
			if (stack == null)
				return;
			if (trader.getStockable(stack.getTypeId(), stack.getDurability(),
					selling) == null) {
				Messaging.sendError(player, "The trader is not currently "
						+ keyword + " that item.");
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
		ItemStack stack = parseItemStack(player, item.split(":"));
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
			modifiers = "clear",
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
		if (Double.parseDouble(price) < 0) {
			player.sendMessage(ChatColor.GRAY
					+ "Negative prices are not allowed.");
			return null;
		}
		return new ItemPrice(Double.parseDouble(price));
	}

	private static ItemStack parseItemStack(Player player, String[] split) {
		try {
			int amount = 1;
			short data = 0;
			Material mat = StringUtils.parseMaterial(split[0]);
			if (mat == null) {
				player.sendMessage(ChatColor.GRAY
						+ "Invalid material ID or name specified.");
				return null;
			}
			switch (split.length) {
			case 3:
				data = Short.parseShort(split[2]);
			case 2:
				amount = Integer.parseInt(split[1]);
				if (amount <= 0 || amount > 64) {
					player.sendMessage(ChatColor.GRAY
							+ "You entered an invalid amount.");
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
		PermissionManager.addPermission("trader.use.help");
		PermissionManager.addPermission("trader.use.showmoney");
		PermissionManager.addPermission("trader.modify.money");
		PermissionManager.addPermission("trader.use.list");
		PermissionManager.addPermission("trader.modify.unlimited");
		PermissionManager.addPermission("trader.modify.clearstock");
		PermissionManager.addPermission("trader.modify.stock");
		PermissionManager.addPermission("trader.use.trade");
	}

	@Override
	public void sendHelpPage(CommandSender sender) {
		HelpUtils.header(sender, "Trader", 1, 1);
		HelpUtils.format(sender, "trader", "list [buy|sell] (page)",
				"list a trader's buy/sell list");
		HelpUtils.format(sender, "trader",
				"[buy|sell] [itemID(:amount:data)] [price]",
				"add an item to a trader's stock");
		HelpUtils.format(sender, "trader", "[buy|sell] remove [itemID:data]",
				"remove item from a trader's stock");
		HelpUtils.format(sender, "trader",
				"[buy|sell] edit [itemID(:amount:data)] [price]",
				"edit a trader's stock");
		HelpUtils.format(sender, "trader", "unlimited",
				"set whether a trader has unlimited stock");
		HelpUtils.format(sender, "trader", "money (give|take) (amount)",
				"control a trader's money");
		HelpUtils.format(sender, "trader", "clear [buy|sell]",
				"clear a trader's stock");
		HelpUtils.footer(sender);
	}
}