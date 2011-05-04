package com.fullwall.Citizens.Healers;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Healer-NPC object
 */
public class HealerNPC {
	private HumanNPC npc;
	private int healPower;
	
	public HealerNPC(HumanNPC npc){
		this.npc = npc;
	}
	
	/**
	 * 
	 * @return the remaining power that a healer has
	 */
	public int getHealPower(){
		return healPower;
	}
	
	/**
	 * 
	 * @param healPower the remaining power of a healer
	 */
	public void setHealPower(int healPower){
		this.healPower = healPower;
	}
}
