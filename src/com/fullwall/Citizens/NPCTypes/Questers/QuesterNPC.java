package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.HashMap;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterNPC implements Toggleable {
	private HumanNPC npc;
	private HashMap<String, Quest> quests = new HashMap<String, Quest>();

	/**
	 * Quester NPC object
	 * 
	 * @param npc
	 */
	public QuesterNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Add a quest
	 * 
	 * @param quest
	 */
	public void addQuest(Quest quest) {
		quests.put(quest.getName(), quest);
	}

	/**
	 * Get a quest
	 * 
	 * @param name
	 * @return
	 */
	public Quest getQuest(String name) {
		return quests.get(name);
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
		PropertyManager.get(getType()).saveState(npc);
	}

	@Override
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}
}