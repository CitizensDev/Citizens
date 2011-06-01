package com.fullwall.Citizens.NPCTypes.Questers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public abstract class QuestProgress {
	protected String questName;
	protected final Player player;
	protected final HumanNPC quester;
	protected int amountCompleted;
	protected ItemStack lastItem;
	protected Location lastLocation;

	public QuestProgress(HumanNPC npc, Player player, String questName) {
		this.quester = npc;
		this.player = player;
		this.questName = questName;
	}

	public Objective getObjective() {
		return QuestManager.getQuest(this.questName).getObjective();
	}

	public ItemStack getObjectiveItem() {
		return getObjective().getItem();
	}

	public HumanNPC getObjectiveDestinationNPC() {
		return getObjective().getDestination();
	}

	public int getObjectiveAmount() {
		return getObjective().getAmount();
	}

	public Location getObjectiveLocation() {
		return getObjective().getLocation();
	}

	public abstract boolean isCompleted();

	public abstract void updateProgress(Event event);
}
