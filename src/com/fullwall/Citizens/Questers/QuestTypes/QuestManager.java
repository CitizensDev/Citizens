package com.fullwall.Citizens.Questers.QuestTypes;

public class QuestManager {

	public enum QuestType {
		/**
		 * go from one place to another
		 * 
		 * @see com.fullwall.Citizens.Questers.QuestTypes.LocationQuest
		 */
		LOCATION,
		/**
		 * deliver an item to another NPC
		 * 
		 * @see com.fullwall.Citizens.Questers.QuestTypes.DeliveryQuest
		 */
		DELIVERY,
		/**
		 * kill a certain amount of players
		 * 
		 * @see com.fullwall.Citizens.Questers.QuestTypes.PlayerCombatQuest
		 */
		PLAYER_COMBAT,
		/**
		 * kill a certain type/number of monsters
		 * 
		 * @see com.fullwall.Citizens.Questers.QuestTypes.MonsterCombatQuest
		 */
		MONSTER_COMBAT,
		/**
		 * travel a certain distance
		 * 
		 * @see com.fullwall.Citizens.Questers.QuestTypes.DistanceQuest
		 */
		DISTANCE,
		/**
		 * destroy a certain type/number of blocks
		 * 
		 * @see com.fullwall.Citizens.Questers.QuestTypes.DestroyQuest
		 */
		DESTROY,
		/**
		 * place a certain type/number of blocks
		 * 
		 * @see com.fullwall.Citizens.Questers.QuestTypes.BuildQuest
		 */
		BUILD;
		
		/**
		 * Changes a quest type enum to a string value
		 * 
		 * @param op
		 * @return
		 */
		public static String getString(QuestType type) {
			return type.toString().toLowerCase().replace("_", "-");
		}
	}
}