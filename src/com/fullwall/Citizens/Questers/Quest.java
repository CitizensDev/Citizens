package com.fullwall.Citizens.Questers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.iConomy;

public class Quest {
	private String questName;
	private HumanNPC questGiver;
	private Player player;
	private String progress;
	private QuestType type;
	private boolean isActive = false;
	private boolean isComplete;

	public Map<String, Boolean> queuedQuests = new HashMap<String, Boolean>();
	public Map<String, Boolean> activeQuests = new HashMap<String, Boolean>();
	private List<String> accepted = new ArrayList<String>();

	/**
	 * Constructs Quest object
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
	
	public Quest(String questName) {
		this.questName = questName;
	}
	
	/**
	 * 
	 * @return the name of the quest
	 */
	public String getName() {
		return questName;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	/**
	 * Add a quest to a queue
	 */
	public void addToQueue() {
		queuedQuests.put(this.getName(), true);
	}

	/**
	 * Accept a quest
	 * 
	 * @param player
	 */
	public void accept(Player player) {
		accepted.add(player.getName());
		queuedQuests.remove(this.getName());
		player.sendMessage("You have accepted the quest!");
	}

	/**
	 * Deny a quest
	 * 
	 * @param player
	 */
	public void deny(Player player) {
		queuedQuests.remove(this.getName());
		player.sendMessage("You have denied the quest!");
	}

	/**
	 * Start a quest
	 * 
	 * @param player
	 */
	public void start(Player player) {
		if (!isActive()) {
			setActive(true);
			if (queuedQuests.containsKey(this.getName())) {
				queuedQuests.remove(this.getName());
				activeQuests.put(this.getName(), true);
				accept(player);
				player.sendMessage("A quest has begun!");
			}
		}
	}

	/**
	 * Stop a quest
	 * 
	 * @param player
	 */
	public void stop(Player player) {
		if (isActive()) {
			setActive(false);
			activeQuests.put(this.getName(), false);
			player.sendMessage("The quest has ended.");
		}
	}
}
