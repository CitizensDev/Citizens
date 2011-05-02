package com.fullwall.Citizens.Questers;

public class Reward {
	private boolean iConomy = false;
	private int moneyAmount = 10;
	private int itemID = 322;
	private int rewardAmount = 1;

	public Reward(int moneyAmount) {
		this.iConomy = true;
		this.moneyAmount = moneyAmount;
	}

	public Reward(int itemID, int rewardAmount) {
		this.iConomy = false;
		this.itemID = itemID;
		this.rewardAmount = rewardAmount;
	}

	public boolean isIconomy() {
		return iConomy;
	}

	public int getMoneyAmount() {
		return moneyAmount;
	}

	public void setMoneyAmount(int moneyAmount) {
		this.moneyAmount = moneyAmount;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(int rewardAmount) {
		this.rewardAmount = rewardAmount;
	}
}