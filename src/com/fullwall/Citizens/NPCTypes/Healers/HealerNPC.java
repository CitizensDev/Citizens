package com.fullwall.Citizens.NPCTypes.Healers;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Healer-NPC object
 */
public class HealerNPC implements Toggleable {
	private HumanNPC npc;
	private int health = 10;
	private int level = 1;

	public HealerNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * 
	 * @return the remaining health that a healer has
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * 
	 * @param strength
	 *            the remaining strength of a healer
	 */
	public void setHealth(int health) {
		this.health = health;
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
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public int getMaxHealth() {
		return level * 10;
	}
}