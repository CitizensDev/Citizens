package com.fullwall.Citizens.NPCTypes.Bandits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditNPC implements Toggleable, Clickable {
	private HumanNPC npc;
	private List<Integer> stealables = new ArrayList<Integer>();

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
	public List<Integer> getStealables() {
		return stealables;
	}

	/**
	 * Add an item ID to the list of items a bandit can steal
	 * 
	 * @param id
	 */
	public void addStealable(Integer id) {
		stealables.add(id);
	}

	/**
	 * Set the list of items that a bandit can steal
	 * 
	 * @param stealables
	 */
	public void setStealables(List<Integer> stealables) {
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

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		// TODO Auto-generated method stub
		
	}
}