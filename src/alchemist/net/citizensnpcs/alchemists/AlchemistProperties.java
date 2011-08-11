package net.citizensnpcs.alchemists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

public class AlchemistProperties extends PropertyManager implements Properties {
	private static final String isAlchemist = ".alchemist.toggle";
	private static final String recipes = ".alchemist.recipes";

	private void saveRecipes(int UID, HashMap<Integer, String> recipeMap) {
		for (Entry<Integer, String> entry : recipeMap.entrySet()) {
			profiles.setString(UID + recipes + "." + entry.getKey(),
					entry.getValue());
		}
	}

	private HashMap<Integer, String> getRecipes(int UID) {
		HashMap<Integer, String> recipeMap = new HashMap<Integer, String>();
		for (String key : profiles.getKeys(UID + recipes)) {
			int itemID;
			try {
				itemID = Integer.parseInt(key);
			} catch (NumberFormatException e) {
				Messaging.log("Number expected at path: " + (UID + recipes)
						+ ". Make sure it is a number.");
				return null;
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
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("alchemist");
			Alchemist alchemist = npc.getType("alchemist");
			alchemist.setRecipes(getRecipes(npc.getUID()));
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
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.GENERAL, "alchemist.temp.node",
				true));
		return nodes;
	}
}