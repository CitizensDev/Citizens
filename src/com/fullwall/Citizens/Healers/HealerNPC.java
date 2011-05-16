package com.fullwall.Citizens.Healers;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Healer-NPC object
 */
public class HealerNPC implements Toggleable {
	private HumanNPC npc;
	private int strength;

	public HealerNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * 
	 * @return the remaining strength that a healer has
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * 
	 * @param strength
	 *            the remaining strength of a healer
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}

	@Override
	public void toggle() {
		npc.setHealer(!npc.isHealer());
		if (npc.isHealer()) {
			HealerPropertyPool.saveLevel(npc.getUID(), 1);
			HealerPropertyPool.saveStrength(npc.getUID(), 10);
		}
	}

	@Override
	public boolean getToggle() {
		return npc.isHealer();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "healer";
	}

	@Override
	public void saveState() {
		HealerPropertyPool.saveState(npc);
	}

	@Override
	public void registerState() {
		HealerPropertyPool.saveHealer(npc.getUID(), true);
	}
}