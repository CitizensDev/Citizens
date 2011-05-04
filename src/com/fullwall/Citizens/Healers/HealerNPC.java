package com.fullwall.Citizens.Healers;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Healer-NPC object
 */
public class HealerNPC {
	private HumanNPC npc;
	private int strength;
	
	public HealerNPC(HumanNPC npc){
		this.npc = npc;
	}
	
	/**
	 * 
	 * @return the remaining strength that a healer has
	 */
	public int getStrength(){
		return strength;
	}
	
	/**
	 * 
	 * @param strength the remaining strength of a healer
	 */
	public void setStrength(int strength){
		this.strength = strength;
	}
}
