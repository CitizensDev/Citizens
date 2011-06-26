package com.citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.citizens.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.citizens.NPCTypes.Questers.Quests.QuestIncrementer;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;

public class DistanceQuest extends QuestIncrementer {
	private double traveled = 0;

	public DistanceQuest(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		super(npc, player, questName, objectives);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			// Considering doing this a different way.
			// Object for distance type? DistanceType?
			double x = ev.getTo().getX() - ev.getFrom().getX();
			double z = ev.getTo().getZ() - ev.getFrom().getZ();
			traveled = Math.round(Math.sqrt(x * x + z * z) * 100);
			double m = traveled / 100D;
			double km = m / 1000D;
			if (km > 0.5D) {

			}// Kilometres if (m> 0.5D) ; // Metres else ; // Centimetres
		}
	}

	@Override
	public boolean isCompleted() {
		return this.getProgress().getAmount() >= this.objective.getAmount();
	}
}