package com.citizens.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.Citizens;
import com.citizens.properties.properties.UtilityProperties;

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
	public static String getRemainder(EconomyOperation op, Player player) {
		return ""
				+ (op.getEconPluginPrice() - Citizens.economy.getAccount(
						player.getName()).balance());
	}

	/**
	 * Checks whether the player has enough money for an operation.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnough(Player player, double price) {
		// double price = UtilityProperties.getPrice(Operation.getString(op,
		// addendum));
		return playerHasEnough(player.getName(), price);
	}

	/**
	 * Checks whether a player has enough for a blacksmith operation
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnoughBlacksmith(Player player, EconomyOperation op) {
		return playerHasEnough(player.getName(), getBlacksmithPrice(player, op));
	}

	/**
	 * Pays for an operation using the player's money.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double pay(Player player, double price) {
		// double price = UtilityProperties.getPrice(Operation.getString(op,
		// addendum));
		subtract(player.getName(), price);
		return price;
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
	 * Get the price for a blacksmith operation
	 * 
	 * @param player
	 * @param item
	 * @param op
	 * @return
	 */
	public static double getBlacksmithPrice(Player player, EconomyOperation op) {
		ItemStack item = player.getItemInHand();
		short maxDurability = Material.getMaterial(item.getTypeId())
				.getMaxDurability();
		double price = (maxDurability - (maxDurability - item.getDurability()))
				* UtilityProperties.getEconPluginPriceExtended(op.getPath(),
						EconomyManager.materialAddendums[EconomyManager
								.getBlacksmithIndex(item)]);
		return price;
	}

	/**
	 * Pays for a blacksmith operation
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double payBlacksmith(Player player, EconomyOperation op) {
		double price = getBlacksmithPrice(player, op);
		subtract(player.getName(), price);
		return price;
	}
}