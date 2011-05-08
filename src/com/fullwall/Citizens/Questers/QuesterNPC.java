package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Utils.QuesterPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterNPC implements Toggleable {
	private HumanNPC npc;

	/**
	 * NPC Quester object
	 * 
	 * @param npc
	 */
	public QuesterNPC(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void toggle() {
		npc.setQuester(!npc.isQuester());
	}

	@Override
	public boolean getToggle() {
		return npc.isQuester();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "quester";
	}

	@Override
	public void saveState() {
		QuesterPropertyPool.saveState(npc);
	}

	@Override
	public void registerState() {
		QuesterPropertyPool.saveQuester(npc.getUID(), true);
	}
}
