package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EarnQuest extends Quest {

	private double amount;

	public EarnQuest(HumanNPC quester, Player player, double amount) {
		super(quester, player);
		this.amount = amount;
	}

	@Override
	public QuestType getType() {
		return QuestType.EARN;
	}

	@Override
	public void updateProgress(Event ev) {
		super.updateProgress(ev);
	}

}
