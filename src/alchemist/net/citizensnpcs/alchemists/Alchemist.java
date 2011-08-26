package net.citizensnpcs.alchemists;

import java.util.HashMap;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Alchemist extends CitizensNPC {
	private HashMap<Integer, String> recipes = new HashMap<Integer, String>();
	private int currentRecipeID = 0;

	public HashMap<Integer, String> getRecipes() {
		return recipes;
	}

	public void setRecipes(HashMap<Integer, String> recipes) {
		this.recipes = recipes;
	}

	public String getRecipe(int itemID) {
		return recipes.get(itemID);
	}

	public void addRecipe(int itemID, String recipe) {
		this.recipes.put(itemID, recipe);
	}

	public int getCurrentRecipeID() {
		return this.currentRecipeID;
	}

	public void setCurrentRecipeID(int currentRecipeID) {
		this.currentRecipeID = currentRecipeID;
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (!PermissionManager.generic(player,
				"citizens.alchemist.use.interact")) {
			Messaging.sendError(player, MessageUtils.noPermissionsMessage);
			return;
		}
		if (!AlchemistManager.hasClickedOnce(player.getName())) {
			if (recipes.size() == 0) {
				Messaging.sendError(player, npc.getStrippedName()
						+ " has no recipes.");
				return;
			}
			if (AlchemistManager.sendRecipeMessage(player, npc, 1)) {
				player.sendMessage(ChatColor.GREEN + "Type "
						+ StringUtils.wrap("/alchemist view (page)")
						+ " for more ingredients.");
				player.sendMessage(ChatColor.GREEN
						+ "Right-click again to craft.");
			}
			AlchemistManager.setClickedOnce(player.getName(), true);
			return;
		}
		AlchemistTask task = new AlchemistTask(npc);
		Bukkit.getServer().getScheduler()
				.scheduleSyncRepeatingTask(Citizens.plugin, task, 2, 1);
		InventoryUtils.showInventory(npc, player);
		AlchemistManager.setClickedOnce(player.getName(), false);
		// TODO requires inventory events to cancel tasks when inventory closes
	}

	@Override
	public CitizensNPCType getType() {
		return new AlchemistType();
	}
}