package com.fullwall.Citizens.NPCTypes.Traders;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + itemID;
		result = prime * result + (selling ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Check other = (Check) obj;
		if (itemID != other.itemID)
			return false;
		if (selling != other.selling)
			return false;
		return true;
	}
}