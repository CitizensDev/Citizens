package com.fullwall.Citizens.NPCTypes.Questers;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public interface CompletedQuest {
	public QuestType getType();

	public String getName();

	public Player getPlayer();

	public HumanNPC getQuester();
}
