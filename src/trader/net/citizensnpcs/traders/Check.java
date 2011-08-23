package net.citizensnpcs.traders;

public class Check {
	private int itemID;
	private short dataValue;
	private boolean selling;

	// Used to distinguish between stockables in a hashmap.
	public Check(int itemID, short dataValue, boolean selling) {
		this.setItemID(itemID);
		this.setDataValue(dataValue);
		this.setSelling(selling);
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setDataValue(short dataValue) {
		this.dataValue = dataValue;
	}

	public void setSelling(boolean selling) {
		this.selling = selling;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + itemID;
		result = prime * result + dataValue;
		result = prime * result + (selling ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Check other = (Check) obj;
		return itemID == other.itemID && dataValue == other.dataValue
				&& selling == other.selling;
	}

	public boolean isSelling() {
		return this.selling;
	}
}