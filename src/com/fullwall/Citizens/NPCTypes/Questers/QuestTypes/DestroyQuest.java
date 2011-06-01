package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import com.fullwall.Citizens.NPCTypes.Questers.QuestProgress;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DestroyQuest extends QuestProgress {
	public DestroyQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof BlockBreakEvent) {
			BlockBreakEvent ev = (BlockBreakEvent) event;
			if (ev.getBlock().getType() == getObjectiveItem().getType()) {
				this.amountCompleted += 1;
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.amountCompleted >= getObjectiveAmount();
	}
}