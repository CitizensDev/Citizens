package net.citizensnpcs.traders;

import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class Stockable {
	private final ItemStack stocking;
	private ItemPrice price;
	private boolean selling;

	// Represents an item that the npc can buy or sell.
	public Stockable(ItemStack stocking, ItemPrice price, boolean selling) {
		this.stocking = stocking;
		this.price = price;
		this.setSelling(selling);
	}

	public Check createCheck() {
		return new Check(this.stocking.getTypeId(),
				this.stocking.getDurability(), isSelling());
	}

	public ItemStack getStocking() {
		return this.stocking;
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

	public String getString(ChatColor colour) {
		return StringUtils.wrap(getStocking().getAmount() + " "
				+ getStocking().getType().name(), colour)
				+ "(s)";
	}

	@Override
	public String toString() {
		String ret = "";
		ret += stocking.getTypeId() + "/" + stocking.getAmount() + "/"
				+ (stocking.getDurability()) + "/" + ",";
		ret += price.toString() + ",";
		ret += selling + ",";
		return ret;
	}

	public void setPrice(ItemPrice price) {
		this.price = price;
	}
}