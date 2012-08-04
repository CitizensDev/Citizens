package net.citizensnpcs.traders;

import com.google.common.base.Objects;

public class Check {
	private final short dataValue;
	private final int itemID;
	private final boolean selling;

	// Used to distinguish between stockables in a hashmap.
	public Check(int itemID, short dataValue, boolean selling) {
		this.itemID = itemID;
		this.dataValue = dataValue;
		this.selling = selling;
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

	@Override
	public int hashCode() {
		return Objects.hashCode(itemID, dataValue, selling);
	}

	public boolean isSelling() {
		return this.selling;
	}
}