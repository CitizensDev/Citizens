package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.fullwall.Citizens.NPCTypes.Questers.QuestProgress;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DistanceQuest extends QuestProgress {
	public DistanceQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			// Considering doing this a different way.
			// Object for distance type?
			/*double x = ev.getTo().getX() - ev.getFrom().getX();
			double z = ev.getTo().getZ() - ev.getFrom().getZ();
			traveled = Math.round(Math.sqrt(x * x + z * z) * 100);
			double m = traveled / 100D;
			double km = m / 1000D;
			if (km > 0.5D)
				; // Kilometres
			if (m > 0.5D)
				; // Metres
			else
				; // Centimetres
			*/
		}
	}

	@Override
	public boolean isCompleted() {
		return this.amountCompleted >= getObjectiveAmount();
	}
}