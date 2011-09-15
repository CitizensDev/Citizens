package net.citizensnpcs.economy;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;

public class EconomyManager {
	private static boolean serverEconomyEnabled = false;
	private static boolean useEconPlugin = SettingsManager
			.getBoolean("UseEconomy");

	// Used for economy-plugin support.
	public static void setServerEconomyEnabled(boolean value) {
		serverEconomyEnabled = value;
	}

	/*
	 * A helper method that checks a few variables for whether economy-plugins
	 * should be enabled. (is using economy-plugins enabled? is an
	 * economy-plugin loaded?)
	 */
	public static boolean useEconPlugin() {
		return (useEconPlugin && serverEconomyEnabled);
	}

	// Gets what item ID or economy-plugin currency is being used for an
	// operation.
	public static String getPaymentType(String amount) {
		if (useEconPlugin()) {
			return format(amount);
		}
		return "None";
	}

	// Uses the economy-plugin methods to check whether a player has enough in
	// their account to pay.
	public static boolean playerHasEnough(String name, double amount) {
		return Citizens.economy.hasAccount(name)
				&& Citizens.economy.getAccount(name).hasEnough(amount);
	}

	// Gets an economy-plugin balance.
	public static double getBalance(String name) {
		if (Citizens.economy.hasAccount(name)) {
			return Citizens.economy.getAccount(name).balance();
		}
		return -1;
	}

	public static String getFormattedBalance(String name) {
		return format(getBalance(name));
	}

	// Gets the economy-plugin currency from the passed String.
	public static String format(String amount) {
		return format(Double.parseDouble(amount));
	}

	// Gets the economy-plugin currency from the passed double.
	public static String format(double price) {
		return Citizens.economy.format(price);
	}

	// Gets the remainder necessary for an operation to be completed.
	public static String getRemainder(Player player, double totalPrice) {
		if (useEconPlugin()) {
			return ""
					+ (totalPrice - Citizens.economy.getAccount(
							player.getName()).balance());
		}
		return "0";
	}

	// Checks whether the player has enough money for an operation.
	public static boolean hasEnough(Player player, double price) {
		if (useEconPlugin()) {
			return playerHasEnough(player.getName(), price);
		}
		return true;
	}

	// Pays for an operation using the player's money.
	public static double pay(Player player, double price) {
		if (useEconPlugin()) {
			subtract(player.getName(), price);
			return price;
		}
		return 0;
	}

	// Add money to a player's account
	public static void add(String name, double price) {
		Citizens.economy.getAccount(name).add(price);
	}

	// Subtract money from a player's account
	public static void subtract(String name, double price) {
		Citizens.economy.getAccount(name).subtract(price);
	}

	// Pay an NPC the specified amount
	public static void pay(HumanNPC npc, double amount) {
		npc.setBalance(npc.getBalance() - amount);
	}

	// Check if an NPC has enough money
	public static boolean hasEnough(HumanNPC npc, double amount) {
		return npc.getBalance() - amount >= 0;
	}
}