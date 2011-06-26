package com.Citizens.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Citizens.Citizens;
import com.Citizens.Properties.Properties.UtilityProperties;
import com.Citizens.Utils.MessageUtils;
import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.Economy.EconomyHandler;
import com.Citizens.Economy.Payment;
import com.Citizens.Economy.EconomyHandler.Operation;
import com.Citizens.resources.nijikokun.register.payment.Method.MethodAccount;

public class ServerEconomyInterface {
	public static final String addendum = ".econplugin";

	/**
	 * Uses the economy-plugin methods to check whether a player has enough in
	 * their account to pay.
	 * 
	 * @param name
	 * @param amount
	 * @return
	 */
	public static boolean playerHasEnough(String name, double amount) {
		return Citizens.economy.hasAccount(name)
				&& Citizens.economy.getAccount(name).hasEnough(amount);
	}

	/**
	 * Gets an economy-plugin balance.
	 * 
	 * @param name
	 * @return
	 */
	public static double getBalance(String name) {
		if (Citizens.economy.hasAccount(name)) {
			return Citizens.economy.getAccount(name).balance();
		}
		return -1;
	}

	public static String getFormattedBalance(String name) {
		return format(getBalance(name));
	}

	/**
	 * Gets the iConomy currency.
	 * 
	 * @param amount
	 * @return
	 */
	public static String format(String amount) {
		return format(Double.parseDouble(amount));
	}

	/**
	 * Gets the economy-plugin currency.
	 * 
	 * @param price
	 * @return
	 */
	public static String format(double price) {
		return Citizens.economy.format(price);
	}

	/**
	 * Gets the remainder necessary for an operation to be completed.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static String getRemainder(Operation op, Player player) {
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum));
		MethodAccount acc = Citizens.economy.getAccount(player.getName());
		return "" + (price - acc.balance());
	}

	/**
	 * Checks whether the player has enough money for an operation.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnough(Player player, Operation op) {
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum));
		return playerHasEnough(player.getName(), price);
	}

	/**
	 * Checks whether the player has enough money for a payment.
	 * 
	 * @param payment
	 * @param player
	 * @return
	 */
	public static boolean hasEnough(Payment payment, Player player) {
		return playerHasEnough(player.getName(), payment.getPrice());
	}

	/**
	 * Checks whether an npc has enough in its balance for a payment.
	 * 
	 * @param payment
	 * @param npc
	 * @return
	 */
	public static boolean hasEnough(Payment payment, HumanNPC npc) {
		return npc.getBalance() >= payment.getPrice();
	}

	/**
	 * Checks whether a player has enough for a blacksmith operation
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnoughBlacksmith(Player player, Operation op) {
		double price = UtilityProperties.getPrice(Operation.getString(
				op,
				addendum
						+ EconomyHandler.materialAddendums[EconomyHandler
								.getBlacksmithIndex(player.getItemInHand())]));
		return playerHasEnough(player.getName(), price);
	}

	/**
	 * Pays for an operation using the player's money.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double pay(Player player, Operation op) {
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum));
		subtract(player.getName(), price);
		return price;
	}

	/**
	 * Pays for an operation using the player's money, with the ability to
	 * multiply
	 * 
	 * @param player
	 * @param op
	 * @param multiple
	 * @return
	 */
	public static double pay(Player player, Operation op, int multiple) {
		double price = 0;
		if (hasEnough(player, op)) {
			price = UtilityProperties.getPrice(Operation
					.getString(op, addendum));
			subtract(player.getName(), price * multiple);
		} else {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
		}
		return price;
	}

	/**
	 * Pays for a payment using the npc's money.
	 * 
	 * @param npc
	 * @param payment
	 * @return
	 */
	public static double pay(HumanNPC npc, Payment payment) {
		npc.setBalance(npc.getBalance() - payment.getPrice());
		return payment.getPrice();
	}

	/**
	 * Pays for a payment using the player's money.
	 * 
	 * @param player
	 * @param payment
	 * @return
	 */
	public static double pay(Player player, Payment payment) {
		subtract(player.getName(), payment.getPrice());
		return payment.getPrice();
	}

	/**
	 * Add money to a player's account
	 * 
	 * @param name
	 * @param price
	 */
	public static void add(String name, double price) {
		Citizens.economy.getAccount(name).add(price);
	}

	/**
	 * Subtract money from a player's account
	 * 
	 * @param name
	 * @param price
	 */
	public static void subtract(String name, double price) {
		Citizens.economy.getAccount(name).subtract(price);
	}

	/**
	 * Pays for a blacksmith operation
	 * 
	 * @param player
	 * @param item
	 * @param op
	 * @return
	 */
	public static double payBlacksmith(Player player, ItemStack item,
			Operation op) {
		short maxDurability = Material.getMaterial(item.getTypeId())
				.getMaxDurability();
		double price = (maxDurability - (maxDurability - item.getDurability()))
				* UtilityProperties
						.getPrice(Operation
								.getString(
										op,
										addendum
												+ EconomyHandler.materialAddendums[EconomyHandler
														.getBlacksmithIndex(item)]));
		subtract(player.getName(), price);
		return price;
	}
}