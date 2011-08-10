package net.citizensnpcs.alchemists;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.citizensnpcs.resources.npclib.HumanNPC;

public class AlchemistManager {

	private static HashMap<String, Boolean> hasClickedOnce = new HashMap<String, Boolean>();

	public static boolean hasClickedOnce(String name) {
		return hasClickedOnce.get(name);
	}

	public static void setClickedOnce(String name, boolean clickedOnce) {
		hasClickedOnce.put(name, clickedOnce);
	}
	
	public static void sendRecipeMessage(Player player, HumanNPC npc) {
		// TODO
		// Alchemist alchemist = npc.getType("alchemist");
	}
}