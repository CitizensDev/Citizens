package com.fullwall.Citizens.Traders;

import org.bukkit.inventory.ItemStack;

public class Buyable {
	private ItemStack buying;
	private ItemPrice price;

	public Buyable(ItemStack buying, ItemPrice price) {
		this.buying = buying;
		this.price = price;
	}

	public ItemStack getBuying() {
		return this.buying;
	}

	public ItemPrice getPrice() {
		return this.price;
	}
}
