package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DestroyQuest extends Quest {
	private int amount;
	private Material destroy;
	private int destroyed = 0;

	public DestroyQuest(HumanNPC quester, Player player, Material destroy,
			int amount) {
		super(quester, player);
		this.destroy = destroy;
		this.amount = amount;
	}

	@Override
	public QuestType getType() {
		return QuestType.DESTROY_BLOCK;
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof BlockBreakEvent) {
			BlockBreakEvent ev = (BlockBreakEvent) event;
			if (ev.getBlock().getType() == destroy) {
				destroyed += 1;
			}
			if (destroyed >= amount) {
				completed = true;
				super.updateProgress(event);
			}
		}
	}
}
