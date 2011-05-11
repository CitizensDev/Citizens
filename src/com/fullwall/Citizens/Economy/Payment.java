package com.fullwall.Citizens.Economy;

import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Traders.ItemPrice;

public class Payment {

	private double price;
	private ItemStack item;
	private boolean iConomy;

	/**
	 * Defines a payment, which can be either iConomy or an item payment. This
	 * constructor defines an item payment.
	 * 
	 * @param price
	 * @param item
	 * @param iConomy
	 */
	public Payment(double price, ItemStack item, boolean iConomy) {
		this.setPrice(price);
		this.setItem(item);
		this.setiConomy(iConomy);
	}

	/**
	 * Defines a payment, which can be either iConomy or an item payment. This
	 * constructor defines an iConomy payment.
	 * 
	 * @param price
	 * @param item
	 * @param iConomy
	 */
	public Payment(double price, boolean iConomy) {
		this.setPrice(price);
		this.setItem(null);
		this.setiConomy(iConomy);
	}

	public Payment(ItemPrice price2) {
		this.setPrice(price2.isiConomy() ? price2.getPrice() : price2
				.getAmount());
		this.setItem(price2.getItemStack());
		this.setiConomy(price2.isiConomy());
	}

	public Payment(ItemStack stocking, boolean iConomy) {
		this.setiConomy(iConomy);
		this.setItem(stocking);
		this.setPrice(stocking.getAmount());
	}

	public void setiConomy(boolean iConomy) {
		this.iConomy = iConomy;
	}

	public boolean isiConomy() {
		return iConomy;
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
