package net.citizensnpcs.blacksmiths;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import com.google.common.collect.Lists;

public class BlacksmithProperties extends PropertyManager implements Properties {
	private BlacksmithProperties() {
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.leather", 0.25));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.gold", 0.50));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.chainmail", 0.75));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.iron", 1));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.diamond", 1.25));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.wood", 0.25));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.gold", 0.50));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.stone", 0.75));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.iron", 1));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.diamond", 1.25));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.misc", 0.50));
		return nodes;
	}

	@Override
	public Collection<String> getNodesForCopy() {
		return Lists.newArrayList();
	}

	@Override
	public boolean isEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isBlacksmith);
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (isEnabled(npc)) {
			if (!npc.isType("blacksmith"))
				npc.registerType("blacksmith");
			Blacksmith blacksmith = npc.getType("blacksmith");
			blacksmith.load(profiles, npc.getUID());
		}
		saveState(npc);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isType("blacksmith"));
			Blacksmith blacksmith = npc.getType("blacksmith");
			blacksmith.save(profiles, npc.getUID());
		}
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isBlacksmith, value);
	}

	public static final BlacksmithProperties INSTANCE = new BlacksmithProperties();

	private static final String isBlacksmith = ".blacksmith.toggle";
}