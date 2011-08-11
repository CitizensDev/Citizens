package net.citizensnpcs.questers.quests;

import net.citizensnpcs.questers.quests.QuestManager.QuestType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Objective {
	public static class Builder {
		private int amount = -1;
		private int destination = -1;

		private ItemStack item = null;
		private Location location = null;
		private String message = "";
		private Material material = null;
		private final QuestType type;

		public Builder(QuestType type) {
			this.type = type;
		}

		public Builder amount(int amount) {
			this.amount = amount;
			return this;
		}

		public Builder destination(int destination) {
			this.destination = destination;
			return this;
		}

		public Builder item(ItemStack item) {
			this.item = item;
			return this;
		}

		public Builder location(Location location) {
			this.location = location;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder material(Material material) {
			this.material = material;
			return this;
		}

		public Objective build() {
			return new Objective(type, amount, destination, item, message,
					material, location);
		}
	}

	private final int amount;
	private final int destination;

	private final ItemStack item;
	private final Location location;
	private final String message;
	private final Material material;
	private final Progress progress = new Progress();
	private final QuestType type;

	private Objective(QuestType type, int amount, int destination,
			ItemStack item, String message, Material material, Location location) {
		this.type = type;
		this.amount = amount;
		this.destination = destination;
		this.item = item;
		this.message = message;
		this.material = material;
		this.location = location;
	}

	public QuestType getType() {
		return type;
	}

	public int getDestinationNPCID() {
		return destination;
	}

	public ItemStack getItem() {
		return item;
	}

	public int getAmount() {
		return amount;
	}

	public Location getLocation() {
		return location;
	}

	public Progress getProgress() {
		return this.progress;
	}

	public String getMessage() {
		return this.message;
	}

	public Material getMaterial() {
		return material;
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