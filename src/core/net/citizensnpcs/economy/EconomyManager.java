package net.citizensnpcs.economy;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.properties.SettingsManager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EconomyManager {
	private static boolean serverEconomyEnabled = false;
	private static boolean useEconPlugin = SettingsManager
			.getBoolean("UseEconomy");
	public static final String addendum = ".econplugin";

	public static final String[] materialAddendums = { ".misc", ".wood",
			".gold", ".stone", ".iron", ".diamond", ".leather", ".chainmail" };

	/**
	 * Used for economy-plugin support.
	 * 
	 * @param value
	 */
	public static void setServerEconomyEnabled(boolean value) {
		serverEconomyEnabled = value;
	}

	/**
	 * A helper method that checks a few variables for whether economy-plugins
	 * should be enabled. (is using economy-plugins enabled? is an
	 * economy-plugin loaded?)
	 * 
	 * @return
	 */
	public static boolean useEconPlugin() {
		return (useEconPlugin && serverEconomyEnabled);
	}

	/**
	 * Gets what item ID or economy-plugin currency is being used for an
	 * operation.
	 * 
	 * @param amount
	 * @return
	 */
	public static String getPaymentType(String amount) {
		if (useEconPlugin()) {
			return format(amount);
		}
		return "None";
	}

	/**
	 * Get the index of the material addendums array based on an item ID
	 * 
	 * @param item
	 * @return
	 */
	public static int getBlacksmithIndex(ItemStack item) {
		int id = item.getTypeId();
		if (id == 259 || id == 346 || id == 359) {
			return 0;
		} else if ((id >= 268 && id <= 271) || id == 290) {
			return 1;
		} else if ((id >= 283 && id <= 286) || id == 294
				|| (id >= 314 && id <= 317)) {
			return 2;
		} else if ((id >= 272 && id <= 275) || id == 291) {
			return 3;
		} else if ((id >= 256 && id <= 258) || id == 267 || id == 292
				|| (id >= 306 && id <= 309)) {
			return 4;
		} else if ((id >= 276 && id <= 279) || id == 293
				|| (id >= 310 && id <= 313)) {
			return 5;
		} else if ((id >= 298 && id <= 301)) {
			return 6;
		} else if ((id >= 302 && id <= 305)) {
			return 7;
		}
		return 0;
	}

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
	public static String getRemainder(Player player, double totalPrice) {
		if (useEconPlugin()) {
			return ""
					+ (totalPrice - Citizens.economy.getAccount(
							player.getName()).balance());
		}
		return "0";
	}

	/**
	 * Checks whether the player has enough money for an operation.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnough(Player player, double price) {
		if (useEconPlugin()) {
			return playerHasEnough(player.getName(), price);
		}
		return true;
	}

	/**
	 * Pays for an operation using the player's money.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double pay(Player player, double price) {
		if (useEconPlugin()) {
			subtract(player.getName(), price);
			return price;
		}
		return 0;
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
}