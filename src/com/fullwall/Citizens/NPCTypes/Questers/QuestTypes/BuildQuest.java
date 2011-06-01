package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BuildQuest extends Quest {
	private Material build;
	private int amount;
	private int built = 0;

	public BuildQuest(HumanNPC quester, Player player) {
		super(quester, player);
	}

	public BuildQuest(HumanNPC quester, Player player, int amount,
			Material material) {
		super(quester, player);
		this.build = material;
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
		return super.getString() + Citizens.separatorChar + this.build.getId()
				+ Citizens.separatorChar + this.amount + Citizens.separatorChar
				+ this.built;
	}
}