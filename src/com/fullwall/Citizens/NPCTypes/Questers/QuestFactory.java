package com.fullwall.Citizens.NPCTypes.Questers;

import org.bukkit.entity.Player;

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
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuestFactory {
	public static Quest newParsedQuest(HumanNPC npc, Player player,
			QuestType type, String string) {
		switch (type) {
		case BUILD:
			return new BuildQuest(npc, player).parse(string);
		case COLLECT:
			return new CollectQuest(npc, player).parse(string);
		case DELIVERY:
			return new DeliveryQuest(npc, player).parse(string);
		case DESTROY_BLOCK:
			return new DestroyQuest(npc, player).parse(string);
		case EARN:
			return new EarnQuest(npc, player).parse(string);
		case HUNT:
			return new HuntQuest(npc, player).parse(string);
		case MOVE_DISTANCE:
			return new DistanceQuest(npc, player).parse(string);
		case MOVE_LOCATION:
			return new LocationQuest(npc, player).parse(string);
		case PLAYER_COMBAT:
			return new CombatQuest(npc, player).parse(string);
		default:
			return null;
		}
	}
}
