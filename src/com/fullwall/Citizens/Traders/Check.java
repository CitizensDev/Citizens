package com.fullwall.Citizens.Traders;

public class Check {
	private int itemID;
	private boolean selling;

	public Check(int itemID, boolean selling) {
		this.setItemID(itemID);
		this.setSelling(selling);
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setSelling(boolean selling) {
		this.selling = selling;
	}

	public boolean isSelling() {
		return selling;
	}
}
