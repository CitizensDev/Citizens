package net.citizensnpcs.alchemists;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Alchemist extends CitizensNPC {
	private Map<Integer, String> recipes = new HashMap<Integer, String>();
	private int currentRecipeID = 0;

	public void addRecipe(int itemID, String recipe) {
		this.recipes.put(itemID, recipe);
	}

	public int getCurrentRecipeID() {
		return this.currentRecipeID;
	}

	public String getRecipe(int itemID) {
		return recipes.get(itemID);
	}

	public Map<Integer, String> getRecipes() {
		return recipes;
	}

	@Override
	public CitizensNPCType getType() {
		return new AlchemistType();
	}

	@Override
	public void load(DataKey root) {
		currentRecipeID = root.getInt("recipes.current", 0);
		recipes.clear();
		root = root.getRelative("recipes");
		for (DataKey key : root.getIntegerSubKeys()) {
			recipes.put(Integer.parseInt(key.name()), key.getString(""));
		}
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (!PermissionManager.hasPermission(player,
				"citizens.alchemist.use.interact")) {
			Messaging.sendError(player, MessageUtils.noPermissionsMessage);
			return;
		}
		if (!AlchemistManager.hasClickedOnce(player.getName())) {
			if (recipes.size() == 0) {
				Messaging.sendError(player, npc.getName() + " has no recipes.");
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
	public void save(DataKey root) {
		root.setInt("recipes.current", currentRecipeID);
		root = root.getRelative("recipes");
		for (Entry<Integer, String> entry : recipes.entrySet()) {
			root.setString(Integer.toString(entry.getKey()), entry.getValue());
		}
	}

	public void setCurrentRecipeID(int currentRecipeID) {
		this.currentRecipeID = currentRecipeID;
	}

	public void setRecipes(Map<Integer, String> recipes) {
		this.recipes = recipes;
	}
}