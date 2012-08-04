package net.citizensnpcs.healers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import com.google.common.collect.Lists;

public class HealerProperties extends PropertyManager implements Properties {
	private HealerProperties() {
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.healer.levelup", 100));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.healer.heal", 100));
		nodes.add(new Node("HealerGiveHealthItem", SettingsType.GENERAL,
				"healers.give-health-item", 35));
		nodes.add(new Node("HealerTakeHealthItem", SettingsType.GENERAL,
				"healers.take-health-item", 276));
		nodes.add(new Node("HealerHealthRegenIncrement", SettingsType.GENERAL,
				"healers.health-regen-increment", 12000));
		nodes.add(new Node("RegenHealerHealth", SettingsType.GENERAL,
				"healers.regen-health", true));
		return nodes;
	}

	@Override
	public Collection<String> getNodesForCopy() {
		return nodesForCopy;
	}

	@Override
	public boolean isEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isHealer);
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (isEnabled(npc)) {
			if (!npc.isType("healer"))
				npc.registerType("healer");
			Healer healer = npc.getType("healer");
			healer.load(profiles, npc.getUID());
		}
		saveState(npc);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("healer");
			setEnabled(npc, is);
			if (is) {
				Healer healer = npc.getType("healer");
				healer.save(profiles, npc.getUID());
			}
		}
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isHealer, value);
	}

	public static final HealerProperties INSTANCE = new HealerProperties();

	private static final String isHealer = ".healer.toggle";

	private static final List<String> nodesForCopy = Lists.newArrayList(
			"healer.toggle", "healer.health", "healer.level");
}