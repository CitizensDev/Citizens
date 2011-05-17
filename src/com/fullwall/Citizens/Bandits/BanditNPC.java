package com.fullwall.Citizens.Bandits;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditNPC implements Toggleable {
	private HumanNPC npc;

	public BanditNPC(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void toggle() {
		npc.setBandit(!npc.isBandit());
	}

	@Override
	public boolean getToggle() {
		return npc.isBandit();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "bandit";
	}

	@Override
	public void saveState() {
		PropertyManager.get(getType()).saveState(npc);
	}

	@Override
	public void registerState() {
		PropertyManager.get(getType()).register(npc);
	}
}