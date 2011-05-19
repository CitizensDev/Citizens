package com.fullwall.Citizens.NPCTypes.Healers;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Healer-NPC object
 */
public class HealerNPC implements Toggleable {
	private HumanNPC npc;
	private int strength = 10;
	private int level = 1;

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
		PropertyManager.get(getType()).saveState(npc);
	}

	@Override
	public void registerState() {
		PropertyManager.get(getType()).register(npc);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public int getMaxStrength() {
		return level * 10;
	}
}