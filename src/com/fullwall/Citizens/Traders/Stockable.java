package com.fullwall.Citizens.Traders;

import org.bukkit.inventory.ItemStack;

public class Stockable {
	private ItemStack stocking;
	private ItemPrice price;
	private boolean selling;

	public Stockable(ItemStack stocking, ItemPrice price, boolean selling) {
		this.stocking = stocking;
		this.price = price;
		this.setSelling(selling);
	}

	public ItemStack getStocking() {
		return this.stocking;
	}

	public Integer getStockingId() {
		return this.stocking.getTypeId();
	}

	public ItemPrice getPrice() {
		return this.price;
	}

	public void setSelling(boolean selling) {
		this.selling = selling;
	}

	public boolean isSelling() {
		return selling;
	}
}
