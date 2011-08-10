package net.citizensnpcs.alchemists;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;

public class Alchemist extends CitizensNPC {
	// Integer = ID of result item, String = recipe
	private HashMap<Integer, String> recipes = new HashMap<Integer, String>();

	public HashMap<Integer, String> getRecipes() {
		return recipes;
	}

	public void setRecipes(HashMap<Integer, String> recipes) {
		this.recipes = recipes;
	}

	public String getRecipe(int result) {
		return recipes.get(result);
	}

	@Override
	public String getType() {
		return "alchemist";
	}

	@Override
	public Properties getProperties() {
		return new AlchemistProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new AlchemistCommands();
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (!PermissionManager.generic(player,
				"citizens.alchemist.use.interact")) {
			Messaging.sendError(player, MessageUtils.noPermissionsMessage);
			return;
		}
		if (recipes == null) {
			Messaging.sendError(player, npc.getStrippedName()
					+ " has no recipes.");
			return;
		}
		if (!AlchemistManager.hasClickedOnce(player.getName())) {
			AlchemistManager.sendRecipeMessage(player, npc);
			AlchemistManager.setClickedOnce(player.getName(), true);
			return;
		}
		InventoryUtils.showInventory(npc, player);
		AlchemistManager.setClickedOnce(player.getName(), false);
	}
}