package com.citizens.Economy;

import org.bukkit.inventory.ItemStack;

import com.citizens.NPCTypes.Traders.ItemPrice;

public class Payment {
	private double price;
	private ItemStack item;
	private boolean econPlugin;

	/**
	 * Defines a payment, which can be either an economy-plugin or an item
	 * payment. This constructor defines an item payment.
	 * 
	 * @param price
	 * @param item
	 * @param econPlugin
	 */
	public Payment(double price, ItemStack item, boolean econPlugin) {
		this.setPrice(price);
		this.setItem(item);
		this.setEconomyPlugin(econPlugin);
	}

	/**
	 * Defines a payment, which can be either an economy-plugin or an item
	 * payment. This constructor defines an iConomy payment.
	 * 
	 * @param price
	 * @param item
	 * @param econPlugin
	 */
	public Payment(double price, boolean econPlugin) {
		this.setPrice(price);
		this.setItem(null);
		this.setEconomyPlugin(econPlugin);
	}

	public Payment(ItemPrice price2) {
		this.setPrice(price2.isiConomy() ? price2.getPrice() : price2
				.getItemStack().getAmount());
		this.setItem(price2.getItemStack());
		this.setEconomyPlugin(price2.isiConomy());
	}

	public Payment(ItemStack stocking) {
		this.setEconomyPlugin(false);
		this.setItem(stocking);
		this.setPrice(stocking.getAmount());
	}

	public void setEconomyPlugin(boolean econPlugin) {
		this.econPlugin = econPlugin;
	}

	public boolean isEconomyPlugin() {
		return econPlugin;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}
}