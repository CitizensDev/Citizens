package com.Citizens.NPCTypes.Pirates;

import org.bukkit.entity.Player;

import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.Interfaces.Clickable;

public class PirateNPC implements Clickable {
	@SuppressWarnings("unused")
	private final HumanNPC npc;
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