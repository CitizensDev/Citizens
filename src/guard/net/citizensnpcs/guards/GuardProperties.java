package net.citizensnpcs.guards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.guards.flags.FlagInfo;
import net.citizensnpcs.guards.flags.FlagList;
import net.citizensnpcs.guards.flags.FlagList.FlagType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import com.google.common.collect.Lists;

public class GuardProperties extends PropertyManager implements Properties {

	public static final GuardProperties INSTANCE = new GuardProperties();

	private GuardProperties() {
	}

	private void loadFlags(Guard guard, int UID) {
		String root = UID + flag, path = root;
		if (!profiles.pathExists(root)) {
			return;
		}
		FlagList flags = guard.getFlags();
		for (String key : profiles.getKeys(root)) {
			path = root + "." + key;
			boolean isSafe = profiles.getBoolean(path + ".safe");
			int priority = profiles.getInt(path + ".priority");
			flags.addFlag(FlagType.parse(profiles.getString(path + ".type")),
					FlagInfo.newInstance(key, priority, isSafe));
		}
	}

	private void saveFlags(int UID, FlagList flags) {
		String root = UID + flag, path = root;
		for (FlagType type : FlagType.values()) {
			for (FlagInfo info : flags.getFlags(type).values()) {
				path = root + "." + info.getName();
				profiles.setString(path + ".type", type.name());
				profiles.setBoolean(path + ".safe", info.isSafe());
				profiles.setInt(path + ".priority", info.priority());
			}
		}
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("guard");
			setEnabled(npc, is);
			if (is) {
				Guard guard = npc.getType("guard");
				guard.save(profiles, npc.getUID());
				saveFlags(npc.getUID(), guard.getFlags());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("guard");
			Guard guard = npc.getType("guard");
			guard.load(profiles, npc.getUID());
			loadFlags(guard, npc.getUID());
		}
		saveState(npc);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isGuard, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isGuard);
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("MaxStationaryReturnTicks", SettingsType.GENERAL,
				"guards.max.stationary-return-ticks", 25));
		nodes.add(new Node("GuardRespawnDelay", SettingsType.GENERAL,
				"guards.respawn-delay", 100));
		nodes.add(new Node("DefaultBouncerProtectionRadius",
				SettingsType.GENERAL,
				"guards.bouncers.default.protection-radius", 10));
		return nodes;
	}

	@Override
	public Collection<String> getNodesForCopy() {
		return nodesForCopy;
	}

	private static final List<String> nodesForCopy = Lists.newArrayList(
			"guard.toggle", "guard.type", "guard.radius", "guard.flags",
			"guard.aggressive");
	private static final String isGuard = ".guard.toggle";
	private static final String flag = ".guard.flags";
}