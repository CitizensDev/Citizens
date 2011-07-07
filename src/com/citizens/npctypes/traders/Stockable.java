package com.citizens.npctypes.traders;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.citizens.Citizens;
import com.citizens.utils.StringUtils;

public class Stockable {
	private final ItemStack stocking;
	private ItemPrice price;
	private boolean selling;

	/**
	 * Represents an item that the npc can buy or sell.
	 * 
	 * @param stocking
	 * @param price
	 * @param selling
	 */
	public Stockable(ItemStack stocking, ItemPrice price, boolean selling) {
		this.stocking = stocking;
		this.price = price;
		this.setSelling(selling);
	}

	public Check createCheck() {
		return new Check(getStockingId(), getStockingDataValue(), isSelling());
	}

	public ItemStack getStocking() {
		return this.stocking;
	}

	public Integer getStockingId() {
		return this.stocking.getTypeId();
	}

	public short getStockingDataValue() {
		return this.stocking.getDurability();
	}

	public ItemPrice getPrice() {
		return this.price;
	}

	public void setSelling(boolean selling) {
		this.selling = selling;
	}

	public boolean isSelling() {
		return selling;
	}

	public boolean isEconPlugin() {
		return price.isEconPlugin();
	}

	public String getString(ChatColor colour) {
		return StringUtils.wrap(getStocking().getAmount() + " "
				+ getStocking().getType().name(), colour)
				+ "(s)";
	}

	@Override
	public String toString() {
		String ret = "";
		ret += stocking.getTypeId()
				+ Citizens.separatorChar
				+ stocking.getAmount()
				+ Citizens.separatorChar
				+ (stocking.getData() == null ? "0" : stocking.getData()
						.getData()) + Citizens.separatorChar + ",";
		ret += price.toString() + ",";
		ret += selling + ",";
		return ret;
	}

	public void setPrice(ItemPrice price) {
		this.price = price;
	}
}