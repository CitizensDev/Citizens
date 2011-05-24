package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DistanceQuest extends Quest {
	private double distance;
	private double traveled;

	public DistanceQuest(HumanNPC quester, Player player, double distance) {
		super(quester, player);
		this.distance = distance;
		traveled = 0;
	}

	@Override
	public QuestType getType() {
		return QuestType.MOVE_DISTANCE;
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			// TODO complicated distance-finding equation here!
			if (traveled >= distance) {
				completed = true;
				super.updateProgress(event);
			}
		}
	}
}