package com.temp.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.temp.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.temp.NPCTypes.Questers.Quests.QuestIncrementer;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

public class CollectQuest extends QuestIncrementer {
	public CollectQuest(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		super(npc, player, questName, objectives);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof PlayerPickupItemEvent) {
			PlayerPickupItemEvent ev = (PlayerPickupItemEvent) event;
			if (ev.getItem().getItemStack().getType() == this.objective
					.getMaterial()) {
				this.getProgress().incrementCompleted(
						ev.getItem().getItemStack().getAmount());
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.getProgress().getAmount() >= this.objective.getAmount();
	}
}