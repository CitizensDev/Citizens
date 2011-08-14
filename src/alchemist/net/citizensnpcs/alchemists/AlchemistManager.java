package net.citizensnpcs.alchemists;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.utils.PageUtils;
import net.citizensnpcs.utils.PageUtils.PageInstance;

public class AlchemistManager {

	private static List<String> hasClickedOnce = new ArrayList<String>();

	public static boolean hasClickedOnce(String name) {
		return hasClickedOnce.contains(name);
	}

	public static void setClickedOnce(String name, boolean clicked) {
		if (clicked) {
			hasClickedOnce.add(name);
		} else {
			hasClickedOnce.remove(name);
		}
	}

	public static boolean sendRecipeMessage(Player player, HumanNPC npc,
			int page) {
		Alchemist alchemist = npc.getType("alchemist");
		int currentRecipe = alchemist.getCurrentRecipeID();
		PageInstance instance = PageUtils.newInstance(player);
		instance.header(ChatColor.GREEN
				+ StringUtils.listify(StringUtils.wrap("Ingredients for "
						+ MessageUtils.getMaterialName(currentRecipe)
						+ ChatColor.WHITE + " <%x/%y>")));
		if (alchemist.getRecipe(currentRecipe) == null) {
			Messaging.log("Current recipe ID: " + currentRecipe + "doesn't exist.");
			return false;
		}
		for (String item : alchemist.getRecipe(currentRecipe).split(",")) {
			instance.push(" - " + ChatColor.GREEN
					+ MessageUtils.getStackString(getStackByString(item)));
		}
		instance.process(page);
		return true;
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
		if (!StringUtils.isNumber(itemID)) {
			Messaging.sendError(player, "That is not a valid number.");
			return false;
		}
		int id = Integer.parseInt(itemID);
		if (Material.getMaterial(id) == null
				|| Material.getMaterial(id) == Material.AIR) {
			Messaging.sendError(player, MessageUtils.invalidItemIDMessage);
			return false;
		}
		return true;
	}
}