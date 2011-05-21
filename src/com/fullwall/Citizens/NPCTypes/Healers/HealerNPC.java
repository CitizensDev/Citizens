package com.fullwall.Citizens.NPCTypes.Healers;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerNPC implements Toggleable {
	private HumanNPC npc;
	private int health = 10;
	private int level = 1;

	/**
	 * Healer NPC object
	 * 
	 * @param npc
	 */
	public HealerNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Get the health of a healer NPC
	 * 
	 * @return
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Set the health of a healer NPC
	 * 
	 * @param
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Get the maximum health of a healer NPC
	 * 
	 * @return
	 */
	public int getMaxHealth() {
		return level * 10;
	}

	/**
	 * Get the level of a healer NPC
	 * 
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Set the level of a healer NPC
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
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
}