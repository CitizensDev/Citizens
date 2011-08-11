package net.citizensnpcs.alchemists;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

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

	public static ItemStack getStackByString(String string) {
		String[] required = string.split(":");
		int itemID = StringUtils.parse(required[0]);
		int amount = 1;
		if (required.length == 2) {
			amount = StringUtils.parse(required[1]);
		}
		return new ItemStack(itemID, amount);
	}
}