package com.temp.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import com.temp.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.temp.NPCTypes.Questers.Quests.QuestIncrementer;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

public class DestroyQuest extends QuestIncrementer {
	public DestroyQuest(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		super(npc, player, questName, objectives);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof BlockBreakEvent) {
			BlockBreakEvent ev = (BlockBreakEvent) event;
			if (ev.getBlock().getType() == this.objective.getMaterial()) {
				this.getProgress().incrementCompleted(1);
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.getProgress().getAmount() >= this.objective.getAmount();
	}
}