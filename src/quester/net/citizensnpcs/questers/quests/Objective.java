package net.citizensnpcs.questers.quests;

import net.citizensnpcs.questers.quests.QuestManager.QuestType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Represents what is necessary to complete a quest objective. In order to
 * represent a wide range of quest possibilities, the class merely stores
 * possible data that could be used by a quest. For most objectives, the simple
 * 'amount' field will be enough to judge what is needed - for example, how many
 * monsters have been killed.
 * 
 * The message field is sent to the player upon completion of the objective.
 * 
 * @author fullwall
 * 
 */
public class Objective {
	private final int amount;
	private final int destination;

	private final ItemStack item;
	private final Location location;
	private final String message;
	private final Material material;
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

	public String getMessage() {
		return this.message;
	}

	public Material getMaterial() {
		return material;
	}

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
}