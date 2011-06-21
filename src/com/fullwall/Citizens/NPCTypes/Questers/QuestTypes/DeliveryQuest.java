package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.Events.NPCTargetEvent;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestIncrementer;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DeliveryQuest extends QuestIncrementer {
	public DeliveryQuest(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		super(npc, player, questName, objectives);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof NPCTargetEvent) {
			NPCTargetEvent e = (NPCTargetEvent) event;
			if (e.getTarget().getEntityId() == this.player.getEntityId()) {
				if (((HumanNPC) e.getEntity()).getUID() == this.objective
						.getDestinationNPCID()) {
					Player player = (Player) e.getTarget();
					if (player.getItemInHand().getType() == this.objective
							.getMaterial()) {
						this.getProgress().setLastItem(player.getItemInHand());
					}
				}
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.getProgress().getAmount() >= this.objective.getAmount();
	}
}