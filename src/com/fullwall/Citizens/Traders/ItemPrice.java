package com.fullwall.Citizens.Traders;

import com.fullwall.Citizens.Citizens;

public class ItemPrice {
	private boolean iConomy = false;
	private int price = 0;
	private int itemID = 1;
	private int data;

	public ItemPrice(int price) {
		this.setPrice(price);
		this.setiConomy(true);
	}

	public ItemPrice(int price, int itemID, int data) {
		this.setData(data);
		this.setPrice(price);
		this.setItemID(itemID);
		this.setiConomy(false);
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
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
		ret += (iConomy ? "" + price + Citizens.separatorChar + iConomy
				+ Citizens.separatorChar : "" + price + Citizens.separatorChar
				+ itemID + Citizens.separatorChar + data
				+ Citizens.separatorChar + iConomy + Citizens.separatorChar);
		return ret;
	}
}
