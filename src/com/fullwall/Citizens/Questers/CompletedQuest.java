package com.fullwall.Citizens.Questers;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public interface CompletedQuest {
	public QuestType getType();

	public String getName();

	public Player getPlayer();

	public HumanNPC getQuester();
}
