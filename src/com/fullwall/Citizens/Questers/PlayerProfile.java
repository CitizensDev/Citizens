package com.fullwall.Citizens.Questers;

public class PlayerProfile {
	private String playerName;
	private int rank;
	private int questsComplete;

	/**
	 * Player's quest profile
	 * 
	 * @param playerName
	 */
	public PlayerProfile(String playerName) {
		this.playerName = playerName;
		rank = 1;
		questsComplete = 0;
	}

	public String getName() {
		return playerName;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void upOneRank() {
		rank++;
	}

	public int getQuestsComplete() {
		return questsComplete;
	}

	public void setQuestsComplete(int questsComplete) {
		this.questsComplete = questsComplete;
	}
}