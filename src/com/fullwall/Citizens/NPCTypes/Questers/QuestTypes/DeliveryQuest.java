package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DeliveryQuest extends Quest {
	private HumanNPC destination;

	public DeliveryQuest(HumanNPC quester, Player player, HumanNPC destination) {
		super(quester, player);
		this.destination = destination;
	}

	@Override
	public QuestType getType() {
		return QuestType.DELIVERY;
	}

	@Override
	public void updateProgress(Event event) {
		super.updateProgress(event);
	}
}
