package com.fullwall.Citizens.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class ItemInterface {
	public static String addendum = "-item";
	public static String currencyAddendum = "-item-currency-id";

	/**
	 * Checks the inventory of a player for having enough for an operation.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnough(Player player, Operation op) {
		// Get the price/currency from the enum name.
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum));
		int currencyID = UtilityProperties.getCurrencyID(Operation.getString(
				op, currencyAddendum));
		// The current count.
		int current = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null && i.getTypeId() == currencyID) {
				current += i.getAmount();
				if (current >= price) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks the inventory of a player for having enough for a payment.
	 * 
	 * @param payment
	 * @param player
	 * @return
	 */
	public static boolean hasEnough(Payment payment, Player player) {
		int current = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null && i.getTypeId() == payment.getItem().getTypeId()) {
				current += i.getAmount();
				if (current >= payment.getPrice()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the item currency from an operation.
	 * 
	 * @param op
	 * @return
	 */
	public static String getCurrency(Operation op, ChatColor colour) {
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum));
		int ID = UtilityProperties.getCurrencyID(Operation.getString(op,
				currencyAddendum));
		return Material.getMaterial(ID) != null ? price + " "
				+ Material.getMaterial(ID).name() + colour + "(s)" : "";
	}

	public static String getCurrency(Payment payment, ChatColor colour) {
		return Material.getMaterial(payment.getItem().getTypeId()) != null ? payment
				.getItem().getAmount()
				+ " "
				+ Material.getMaterial(payment.getItem().getTypeId()).name()
				+ colour + "(s)"
				: "";
	}

	/**
	 * Gets the remainder necessary in an inventory from an operation.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static String getRemainder(Operation op, Player player) {
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum));
		int currencyID = UtilityProperties.getCurrencyID(Operation.getString(
				op, currencyAddendum));
		double current = price;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null && i.getTypeId() == currencyID) {
				current -= i.getAmount();
			}
		}
		return "" + current;
	}

	/**
	 * Pays from the player's inventory using an operation.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double pay(Player player, Operation op) {
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum));
		int currencyID = UtilityProperties.getCurrencyID(Operation.getString(
				op, currencyAddendum));
		double current = price;
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, i, currencyID, current,
						count);
				if (current <= 0)
					break;
			}
			count += 1;
		}
		return price;
	}

	/**
	 * Pays from the player's inventory using an operation, with the ability to
	 * multiply
	 * 
	 * @param player
	 * @param op
	 * @param multiple
	 * @return
	 */
	public static double pay(Player player, Operation op, int multiple) {
		double price = UtilityProperties.getPrice(Operation.getString(op,
				addendum)) * multiple;
		int currencyID = UtilityProperties.getCurrencyID(Operation.getString(
				op, currencyAddendum));
		double current = price;
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, i, currencyID, current,
						count);
				if (current <= 0)
					break;
			}
			count += 1;
		}
		return price;
	}

	/**
	 * Pays for a payment from the player's inventory.
	 * 
	 * @param player
	 * @param payment
	 * @param slot
	 * @return
	 */
	public static double pay(Player player, Payment payment, int slot) {
		int currencyID = payment.getItem().getTypeId();
		double current = payment.getPrice();
		int count = 0;
		if (slot != -1) {
			current = decreaseItemStack(player,
					player.getInventory().getItem(slot), currencyID, current,
					slot);
		}
		if (current <= 0)
			return payment.getPrice();
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, i, currencyID, current,
						count);
				if (current <= 0)
					break;
			}
			count += 1;
		}
		return payment.getPrice();
	}

	public static double decreaseItemStack(Player player, ItemStack i,
			int currencyID, double current, int slot) {
		if (i.getTypeId() == currencyID) {
			int amount = i.getAmount();
			int toChange = 0;
			current -= amount;
			if (current < 0) {
				toChange -= current;
			}
			if (toChange == 0)
				i = null;
			else {
				i.setAmount(toChange);
			}
			player.getInventory().setItem(slot, i);
		}
		return current;
	}
}
