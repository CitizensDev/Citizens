package com.fullwall.Citizens.Questers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.iConomy;

public class Quest {
	private String questName;
	private HumanNPC startNPC;
	private Player player;
	private String progress;
	private String type;
	private int iConomyReward;
	private int itemRewardID;
	private int itemRewardAmount;
	private String prereq;
	private String desc;
	private String completionText;
	private boolean isActive = false;
	private boolean isComplete;

	public Map<String, Boolean> queuedQuests = new HashMap<String, Boolean>();
	public Map<String, Boolean> activeQuests = new HashMap<String, Boolean>();
	private List<String> accepted = new ArrayList<String>();

	/**
	 * Constructs Quest object
	 * 
	 * @param player
	 * @param startNPC
	 * @param type
	 * @param isComplete
	 */
	public Quest(Player player, HumanNPC startNPC, String type,
			int iConomyReward, int itemRewardID, int itemRewardAmount,
			String desc, String prereq, String completionText,
			boolean isComplete) {
		this.player = player;
		this.startNPC = startNPC;
		this.type = type;
		this.iConomyReward = iConomyReward;
		this.itemRewardID = itemRewardID;
		this.itemRewardAmount = itemRewardAmount;
		this.prereq = prereq;
		this.desc = desc;
		this.completionText = completionText;
		this.isComplete = isComplete;
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
	public HumanNPC getStartNPC() {
		return startNPC;
	}

	/**
	 * 
	 * @param startNPC
	 *            the NPC who gives out a quest
	 */
	public void setStartNPC(HumanNPC startNPC) {
		this.startNPC = startNPC;
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
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of quest
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the reward if using iConomy
	 * 
	 * @return
	 */
	public int getiConomyReward() {
		return iConomyReward;
	}

	/**
	 * Set the reward if using iConomy
	 * 
	 * @param iConomyReward
	 */
	public void setiConomyReward(int iConomyReward) {
		this.iConomyReward = iConomyReward;
	}

	/**
	 * Get the item reward ID
	 * 
	 * @return
	 */
	public int getItemRewardID() {
		return itemRewardID;
	}

	/**
	 * Set the item reward ID
	 * 
	 * @param itemRewardID
	 */
	public void setItemRewardID(int itemRewardID) {
		this.itemRewardID = itemRewardID;
	}

	/**
	 * Get the amount of an item to give for a reward
	 * 
	 * @return
	 */
	public int getItemRewardAmount() {
		return itemRewardAmount;
	}

	/**
	 * Set the amount of an item to give for a reward
	 * 
	 * @param itemRewardAmount
	 */
	public void setItemRewardAmount(int itemRewardAmount) {
		this.itemRewardAmount = itemRewardAmount;
	}

	/**
	 * Get a prerequisite quest that a player must complete before moving on to
	 * this quest
	 * 
	 * @return
	 */
	public String getPrerequisite() {
		return prereq;
	}

	/**
	 * Set the prerequisite quest that a player must complete before moving on
	 * to this quest
	 * 
	 * @param prereq
	 */
	public void setPrerequisite(String prereq) {
		this.prereq = prereq;
	}

	/**
	 * 
	 * @return the progress of a quest
	 */
	public String getProgress() {
		return progress;
	}

	/**
	 * Get the description of a quest
	 * 
	 * @return
	 */
	public String getDescription() {
		return desc;
	}

	/**
	 * Set the description of a quest
	 * 
	 * @param desc
	 */
	public void setDescription(String desc) {
		this.desc = desc;
	}

	/**
	 * Get the text that is outputted when a quest is complete
	 * 
	 * @return
	 */
	public String getCompletionText() {
		return completionText;
	}

	/**
	 * Set the text that is outputted at the end of a quest
	 * 
	 * @param completionText
	 */
	public void setCompletionText(String completionText) {
		this.completionText = completionText;
	}

	/**
	 * Gets if the quest is active
	 * 
	 * @return
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Set the quest's state as active/unactive
	 * 
	 * @param isActive
	 */
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
	public void giveItemReward(Player player) {
		player.getInventory().addItem(
				new ItemStack(getItemRewardID(), getItemRewardAmount()));
		player.sendMessage("You have been rewarded" + getItemRewardAmount()
				+ " " + getItemRewardID() + ".");
	}

	/**
	 * Give a player a reward using iConomy
	 * 
	 * @param player
	 * @param amount
	 */
	public void giveiConomyReward(Player player) {
		iConomy.getAccount(player.getName()).getHoldings()
				.add(getiConomyReward());
		player.sendMessage("You have been rewarded" + getiConomyReward() + ".");
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
