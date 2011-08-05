package net.citizensnpcs.traders;

import net.citizensnpcs.utils.MessageUtils;

import org.bukkit.ChatColor;

public class TraderMessageUtils {

	/**
	 * Formats the price message for an ItemPrice.
	 * 
	 * @param price
	 * @return
	 */
	public static String getPriceMessage(ItemPrice price, ChatColor colour) {
		String message = "";
		// message += colour
		// + StringUtils.wrap(
		// EconomyManager.getCurrency(new Payment(price), colour),
		// colour);
		// TODO
		return message;
	}

	/**
	 * Formats the ItemStack contained in a stockable to a string.
	 * 
	 * @param s
	 * @param colour
	 * @return
	 */
	public static String getStockableMessage(Stockable s, ChatColor colour) {
		return MessageUtils.getStackString(s.getStocking(), colour) + " for "
				+ getPriceMessage(s.getPrice(), colour);
	}
}
