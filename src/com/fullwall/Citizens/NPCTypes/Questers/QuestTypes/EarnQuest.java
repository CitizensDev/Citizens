package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.events.AccountUpdateEvent;

public class EarnQuest extends Quest {
	private double amount;

	public EarnQuest(HumanNPC quester, Player player) {
		super(quester, player);
	}

	public EarnQuest(HumanNPC quester, Player player, double amount) {
		super(quester, player);
		this.amount = amount;
	}

	@Override
	public QuestType getType() {
		return QuestType.EARN;
	}

	@Override
	public void updateProgress(Event event) {
		// Note: No idea if this actually works
		if (event instanceof AccountUpdateEvent) {
			AccountUpdateEvent ev = (AccountUpdateEvent) event;
			double previousAmount = ev.getPrevious();
			double currentAmount = ev.getBalance();
			if (previousAmount - currentAmount >= amount) {
				completed = true;
				super.updateProgress(ev);
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