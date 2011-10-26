package net.citizensnpcs.traders;

public class Check {
	private final int itemID;
	private final short dataValue;
	private final boolean selling;

	// Used to distinguish between stockables in a hashmap.
	public Check(int itemID, short dataValue, boolean selling) {
		this.itemID = itemID;
		this.dataValue = dataValue;
		this.selling = selling;
	}

	@Override
	public int hashCode() {
		return 31 * (31 * (31 + itemID) + dataValue) + (selling ? 1231 : 1237);
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