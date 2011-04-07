package com.fullwall.Citizens.Traders;

public class ItemPrice {
	private boolean iConomy = false;
	private int price = 0;
	private int itemID = 1;

	public ItemPrice(int price) {
		this.price = price;
		this.iConomy = true;
	}

	public ItemPrice(int itemID, int price) {
		this.price = price;
		this.itemID = itemID;
		this.iConomy = false;
	}

	public boolean isIconomy() {
		return this.iConomy;
	}

	public int getPrice() {
		return this.price;
	}

	public int getItemID() {
		return this.itemID;
	}
}
