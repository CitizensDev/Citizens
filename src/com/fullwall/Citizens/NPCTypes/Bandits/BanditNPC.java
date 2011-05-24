package com.fullwall.Citizens.NPCTypes.Bandits;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditNPC implements Toggleable {
	private HumanNPC npc;
	private List<String> stealables = new ArrayList<String>();

	/**
	 * Bandit NPC object
	 * 
	 * @param npc
	 */
	public BanditNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Get the list of items that a bandit can steal
	 * 
	 * @return
	 */
	public List<String> getStealables() {
		for (int x = 0; x < stealables.size(); x++) {
			stealables.get(x).split(",");
		}
		return stealables;
	}

	/**
	 * Add an item ID to the list of items a bandit can steal
	 * 
	 * @param id
	 */
	public void addStealable(String id) {
		stealables.add(id);
	}

	/**
	 * Set the list of items that a bandit can steal
	 * 
	 * @param stealables
	 */
	public void setStealables(List<String> stealables) {
		this.stealables = stealables;
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
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}
}