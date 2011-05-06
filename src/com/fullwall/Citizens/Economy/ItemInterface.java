package com.fullwall.Citizens.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.PropertyPool;

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
		int price = PropertyPool.getPrice(Operation.getString(op, addendum));
		int currencyID = PropertyPool.getCurrencyID(Operation.getString(op,
				currencyAddendum));
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
		int price = PropertyPool.getPrice(Operation.getString(op, addendum));
		int ID = PropertyPool.getCurrencyID(Operation.getString(op,
				currencyAddendum));
		return Material.getMaterial(ID) != null ? price + " "
				+ Material.getMaterial(ID).name() + colour + "(s)" : "";
	}

	public static String getCurrency(Payment payment, ChatColor colour) {
		return Material.getMaterial(payment.getItem().getTypeId()) != null ? payment
				.getPrice()
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
		int price = PropertyPool.getPrice(Operation.getString(op, addendum));
		int currencyID = PropertyPool.getCurrencyID(Operation.getString(op,
				currencyAddendum));
		int current = price;
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
	public static int pay(Player player, Operation op) {
		int price = PropertyPool.getPrice(Operation.getString(op, addendum));
		int currencyID = PropertyPool.getCurrencyID(Operation.getString(op,
				currencyAddendum));
		int current = price;
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				decreaseItemStack(player, currencyID, current, count);
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
	public static int pay(Player player, Payment payment, int slot) {
		int currencyID = payment.getItem().getTypeId();
		int current = payment.getPrice();
		int count = 0;
		if (slot != -1) {
			decreaseItemStack(player, currencyID, current, slot);
		}
		if (current <= 0)
			return payment.getPrice();
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				decreaseItemStack(player, currencyID, current, count);
				if (current <= 0)
					break;
			}
			count += 1;
		}
		return payment.getPrice();
	}

	public static int decreaseItemStack(Player player, int currencyID,
			int current, int slot) {
		ItemStack i = player.getInventory().getItem(slot);
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
