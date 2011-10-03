package net.citizensnpcs.traders;

public class ItemPrice {
	private double price = 0;

	// Represents a price for an item.
	public ItemPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return this.price;
	}

	@Override
	public String toString() {
		return "" + price;
	}
}