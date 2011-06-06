package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestIncrementer;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BuildQuest extends QuestIncrementer {
	public BuildQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof BlockPlaceEvent) {
			BlockPlaceEvent ev = (BlockPlaceEvent) event;
			if (ev.getBlockPlaced().getType() == this.objective.getItem()
					.getType()) {
				this.getProgress().incrementCompleted(1);
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.getProgress().getAmount() >= this.objective.getAmount();
	}
}