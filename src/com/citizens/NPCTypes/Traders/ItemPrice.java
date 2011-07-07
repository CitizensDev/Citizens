package com.citizens.npctypes.traders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.citizens.Citizens;

public class ItemPrice {
	private boolean econPlugin = false;
	private double price = 0;
	private ItemStack item;

	/**
	 * Represents a price, which can be iConomy or an item. This constructs an
	 * iConomy price.
	 * 
	 * @param price
	 */
	public ItemPrice(double price) {
		this.setPrice(price);
		this.setEconPlugin(true);
	}

	/**
	 * Represents a price, which can be iConomy or an item. This constructs an
	 * item price.
	 * 
	 * @param price
	 * @param itemID
	 * @param data
	 */
	public ItemPrice(ItemStack item) {
		this.item = item;
		this.setEconPlugin(false);
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setEconPlugin(boolean econPlugin) {
		this.econPlugin = econPlugin;
	}

	public boolean isEconPlugin() {
		return econPlugin;
	}

	public int getData() {
		return item.getData() == null ? 0 : item.getData().getData();
	}

	@Override
	public String toString() {
		String ret = "";
		if (econPlugin) {
			ret += "" + price + Citizens.separatorChar + econPlugin;
		} else {
			ret += "" + item.getAmount() + Citizens.separatorChar
					+ item.getTypeId() + Citizens.separatorChar + getData()
					+ Citizens.separatorChar + econPlugin
					+ Citizens.separatorChar;
		}
		return ret;
	}

	public ItemStack getItemStack() {
		return item;
	}

	public MaterialData getMaterialData() {
		return item == null ? null : item.getData();
	}
}