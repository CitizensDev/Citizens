package com.temp.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

import com.temp.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.temp.NPCTypes.Questers.Quests.QuestIncrementer;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

public class HuntQuest extends QuestIncrementer {
	public HuntQuest(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		super(npc, player, questName, objectives);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof EntityDeathEvent) {
			EntityDeathEvent ev = (EntityDeathEvent) event;
			if (ev.getEntity() instanceof Monster
					|| ev.getEntity() instanceof Creature) {
				this.getProgress().incrementCompleted(1);
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.getProgress().getAmount() >= this.objective.getAmount();
	}
}