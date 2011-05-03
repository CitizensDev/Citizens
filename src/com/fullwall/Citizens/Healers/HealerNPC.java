package com.fullwall.Citizens.Healers;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerNPC {
	private HumanNPC npc;
	// amount of "heal-power" remaining in the NPC, this drains as it is used
	private int healPower;
	
	public HealerNPC(HumanNPC npc){
		this.npc = npc;
	}
	
	public int getHealPower(){
		return healPower;
	}
	
	public void setHealPower(int healPower){
		this.healPower = healPower;
	}
}
