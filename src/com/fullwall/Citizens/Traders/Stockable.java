package com.fullwall.Citizens.Traders;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Utils.StringUtils;

public class Stockable {
	private ItemStack stocking;
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
		return new Check(getStockingId(), isSelling());
	}

	public ItemStack getStocking() {
		return this.stocking;
	}

	public Integer getStockingId() {
		return this.stocking.getTypeId();
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

	public boolean isiConomy() {
		return price.isiConomy();
	}

	public String getString(ChatColor colour) {
		return StringUtils.yellowify(getStocking().getAmount() + " "
				+ getStocking().getType().name(), colour)
				+ "(s)";
	}

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
}