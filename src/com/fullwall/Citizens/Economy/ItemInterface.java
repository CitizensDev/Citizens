package com.fullwall.Citizens.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
				if (current > price) {
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
				if (current > payment.getPrice()) {
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
	public static String getCurrency(Operation op) {
		int ID = PropertyPool.getCurrencyID(Operation.getString(op,
				currencyAddendum));
		return Material.getMaterial(ID) != null ? Material.getMaterial(ID)
				.name() : "";
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
		PlayerInventory inv = player.getInventory();
		int current = price;
		for (ItemStack i : inv.getContents()) {
			if (i != null && i.getTypeId() == currencyID) {
				int amount = i.getAmount();
				current -= amount;
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
		PlayerInventory inv = player.getInventory();
		int current = price;
		for (ItemStack i : inv.getContents()) {
			if (i != null && i.getTypeId() == currencyID) {
				int amount = i.getAmount();
				int toChange = 0;
				current -= amount;
				if (current < 0) {
					// we've gone over what we need to take.
					// take away the negative to get the positive value
					// (remainder).
					toChange -= current;
				}
				// air blocks are null
				if (toChange == 0)
					i = null;
				else {
					i.setAmount(toChange);
					break;
				}
			}
		}
		return price;
	}

	/**
	 * Pays for a payment from the player's inventory.
	 * 
	 * @param player
	 * @param payment
	 * @return
	 */
	public static int pay(Player player, Payment payment) {
		PlayerInventory inv = player.getInventory();
		int currencyID = payment.getItem().getTypeId();
		int current = payment.getPrice();
		for (ItemStack i : inv.getContents()) {
			if (i != null && i.getTypeId() == currencyID) {
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
					break;
				}
			}
		}
		return payment.getPrice();
	}
}
