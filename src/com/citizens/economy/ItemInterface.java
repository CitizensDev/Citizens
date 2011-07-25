package com.citizens.economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.properties.properties.UtilityProperties;
import com.citizens.utils.MessageUtils;

public class ItemInterface {

	/**
	 * Checks the inventory of a player for having enough for an operation.
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnough(Player player, int itemCurrencyID,
			int itemPrice) {
		// Get the price/currency from the enum name.
		// double price = UtilityProperties.getPrice(Operation.getString(op,
		// addendum));

		// int currencyID = UtilityProperties.getCurrencyID(Operation.getString(
		// op, currencyAddendum));
		// The current count.
		int current = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null && i.getTypeId() == itemCurrencyID) {
				current += i.getAmount();
				if (current >= itemPrice) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks the inventory if a player has enough of an item for a blacksmith
	 * operation
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static boolean hasEnoughBlacksmith(Player player, EconomyOperation op) {
		double price = getBlacksmithPrice(op, player);
		int currencyID = op.getItemCurrencyID();
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
	public static String getCurrency(EconomyOperation op) {
		return ChatColor.stripColor(MessageUtils.getStackString(new ItemStack(
				op.getItemCurrencyID(), op.getItemPrice())));
	}

	/**
	 * Get the currency for a blacksmith operation
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static String getBlacksmithCurrency(Player player,
			EconomyOperation op) {
		return ChatColor.stripColor(MessageUtils.getStackString(new ItemStack(
				op.getItemCurrencyID(), getBlacksmithPrice(op, player))));
	}

	/**
	 * Get the currency using a payment
	 * 
	 * @param payment
	 * @param colour
	 * @return
	 */
	public static String getCurrency(Payment payment, ChatColor colour) {
		return MessageUtils.getStackString(payment.getItem(), colour);
	}

	/**
	 * Gets the remainder necessary in an inventory from an operation.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static String getRemainder(EconomyOperation op, Player player) {
		int price = op.getItemPrice();
		int currencyID = op.getItemCurrencyID();
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
	public static double pay(Player player, int itemCurrencyID, int itemPrice) {
		// double price = UtilityProperties.getPrice(Operation.getString(op,
		// addendum));
		// int currencyID = UtilityProperties.getCurrencyID(Operation.getString(
		// op, currencyAddendum));
		double current = itemPrice;
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, itemCurrencyID, current,
						count);
				if (current <= 0) {
					break;
				}
			}
			count += 1;
		}
		return itemPrice;
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
		if (current <= 0) {
			return current;
		}
		int count = 0;
		if (slot != -1) {
			current = decreaseItemStack(player, currencyID, current, slot);
		}
		if (current <= 0) {
			return payment.getPrice();
		}
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) {
				current = decreaseItemStack(player, currencyID, current, count);
				if (current <= 0) {
					break;
				}
			}
			count += 1;
		}
		return payment.getPrice();
	}

	/**
	 * Pays for a blacksmith operation
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double payBlacksmith(Player player, EconomyOperation op) {
		int currencyID = op.getItemCurrencyID();
		double blacksmithPrice = getBlacksmithPrice(op, player);
		if (blacksmithPrice <= 0 || currencyID == 0) {
			return blacksmithPrice;
		}
		double current = blacksmithPrice;
		int count = 0;
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				current = decreaseItemStack(player, currencyID, current, count);
				if (current <= 0) {
					break;
				}
			}
			count += 1;
		}
		return blacksmithPrice;
	}

	/**
	 * Get the price for repairing an item
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static int getBlacksmithPrice(EconomyOperation op, Player player) {
		ItemStack item = player.getItemInHand();
		short maxDurability = Material.getMaterial(item.getTypeId())
				.getMaxDurability();
		double price = (maxDurability - (maxDurability - item.getDurability()))
				* (UtilityProperties.getItemPriceExtended(op.getPath(),
						EconomyManager.materialAddendums[EconomyManager
								.getBlacksmithIndex(item)]));
		if (price < 1) {
			price = 1;
		}
		return (int) price;
	}

	/**
	 * Removes item(s) from an inventory
	 * 
	 * @param player
	 * @param currencyID
	 * @param current
	 * @param slot
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static double decreaseItemStack(Player player, int currencyID,
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
		player.updateInventory();
		return current;
	}
}