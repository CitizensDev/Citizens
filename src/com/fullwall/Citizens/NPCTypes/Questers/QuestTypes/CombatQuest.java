package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

import com.fullwall.Citizens.NPCTypes.Questers.QuestProgress;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class CombatQuest extends QuestProgress {
	public CombatQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof EntityDeathEvent) {
			EntityDeathEvent ev = (EntityDeathEvent) event;
			if (ev.getEntity() instanceof Player) {
				this.amountCompleted += 1;
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.amountCompleted >= getObjectiveAmount();
	}
}