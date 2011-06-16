package com.fullwall.Citizens.NPCTypes.Pirates;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PirateNPC implements Clickable {
	@SuppressWarnings("unused")
	private HumanNPC npc;
	private boolean boating = true;

	/**
	 * Pirate NPC object
	 * 
	 * @param npc
	 */
	public PirateNPC(HumanNPC npc) {
		this.npc = npc;
	}

	public boolean isBoating() {
		return boating;
	}

	public void setBoating(boolean boating) {
		this.boating = boating;
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
	}
}