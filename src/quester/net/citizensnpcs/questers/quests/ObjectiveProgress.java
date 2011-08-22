package net.citizensnpcs.questers.quests;

import net.citizensnpcs.questers.quests.Objectives.ObjectiveCycler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ObjectiveProgress {
	private final int UID;
	private final Objective objective;
	private final Player player;
	private final QuestUpdater questtype;
	private final String questName;

	private int amountCompleted = 0;
	private ItemStack lastItem;
	private Location lastLocation;

	public ObjectiveProgress(int UID, Player player, String questName,
			ObjectiveCycler objectives) {
		this.UID = UID;
		this.player = player;
		this.questName = questName;
		this.objective = objectives.nextObjective();
		this.questtype = QuestAPI.getObjective(objective.getType());
	}

	public boolean update(Event event) {
		return getQuestUpdater().update(event, this);
	}

	public boolean isCompleted() {
		return getQuestUpdater().isCompleted(this);
	}

	public int getQuesterUID() {
		return UID;
	}

	public Player getPlayer() {
		return player;
	}

	public Objective getObjective() {
		return this.objective;
	}

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

	public String getQuestName() {
		return questName;
	}

	public Event.Type[] getEventTypes() {
		return getQuestUpdater().getEventTypes();
	}

	public QuestUpdater getQuestUpdater() {
		return questtype;
	}

}