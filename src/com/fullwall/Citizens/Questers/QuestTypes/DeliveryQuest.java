package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Quest type involving the delivery of an item to an NPC
 */
public class DeliveryQuest {
	private HumanNPC questGiver;
	private HumanNPC deliverToNPC;
	private ItemStack itemToDeliver;

	public DeliveryQuest(HumanNPC questGiver, HumanNPC deliverToNPC,
			ItemStack itemToDeliver) {
		this.questGiver = questGiver;
		this.deliverToNPC = deliverToNPC;
		this.itemToDeliver = itemToDeliver;
	}

	/**
	 * 
	 * @return the item that a player must deliver to an NPC
	 */
	public ItemStack getItemToDeliver() {
		return itemToDeliver;
	}

	/**
	 * 
	 * @param itemToDeliver
	 *            the item that a player must deliver to an NPC
	 */
	public void setItemToDeliver(ItemStack itemToDeliver) {
		this.itemToDeliver = itemToDeliver;
	}

	/**
	 * 
	 * @return the NPC that a player must deliver an item to
	 */
	public HumanNPC getDeliverToNPC() {
		return deliverToNPC;
	}

	/**
	 * 
	 * @param deliverToNPC
	 *            the NPC that a player must deliver an item to
	 */
	public void setDeliverToNPC(HumanNPC deliverToNPC) {
		this.deliverToNPC = deliverToNPC;
	}
}