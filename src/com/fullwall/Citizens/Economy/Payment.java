package com.fullwall.Citizens.Economy;

import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Traders.ItemPrice;

public class Payment {

	private int price;
	private ItemStack item;
	private boolean iConomy;

	public Payment(int price, ItemStack item, boolean iConomy) {
		this.setPrice(price);
		this.setItem(item);
		this.setiConomy(iConomy);
	}

	public Payment(int price, boolean iConomy) {
		this.setPrice(price);
		this.setItem(null);
		this.setiConomy(iConomy);
	}

	public Payment(ItemPrice price2) {
		this.setPrice(price2.getPrice());
		this.setItem(new ItemStack(price2.getItemID(), 1));
		this.setiConomy(price2.isIconomy());
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

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}
}
