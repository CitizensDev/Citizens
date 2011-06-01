package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class LocationQuest extends Quest {
	private Location location;

	public LocationQuest(HumanNPC quester, Player player) {
		super(quester, player);
	}

	public LocationQuest(HumanNPC quester, Player player, Location location) {
		super(quester, player);
		this.location = location;
	}

	@Override
	public QuestType getType() {
		return QuestType.MOVE_LOCATION;
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			if (ev.getPlayer().getLocation() == location) {
				completed = true;
				super.updateProgress(event);
			}
		}
	}

	@Override
	public Quest parse(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createString() {
		// TODO Auto-generated method stub
		return null;
	}
}