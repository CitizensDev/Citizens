package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.fullwall.Citizens.NPCTypes.Questers.QuestProgress;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class CollectQuest extends QuestProgress {
	public CollectQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof PlayerPickupItemEvent) {
			PlayerPickupItemEvent ev = (PlayerPickupItemEvent) event;
			if (ev.getItem().getItemStack().getType() == getObjectiveItem()
					.getType()) {
				this.amountCompleted += ev.getItem().getItemStack().getAmount();
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.amountCompleted >= getObjectiveAmount();
	}
}