package net.citizensnpcs.npcdata;

import org.bukkit.inventory.ItemStack;

public class ItemData {
	private int id;
	private short durability;

	/*
	 * Used to store items with an ID and durability
	 */
	public ItemData(int id, short durability) {
		this.id = id;
		this.durability = durability;
	}

	public ItemStack createStack() {
		return new ItemStack(this.id, 1, this.durability);
	}

	public int getID() {
		return this.id;
	}

	public short getDurability() {
		return this.durability;
	}
}