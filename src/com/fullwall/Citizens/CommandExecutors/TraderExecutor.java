package com.fullwall.Citizens.CommandExecutors;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.IconomyInterface;
import com.fullwall.Citizens.Economy.Payment;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Traders.ItemPrice;
import com.fullwall.Citizens.Traders.Stockable;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderExecutor implements CommandExecutor {

	@SuppressWarnings("unused")
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
		if (!npc.isTrader()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a trader yet.");
			return true;
		} else {
			if (args.length == 3 && args[0].equals("balance")) {
				if (BasicExecutor.hasPermission("citizens.trader.balance",
						sender)) {
					try {
						if (!EconomyHandler.useIconomy())
							player.sendMessage(ChatColor.GRAY
									+ "This server is not using an economy plugin.");
						else
							changeBalance(player, npc, args);
					} catch (NumberFormatException e) {
						player.sendMessage(ChatColor.RED
								+ "Invalid balance change amount entered.");
					}
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			} else if (args.length == 3
					&& (args[0].contains("b") || args[0].contains("s"))) {
				if (BasicExecutor
						.hasPermission("citizens.trader.stock", sender)) {
					changeTraderBuySell(player, npc, args[1], args[2],
							args[0].contains("s"));
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			} else if (args.length == 2 && (args[0].contains("unl"))) {
				if (BasicExecutor.hasPermission(
						"citizens.admin.unlimitedtrader", sender)) {
					changeUnlimited(npc, sender, args[1]);
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			} else if (args.length == 3 && args[0].contains("list")
					&& (args[1].contains("s") || args[1].contains("b"))) {
				if (BasicExecutor
						.hasPermission("citizens.trader.stock", sender)) {
					displayList(player, npc, args, args[1].contains("s"));
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			}
			TraderPropertyPool.saveTraderState(npc);
		}
		return returnval;
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
		ArrayList<Stockable> stock = npc.getTraderNPC().getStockables(selling);
		String keyword = "";
		if (selling)
			keyword = "Selling";
		else
			keyword = "Buying";
		// have to paginate.
		int page = 0;
		if (stock.size() > 4 && args.length == 3) {
			page = Integer.parseInt(args[2]);
		} else {
			player.sendMessage(ChatColor.GOLD + "NPC " + keyword
					+ " List (Page "
					+ StringUtils.yellowify(page, ChatColor.GOLD) + " of "
					+ StringUtils.yellowify(stock.size() / 4, ChatColor.YELLOW)
					+ ")");
			player.sendMessage(ChatColor.AQUA
					+ "-------------------------------");
			int startPoint = 4 * page - 1;
			for (int i = startPoint; i != startPoint + 3; ++i) {
				Stockable s = stock.get(i);
			}
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
		if (unlimited.equals("true") || unlimited.equals("on")) {
			npc.getTraderNPC().setUnlimited(true);
			sender.sendMessage(ChatColor.GREEN
					+ "The trader will now have unlimited stock!");
		} else if (unlimited.equals("false") || unlimited.equals("off")) {
			npc.getTraderNPC().setUnlimited(false);
			sender.sendMessage(ChatColor.GREEN
					+ "The trader has stopped having unlimited stock.");
		} else
			sender.sendMessage(ChatColor.GREEN
					+ "Incorrect unlimited type entered. Valid values are true, on, false, off.");
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
	private void changeTraderBuySell(Player player, HumanNPC npc, String item,
			String price, boolean selling) {
		if (item.contains("rem")) {
			Material mat = StringUtils.parseMaterial(price);
			if (mat == null) {
				player.sendMessage(ChatColor.RED
						+ "Invalid item ID or name specified.");
				return;
			}
			if (selling) {
				if (npc.getTraderNPC().getStockable(mat.getId(), true) == null) {
					player.sendMessage(ChatColor.RED
							+ "The NPC is not currently selling that item.");
					return;
				} else {
					npc.traderNPC.removeStockable(mat.getId(), true);
					player.sendMessage(ChatColor.GREEN + "Removed "
							+ StringUtils.yellowify(mat.name())
							+ " from the NPC's selling list.");
				}
			} else {
				if (npc.getTraderNPC().getStockable(mat.getId(), false) == null) {
					player.sendMessage(ChatColor.RED
							+ "The NPC is not currently buying that item.");
					return;
				} else {
					npc.traderNPC.removeStockable(mat.getId(), false);
					player.sendMessage(ChatColor.GREEN + "Removed "
							+ StringUtils.yellowify(mat.name())
							+ " from the NPC's buying list.");
				}
			}
			return;
		}
		String[] split = item.split(":");
		ItemStack stack = createItemStack(split);
		if (stack == null) {
			player.sendMessage(ChatColor.RED
					+ "Invalid item ID or name specified.");
			return;
		}
		split = price.split(":");
		ItemStack cost = null;
		if (split.length != 1) {
			cost = createItemStack(split);
			if (cost == null) {
				player.sendMessage(ChatColor.RED
						+ "Invalid item ID or name specified.");
				return;
			}
		}
		int data = Citizens.MAGIC_DATA_VALUE;
		if (stack.getData() != null)
			data = stack.getData().getData();
		ItemPrice itemPrice = new ItemPrice(stack.getAmount(),
				stack.getTypeId(), data);
		itemPrice.setiConomy(cost == null);
		Stockable s = new Stockable(stack, itemPrice, false);
		if (selling) {
			s.setSelling(true);
			npc.getTraderNPC().addStockable(s);
			player.sendMessage(ChatColor.GREEN + "The NPC is now selling "
					+ MessageUtils.getStockableMessage(s, ChatColor.GREEN)
					+ ".");
		} else {
			npc.getTraderNPC().addStockable(s);
			player.sendMessage(ChatColor.GREEN + "The NPC is now buying "
					+ MessageUtils.getStockableMessage(s, ChatColor.GREEN)
					+ ".");
		}
	}

	/**
	 * Creates an ItemStack from the given string ItemStack format.
	 * 
	 * @param split
	 * @return
	 */
	private ItemStack createItemStack(String[] split) {
		int amount = 1;
		int data = 0;
		Material mat = StringUtils.parseMaterial(split[0]);
		if (mat == null) {
			return null;
		}
		if (split.length > 1)
			amount = Integer.parseInt(split[1]);
		if (split.length > 2)
			data = Integer.parseInt(split[2]);
		ItemStack stack = new ItemStack(mat, amount);
		if (data > 0) {
			MaterialData mdata = new MaterialData(data);
			stack.setData(mdata);
		}
		return stack;
	}

	/**
	 * Alters the selected trader's balance depending on the arguments given.
	 * Balance is used for iConomy.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 * @throws NumberFormatException
	 */
	private void changeBalance(Player player, HumanNPC npc, String[] args)
			throws NumberFormatException {
		int amount = Integer.valueOf(args[2]);
		if (args[1].equals("give")) {
			if (EconomyHandler.canBuy(new Payment(amount, true), player)) {
				EconomyHandler.pay(new Payment(-amount, true), npc);
				EconomyHandler.pay(new Payment(amount, true), player);
				player.sendMessage(ChatColor.GREEN
						+ "Gave "
						+ StringUtils.yellowify(IconomyInterface
								.getCurrency(amount))
						+ " to "
						+ StringUtils.yellowify(npc.getStrippedName())
						+ ". Your balance is now "
						+ StringUtils.yellowify(
								IconomyInterface.getBalance(player.getName()),
								ChatColor.GREEN) + ".");
			} else {
				player.sendMessage(ChatColor.RED
						+ "You don't have enough money for that! Need "
						+ " "
						+ StringUtils.yellowify(
								IconomyInterface.getCurrency(amount
										- IconomyInterface.getBalance(player
												.getName())), ChatColor.RED)
						+ " more.");
			}
		} else if (args[1].equals("take")) {
			if (EconomyHandler.canBuy(new Payment(amount, true), npc)) {
				EconomyHandler.pay(new Payment(amount, true), npc);
				EconomyHandler.pay(new Payment(-amount, true), player);
				player.sendMessage(ChatColor.GREEN
						+ "Took "
						+ StringUtils.yellowify(IconomyInterface
								.getCurrency(amount))
						+ " from "
						+ StringUtils.yellowify(npc.getStrippedName())
						+ ". Your balance is now "
						+ StringUtils.yellowify(IconomyInterface
								.getBalance(player.getName())) + ".");
			} else {
				player.sendMessage(ChatColor.RED
						+ "The NPC doesn't have enough money for that! It needs "
						+ StringUtils.yellowify(
								IconomyInterface.getCurrency(amount
										- npc.getTraderNPC().getBalance()),
								ChatColor.RED) + " more in its balance.");
			}
		} else
			player.sendMessage(ChatColor.RED + "Invalid argument type "
					+ StringUtils.yellowify(args[1], ChatColor.RED) + ".");
	}
}
