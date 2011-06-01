package com.fullwall.Citizens.NPCTypes.Questers;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class Objective {
	private HumanNPC destination;
	private ItemStack item;
	private int amount;
	private Location location;

	public void setDestination(HumanNPC destination) {
		this.destination = destination;
	}

	public HumanNPC getDestination() {
		return destination;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}
}
