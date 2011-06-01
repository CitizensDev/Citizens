package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import com.fullwall.Citizens.NPCTypes.Questers.QuestProgress;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BuildQuest extends QuestProgress {
	public BuildQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof BlockPlaceEvent) {
			BlockPlaceEvent ev = (BlockPlaceEvent) event;
			if (ev.getBlockPlaced().getType() == getObjectiveItem().getType()) {
				amountCompleted += 1;
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return amountCompleted >= getObjectiveAmount();
	}
}