package net.citizensnpcs.alchemists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.api.Node;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

public class AlchemistProperties extends PropertyManager implements Properties {
	private static final String isAlchemist = ".alchemist.toggle";
	private static final String recipes = ".alchemist.recipes";
	private static final String currentRecipe = recipes + ".current";

	public static final AlchemistProperties INSTANCE = new AlchemistProperties();

	private AlchemistProperties() {
	}

	private void saveCurrentRecipe(int UID, int currentRecipeID) {
		profiles.setInt(UID + currentRecipe, currentRecipeID);
	}

	private int getCurrentRecipe(int UID) {
		return profiles.getInt(UID + currentRecipe);
	}

	private void saveRecipes(int UID, HashMap<Integer, String> recipeMap) {
		for (Entry<Integer, String> entry : recipeMap.entrySet()) {
			profiles.setString(UID + recipes + "." + entry.getKey(),
					entry.getValue());
		}
	}

	private HashMap<Integer, String> getRecipes(int UID) {
		HashMap<Integer, String> recipeMap = new HashMap<Integer, String>();
		if (profiles.getKeys(UID + recipes).size() == 0) {
			return recipeMap;
		}
		for (String key : profiles.getKeys(UID + recipes)) {
			int itemID;
			try {
				itemID = Integer.parseInt(key);
			} catch (NumberFormatException e) {
				Messaging.log("Number expected at path: " + (UID + recipes)
						+ ". Make sure it is a number.");
				return recipeMap;
			}
			recipeMap.put(itemID,
					profiles.getString(UID + recipes + "." + itemID));
		}
		return recipeMap;
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("alchemist");
			setEnabled(npc, is);
			if (is) {
				Alchemist alchemist = npc.getType("alchemist");
				saveRecipes(npc.getUID(), alchemist.getRecipes());
				saveCurrentRecipe(npc.getUID(), alchemist.getCurrentRecipeID());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("alchemist");
			Alchemist alchemist = npc.getType("alchemist");
			alchemist.setRecipes(getRecipes(npc.getUID()));
			alchemist.setCurrentRecipeID(getCurrentRecipe(npc.getUID()));
		}
		saveState(npc);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isAlchemist, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isAlchemist);
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isAlchemist)) {
			profiles.setString(nextUID + isAlchemist,
					profiles.getString(UID + isAlchemist));
		}
		if (profiles.pathExists(UID + recipes)) {
			saveRecipes(nextUID,
					((Alchemist) NPCManager.get(UID).getType("alchemist"))
							.getRecipes());
		}
		if (profiles.pathExists(UID + currentRecipe)) {
			profiles.setInt(nextUID + currentRecipe,
					profiles.getInt(UID + currentRecipe));
		}
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("AlchemistFailedCraftChance", SettingsType.GENERAL,
				"general.alchemists.failed-craft-chance", 10));
		nodes.add(new Node("AlchemistFailedCraftItem", SettingsType.GENERAL,
				"items.alchemists.failed-craft-item", 263));
		return nodes;
	}
}