package com.fullwall.Citizens.Blacksmiths;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Utils.BlacksmithPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithNPC implements Toggleable {
	private HumanNPC npc;

	/**
	 * NPC Blacksmith object
	 * 
	 * @param npc
	 */
	public BlacksmithNPC(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void toggle() {
		npc.setBlacksmith(!npc.isBlacksmith());
	}

	@Override
	public boolean getToggle() {
		return npc.isBlacksmith();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "blacksmith";
	}

	@Override
	public void saveState() {
		BlacksmithPropertyPool.saveState(npc);
	}

	@Override
	public void registerState() {
		BlacksmithPropertyPool.saveBlacksmith(npc.getUID(), true);
	}
}