package com.fullwall.Citizens.NPCTypes.Questers;

import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.BuildQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.CollectQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.CombatQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.DeliveryQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.DestroyQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.DistanceQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.EarnQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.HuntQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.LocationQuest;

public class QuestFactory {
	public static Quest newParsedQuest(QuestType type, String string) {
		switch (type) {
		case BUILD:
			return new BuildQuest().parse(string);
		case COLLECT:
			return new CollectQuest().parse(string);
		case DELIVERY:
			return new DeliveryQuest().parse(string);
		case DESTROY_BLOCK:
			return new DestroyQuest().parse(string);
		case EARN:
			return new EarnQuest().parse(string);
		case HUNT:
			return new HuntQuest().parse(string);
		case MOVE_DISTANCE:
			return new DistanceQuest().parse(string);
		case MOVE_LOCATION:
			return new LocationQuest().parse(string);
		case PLAYER_COMBAT:
			return new CombatQuest().parse(string);
		default:
			return null;
		}
	}
}
