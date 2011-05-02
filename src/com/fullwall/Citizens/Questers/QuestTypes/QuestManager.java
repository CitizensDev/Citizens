package com.fullwall.Citizens.Questers.QuestTypes;

public class QuestManager {

	public enum QuestType {
		// go from one place to another
		LOCATION,
		// deliver an item to another NPC
		DELIVERY,
		// kill a certain amount of players
		PLAYER_COMBAT,
		// kill a certain type/number of monsters
		MONSTER_COMBAT,
		// travel a certain distance
		DISTANCE,
		// destroy a certain type/number of blocks
		DESTROY,
		// place a certain type/number of blocks
		BUILD
	}
}
