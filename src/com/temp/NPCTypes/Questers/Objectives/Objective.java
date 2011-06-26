package com.temp.NPCTypes.Questers.Objectives;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.temp.NPCTypes.Questers.Quests.QuestManager.QuestType;

public class Objective {
	private int amount = -1;
	private int destination = -1;
	private ItemStack item = null;
	private Location location = null;
	private String message = "";
	private Material material = null;
	private final Progress progress = new Progress();
	private final QuestType type;

	public QuestType getType() {
		return type;
	}

	public Objective(QuestType type) {
		this.type = type;
	}

	public void setDestination(int UID) {
		this.destination = UID;
	}

	public int getDestinationNPCID() {
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

	public Progress getProgress() {
		return this.progress;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public class Progress {
		private int amountCompleted = 0;
		private ItemStack lastItem;
		private Location lastLocation;

		public void incrementCompleted(int i) {
			this.setAmountCompleted(this.getAmount() + 1);
		}

		public int getAmount() {
			return amountCompleted;
		}

		public void setAmountCompleted(int amountCompleted) {
			this.amountCompleted = amountCompleted;
		}

		public void setLastItem(ItemStack lastItem) {
			this.lastItem = lastItem;
		}

		public ItemStack getLastItem() {
			return lastItem;
		}

		public void setLastLocation(Location lastLocation) {
			this.lastLocation = lastLocation;
		}

		public Location getLastLocation() {
			return lastLocation;
		}
	}
}