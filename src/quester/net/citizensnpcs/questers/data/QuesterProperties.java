package net.citizensnpcs.questers.data;

import java.util.Collection;
import java.util.List;

import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.questers.Quester;
import net.citizensnpcs.resources.npclib.HumanNPC;

import com.google.common.collect.Lists;

public class QuesterProperties extends PropertyManager implements Properties {
	private QuesterProperties() {
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = Lists.newArrayList();
		nodes.add(new Node("QuestSaveDelay", SettingsType.GENERAL,
				"quests.save.command-delay-ms", 5000));
		nodes.add(new Node("ItemExploitCheckDelay", SettingsType.GENERAL,
				"quests.exploits.item-pickup.check-delay", 400));
		nodes.add(new Node("CombatExploitTimes", SettingsType.GENERAL,
				"quests.exploits.combat.check-times", 2));
		nodes.add(new Node("CombatExploitRadius", SettingsType.GENERAL,
				"quests.exploits.combat.check-radius", 20));
		nodes.add(new Node("BlockTrackingRemoveDelay", SettingsType.GENERAL,
				"quests.exploits.blocks.tracking-remove-delay", 6000));
		return nodes;
	}

	@Override
	public Collection<String> getNodesForCopy() {
		return nodesForCopy;
	}

	@Override
	public boolean isEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isQuester);
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (!npc.isType("quester"))
			npc.registerType("quester");
		Quester quester = npc.getType("quester");
		quester.load(profiles, npc.getUID());
		saveState(npc);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isType("quester"));
			Quester quester = npc.getType("quester");
			quester.save(profiles, npc.getUID());
		}
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isQuester, value);
	}

	public static final QuesterProperties INSTANCE = new QuesterProperties();

	private static final String isQuester = ".quester.toggle";

	private static final List<String> nodesForCopy = Lists.newArrayList(
			"quester.toggle", "quester.quests");
}