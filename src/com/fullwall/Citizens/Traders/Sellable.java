package com.fullwall.Citizens.Traders;

import org.bukkit.inventory.ItemStack;

public class Sellable {
	private ItemStack selling;
	private ItemPrice price;

	public Sellable(ItemStack selling, ItemPrice price) {
		this.selling = selling;
		this.price = price;
	}

	public ItemStack getSelling() {
		return this.selling;
	}

	public Integer getSellingId() {
		return this.selling.getTypeId();
	}

	public ItemPrice getPrice() {
		return this.price;
	}
}
