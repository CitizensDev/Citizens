package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class CollectQuest extends Quest {

	private Material collect;
	private int amount;
	private int collected = 0;

	public CollectQuest(HumanNPC quester, Player player, Material collect,
			int amount) {
		super(quester, player);
		this.quester = quester;
		this.player = player;
		this.collect = collect;
		this.amount = amount;
	}

	@Override
	public QuestType getType() {
		return QuestType.COLLECT;
	}

	@Override
	public void updateProgress(Event ev) {
		if (ev instanceof PlayerPickupItemEvent) {
			PlayerPickupItemEvent event = (PlayerPickupItemEvent) ev;
			if (event.getItem().getItemStack().getType() == collect) {
				collected += event.getItem().getItemStack().getAmount();
			}
			if (collected >= amount) {
				completed = true;
				super.updateProgress(event);
			}
		}
	}
}
