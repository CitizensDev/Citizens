package net.citizensnpcs.traders;

import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.utils.MessageUtils;

import org.bukkit.ChatColor;

public class TraderMessageUtils {

	// Formats the price message for an ItemPrice.
	public static String getPriceMessage(ItemPrice price, ChatColor colour) {
		String message = "";
	    Double itemPrice = price.getPrice();
		String paymentType = EconomyManager.getPaymentType(itemPrice.toString());
		message += colour;
		message += paymentType;
		return message;
	}

	// Formats the ItemStack contained in a stockable to a string.
	public static String getStockableMessage(Stockable s, ChatColor colour) {
		return MessageUtils.getStackString(s.getStocking(), colour) + " for "
				+ getPriceMessage(s.getPrice(), colour);
	}
}
