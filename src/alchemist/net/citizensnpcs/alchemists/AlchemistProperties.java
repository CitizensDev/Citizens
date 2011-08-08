package net.citizensnpcs.alchemists;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.SettingsManager.SettingsType;
import net.citizensnpcs.resources.npclib.HumanNPC;

public class AlchemistProperties extends PropertyManager implements Properties {
	private static final String isAlchemist = ".alchemist.toggle";

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("alchemist");
			setEnabled(npc, is);
			if (is) {
				// Alchemist alchemist = npc.getType("alchemist");
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {

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
		// TODO Auto-generated method stub

	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.GENERAL,
				"alchemist.temp.node", true));
		return nodes;
	}
}