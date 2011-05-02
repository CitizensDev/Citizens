package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.Goal;
import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DeliveryQuest implements Quest {
	private HumanNPC questGiver;
	private HumanNPC deliverToNPC;
	private ItemStack itemToDeliver;
	private Goal goal;

	public DeliveryQuest(HumanNPC questGiver, HumanNPC deliverToNPC,
			ItemStack itemToDeliver, Goal goal) {
		this.questGiver = questGiver;
		this.deliverToNPC = deliverToNPC;
		this.itemToDeliver = itemToDeliver;
		this.goal = goal;
	}

	public ItemStack getItemToDeliver() {
		return itemToDeliver;
	}

	public void setItemToDeliver(ItemStack itemToDeliver) {
		this.itemToDeliver = itemToDeliver;
	}

	public HumanNPC getDeliverToNPC() {
		return deliverToNPC;
	}

	public void setDeliverToNPC(HumanNPC deliverToNPC) {
		this.deliverToNPC = deliverToNPC;
	}

	@Override
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestType getType() {
		return QuestType.DELIVERY;
	}

	@Override
	public Goal getGoal() {
		return goal;
	}

	@Override
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	@Override
	public String getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	@Override
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
	}
}