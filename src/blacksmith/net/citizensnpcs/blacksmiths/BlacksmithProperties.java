package net.citizensnpcs.blacksmiths;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.SettingsManager.SettingsType;
import net.citizensnpcs.resources.npclib.HumanNPC;

public class BlacksmithProperties extends PropertyManager implements Properties {
	private final String isBlacksmith = ".blacksmith.toggle";

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isType("blacksmith"));
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("blacksmith");
		}
		saveState(npc);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isBlacksmith, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isBlacksmith);
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isBlacksmith)) {
			profiles.setString(nextUID + isBlacksmith,
					profiles.getString(UID + isBlacksmith));
		}
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.leather", 0.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.gold", 0.50));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.chainmail", 0.75));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.iron", 1));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.diamond", 1.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.wood", 0.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.gold", 0.50));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.stone", 0.75));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.iron", 1));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.diamond", 1.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.misc", 0.50));
		return nodes;
	}
}