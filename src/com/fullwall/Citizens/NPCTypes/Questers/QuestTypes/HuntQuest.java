package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HuntQuest extends Quest {

	public HuntQuest(HumanNPC quester, Player player) {
		super(quester, player);
	}

	@Override
	public QuestType getType() {
		return QuestType.HUNT;
	}

	@Override
	public void updateProgress(Event event) {
		super.updateProgress(event);
	}
}
