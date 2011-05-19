package com.fullwall.Citizens.NPCTypes.Traders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Citizens;

public class ItemPrice {
	private boolean iConomy = false;
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
		this.setiConomy(true);
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
		this.setiConomy(false);
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setiConomy(boolean iConomy) {
		this.iConomy = iConomy;
	}

	public boolean isiConomy() {
		return iConomy;
	}

	public int getData() {
		return item.getData() == null ? 0 : item.getData().getData();
	}

	public String toString() {
		String ret = "";
		if (iConomy)
			ret += "" + price + Citizens.separatorChar + iConomy;
		else
			ret += "" + item.getAmount() + Citizens.separatorChar
					+ item.getTypeId() + Citizens.separatorChar + getData()
					+ Citizens.separatorChar + iConomy + Citizens.separatorChar;
		return ret;
	}

	public ItemStack getItemStack() {
		return item;
	}

	public MaterialData getMaterialData() {
		return item == null ? null : item.getData();
	}
}