package com.fullwall.Citizens.Traders;

import com.fullwall.Citizens.Citizens;

public class Check {
	private int itemID;
	private boolean selling;

	/**
	 * Used to distinguish between stockables in a hashmap.
	 * 
	 * @param itemID
	 * @param selling
	 */
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

	public String toString() {
		return "" + itemID + Citizens.separatorChar + selling;
	}
}
