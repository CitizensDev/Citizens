package com.fullwall.Citizens.Traders;

import org.bukkit.inventory.ItemStack;

public class Sellable {
	private boolean iConomy = false;
	private ItemStack selling;

	public Sellable(boolean iConomy, ItemStack selling, int price) {
		this.iConomy = iConomy;
		this.selling = selling;
	}
}
