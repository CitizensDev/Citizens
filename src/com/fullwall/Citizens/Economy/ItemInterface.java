package com.fullwall.Citizens.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.Citizens.Utils.MessageUtils;

public class ItemInterface {
	public static String addendum = ".item";
	public static String currencyAddendum = ".item-currency-id";
	private static double blacksmithPrice;

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
		if (price <= 0 || currencyID == 0) {
			return true;
		}
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
		if (payment.getItem().getAmount() <= 0
				|| payment.getItem().getTypeId() == 0) {
			return true;
		}
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
		return ChatColor.stripColor(MessageUtils.getStackString(new ItemStack(
				ID, (int) price)));
	}

	/**
	 * Get the name of an item currency by its item ID
	 * 
	 * @param op
	 * @return
	 */
	public static String getCurrencyName(Operation op) {
		return Material.getMaterial(
				UtilityProperties.getCurrencyID(Operation.getString(op,
						currencyAddendum))).name();
	}

	/**
	 * Get the currency using a payment
	 * 
	 * @param payment
	 * @param colour
	 * @return
	 */
	public static String getCurrency(Payment payment, ChatColor colour) {
		return ChatColor.stripColor(MessageUtils.getStackString(payment
				.getItem()));
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
		if (price <= 0 || currencyID == 0) {
			return price;
		}
		double current = price;
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, currencyID, current, count);
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
		if (price <= 0 || currencyID == 0) {
			return price;
		}
		double current = price;
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, currencyID, current, count);
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
		if (current <= 0)
			return current;
		int count = 0;
		if (slot != -1) {
			current = decreaseItemStack(player, currencyID, current, slot);
		}
		if (current <= 0)
			return payment.getPrice();
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) {
				current = decreaseItemStack(player, currencyID, current, count);
				if (current <= 0)
					break;
			}
			count += 1;
		}
		return payment.getPrice();
	}

	public static double decreaseItemStack(Player player, int currencyID,
			double current, int slot) {
		ItemStack item = player.getInventory().getItem(slot);
		if (item.getTypeId() == currencyID) {
			int amount = item.getAmount();
			int toChange = 0;
			current -= amount;
			if (current < 0) {
				toChange -= current;
			}
			if (toChange == 0) {
				item = null;
			} else {
				item.setAmount(toChange);
			}
			player.getInventory().setItem(slot, item);
		}
		return current;
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
		int currencyID = UtilityProperties.getCurrencyID(Operation.getString(
				op, currencyAddendum));
		double current = getBlacksmithPrice(player, item, op);
		if (current <= 0 || currencyID == 0) {
			return blacksmithPrice;
		}
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, currencyID, current, count);
				if (current <= 0)
					break;
			}
			count += 1;
		}
		return blacksmithPrice;
	}

	/**
	 * Get the price for repairing an item
	 * 
	 * @param player
	 * @param item
	 * @param op
	 * @return
	 */
	public static int getBlacksmithPrice(Player player, ItemStack item,
			Operation op) {
		short maxDurability = Material.getMaterial(item.getTypeId())
				.getMaxDurability();
		double percentage = ((double) maxDurability - item.getDurability())
				/ maxDurability;
		blacksmithPrice = (1.0 - percentage)
				* UtilityProperties.getPrice(Operation.getString(op, addendum));
		if (blacksmithPrice < 1.0) {
			blacksmithPrice += 1;
		}
		return (int) blacksmithPrice;
	}
}