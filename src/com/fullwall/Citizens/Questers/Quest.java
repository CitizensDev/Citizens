package com.fullwall.Citizens.Questers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.iConomy;

public class Quest {
	private HumanNPC questGiver;
	private Player player;
	private String progress;
	private QuestType type;
	private boolean isComplete;

	/**
	 * Contstructs Quest object
	 * 
	 * @param player
	 * @param questGiver
	 * @param type
	 * @param isComplete
	 */
	public Quest(Player player, HumanNPC questGiver, QuestType type,
			boolean isComplete) {
		this.player = player;
		this.questGiver = questGiver;
		this.type = type;
		this.isComplete = isComplete;
	}

	/**
	 * 
	 * @return the NPC who gives out a quest
	 */
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	/**
	 * 
	 * @param questGiver
	 *            the NPC who gives out a quest
	 */
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
	}

	/**
	 * 
	 * @return the player who is doing the questing
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * 
	 * @return the type of quest it is
	 */
	public QuestType getType() {
		return type;
	}

	/**
	 * Sets the type of quest
	 * 
	 * @param type
	 */
	public void setType(QuestType type) {
		this.type = type;
	}

	/**
	 * 
	 * @return the progress of an active quest
	 */
	public String getProgress(Quest quest) {
		return progress;
	}

	/**
	 * 
	 * @return true if a quest is complete
	 */
	public boolean isComplete() {
		return isComplete;
	}

	/**
	 * Set a quest's completion status
	 * 
	 * @param isComplete
	 */
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	/**
	 * Give a player a reward using iConomy
	 * 
	 * @param player
	 * @param amount
	 */
	public void giveItemReward(Player player, int itemID, int amount) {
		player.getInventory().addItem(new ItemStack(itemID, amount));
		player.sendMessage("You have been rewarded" + amount + itemID + ".");
	}

	/**
	 * Give a player a reward using iConomy
	 * 
	 * @param player
	 * @param amount
	 */
	public void giveiConomyReward(Player player, int amount) {
		iConomy.getAccount(player.getName()).getHoldings().add(amount);
		player.sendMessage("You have been rewarded" + amount + ".");
	}
}
