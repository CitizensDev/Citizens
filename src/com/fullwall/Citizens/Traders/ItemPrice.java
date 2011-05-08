package com.fullwall.Citizens.Traders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Citizens;

public class ItemPrice {
	private boolean iConomy = false;
	private double price = 0;
	private int itemID = 1;
	private int data;
	private int amount = 1;

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
	public ItemPrice(int amount, int itemID, int data) {
		this.setData(data);
		this.setAmount(amount);
		this.setItemID(itemID);
		this.setiConomy(false);
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getItemID() {
		return this.itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setiConomy(boolean iConomy) {
		this.iConomy = iConomy;
	}

	public boolean isiConomy() {
		return iConomy;
	}

	public void setData(int data) {
		this.data = data;
	}

	public int getData() {
		return data;
	}

	public String toString() {
		String ret = "";
		if (iConomy)
			ret += "" + price + Citizens.separatorChar + iConomy;
		else
			ret += "" + amount + Citizens.separatorChar + itemID
					+ Citizens.separatorChar + data + Citizens.separatorChar
					+ iConomy + Citizens.separatorChar;
		return ret;
	}

	public ItemStack getItemStack() {
		ItemStack item = new ItemStack(itemID, amount);
		item.setData(new MaterialData(data));
		return item;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
}
