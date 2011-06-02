package com.fullwall.Citizens.NPCTypes.Evil;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EvilNPC implements Clickable {

	/**
	 * Massive Wall of Text Explaining Evil NPCs
	 */
	// This NPC type roams randomly throughout the world. They should be a
	// slightly rare type, maybe only 1 or 2 spawned at a time. When they spawn,
	// they are given a random name from a list of names (configurable? - yes).
	// They go around killing players and looting their junk. If you walk close
	// to one, it gives you one of many nasty remarks (ex:
	// "That creeper and I had a good time kicking your mom's ass") - also
	// configurable. Players can befriend these misunderstood NPCs
	// by right clicking with an item (configurable? also if you want to use
	// this). When right-clicked, the player is given the option to turn it into
	// any of the available NPC types. Evil NPCs are not toggleable, so there is
	// no going back when you
	// turn it into another type. Their data is not like any other NPC type, so
	// using the
	// PropertyHandler may not be the right choice - probably just a file that
	// lists perhaps name, ticks lived, location, inventory (need to delete when
	// the ticks lived > max ticks). The only PropertyHandler class I can think
	// of is an evil.citizens file, so we can save the NPC as evil on server
	// restarts.
	// Otherwise, we'll use regular .txt files for the configurable
	// names and rude remarks. When an Evil NPC spawns, one random name is
	// pulled from "plugins/Citizens/Evil NPCs/names.citizens" and given to the
	// NPC.
	// Of course, we can use the same functions that are used for basic NPC's
	// text for the random rude
	// remarks -> need to be made more generic OFC.

	@SuppressWarnings("unused")
	private HumanNPC npc;

	/**
	 * Evil NPC object
	 * 
	 * @param npc
	 */
	public EvilNPC(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (player.getItemInHand().getTypeId() == Constants.evilNPCTameItem) {
			// TODO tame Evil NPC here
		}
	}
}