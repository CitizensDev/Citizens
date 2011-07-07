package com.citizens.npctypes.interfaces;

import org.bukkit.entity.Player;

import com.citizens.resources.npclib.HumanNPC;

public interface Clickable {
	/**
	 * Left-clicking an NPC
	 * 
	 * @return
	 */
	public void onLeftClick(Player player, HumanNPC npc);

	/**
	 * Right-clicking an NPC
	 * 
	 * @return
	 */
	public void onRightClick(Player player, HumanNPC npc);
}