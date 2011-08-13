package net.citizensnpcs.alchemists;

import java.util.HashMap;

import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AlchemistManager {

	private static HashMap<String, Boolean> hasClickedOnce = new HashMap<String, Boolean>();

	public static boolean hasClickedOnce(String name) {
		return hasClickedOnce.get(name);
	}

	public static void setClickedOnce(String name, boolean clickedOnce) {
		hasClickedOnce.put(name, clickedOnce);
	}

	public static void sendRecipeMessage(Player player, HumanNPC npc) {
		Alchemist alchemist = npc.getType("alchemist");
		player.sendMessage(ChatColor.GREEN
				+ "Current Recipe: "
				+ StringUtils.wrap(MessageUtils.getMaterialName(alchemist
						.getCurrentRecipeID())));
		player.sendMessage(ChatColor.GREEN + "Type "
				+ StringUtils.wrap("/alchemist select [itemID]")
				+ " to select a different recipe.");
		player.sendMessage(ChatColor.GREEN + "Right-click again to craft.");
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

	public static boolean checkValidID(Player player, String itemID) {
		int id;
		try {
			id = Integer.parseInt(itemID);
		} catch (NumberFormatException e) {
			Messaging.sendError(player, "That is not a valid number.");
			return false;
		}
		if (Material.getMaterial(id) == null) {
			Messaging.sendError(player, MessageUtils.invalidItemIDMessage);
			return false;
		}
		return true;
	}
}