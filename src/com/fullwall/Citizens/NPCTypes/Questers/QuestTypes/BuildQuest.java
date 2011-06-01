package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BuildQuest extends Quest {
	private int amount;
	private Material build;
	private int built = 0;

	public BuildQuest() {
		super();
	}

	public BuildQuest(HumanNPC quester, Player player, int amount) {
		super(quester, player);

		this.amount = amount;
	}

	@Override
	public QuestType getType() {
		return QuestType.BUILD;
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof BlockPlaceEvent) {
			BlockPlaceEvent ev = (BlockPlaceEvent) event;
			if (ev.getBlockPlaced().getType() == build) {
				built += 1;
			}
			if (built >= amount) {
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