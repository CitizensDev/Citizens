package com.fullwall.Citizens.Economy;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.nijikokun.register.payment.Method.MethodAccount;

public class ServerEconomyInterface {

	public static String addendum = "-econplugin";

	/**
	 * Uses the iConomy methods to check whether a player has enough in their
	 * account to pay.
	 * 
	 * @param name
	 * @param amount
	 * @return
	 */
	public static boolean playerHasEnough(String name, double amount) {
		if (Citizens.economy.hasAccount(name))
			return Citizens.economy.getAccount(name).hasEnough(amount);
		else
			return false;
	}

	/**
	 * Gets an iConomy balance.
	 * 
	 * @param name
	 * @return
	 */
	public static double getBalance(String name) {
		if (Citizens.economy.hasAccount(name))
			return Citizens.economy.getAccount(name).balance();
		else
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
	 * Gets the iConomy currency.
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

	public static void add(String name, double price) {
		Citizens.economy.getAccount(name).add(price);
	}

	public static void subtract(String name, double price) {
		Citizens.economy.getAccount(name).subtract(price);
	}

	/**
	 * Get the price it costs for a blacksmith operation
	 * 
	 * @param player
	 * @param item
	 * @param op
	 * @return
	 */
	public static double payBlacksmithPrice(Player player, ItemStack item,
			Operation op) {
		short maxDurability = Material.getMaterial(item.getTypeId())
				.getMaxDurability();
		double percentage = ((double) maxDurability - item.getDurability())
				/ (double) maxDurability;
		double price = (1.0 - percentage)
				* UtilityProperties.getPrice(Operation.getString(op, addendum));
		subtract(player.getName(), price);
		return price;
	}
}