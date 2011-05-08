package com.fullwall.Citizens.Economy;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.iConomy;
import com.iConomy.system.Account;

public class IconomyInterface {

	public static String addendum = "-iconomy";

	/**
	 * Uses the iConomy methods to check whether a player has enough in their
	 * account to pay.
	 * 
	 * @param name
	 * @param amount
	 * @return
	 */
	public static boolean playerHasEnough(String name, double amount) {
		if (iConomy.hasAccount(name))
			return iConomy.getAccount(name).getHoldings().hasEnough(amount);
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
		if (iConomy.hasAccount(name))
			return iConomy.getAccount(name).getHoldings().balance();
		else
			return -1;
	}

	/**
	 * Gets the iConomy currency.
	 * 
	 * @param amount
	 * @return
	 */
	public static String getCurrency(String amount) {
		return iConomy.format(Double.parseDouble(amount));
	}

	/**
	 * Gets the iConomy currency.
	 * 
	 * @param price
	 * @return
	 */
	public static String getCurrency(double price) {
		return iConomy.format(price);
	}

	/**
	 * Gets the remainder necessary for an operation to be completed.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static String getRemainder(Operation op, Player player) {
		double price = PropertyPool.getPrice(Operation.getString(op, addendum));
		Account acc = iConomy.getAccount(player.getName());
		return "" + (price - acc.getHoldings().balance());
	}

	/**
	 * Checks whether the player has enough money for an operation.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnough(Player player, Operation op) {
		double price = PropertyPool.getPrice(Operation.getString(op, addendum));
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
		return npc.getBalance() > payment.getPrice();
	}

	/**
	 * Pays for an operation using the player's money.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double pay(Player player, Operation op) {
		double price = PropertyPool.getPrice(Operation.getString(op, addendum));
		iConomy.getAccount(player.getName()).getHoldings().subtract(price);
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
		double price = PropertyPool.getPrice(Operation.getString(op, addendum));
		iConomy.getAccount(player.getName()).getHoldings()
				.subtract(price * multiple);
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
		iConomy.getAccount(player.getName()).getHoldings()
				.subtract(payment.getPrice());
		return payment.getPrice();
	}
}
