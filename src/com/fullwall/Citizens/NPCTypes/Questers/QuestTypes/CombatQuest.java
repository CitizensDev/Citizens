package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class CombatQuest extends Quest {
	public CombatQuest(HumanNPC quester, Player player) {
		super(quester, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public QuestType getType() {
		return QuestType.PLAYER_COMBAT;
	}

	@Override
	public void updateProgress(Event event) {
		super.updateProgress(event);
	}
}
