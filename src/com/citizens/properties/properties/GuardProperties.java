package com.citizens.properties.properties;

import com.citizens.interfaces.Saveable;
import com.citizens.npctypes.guards.FlagInfo;
import com.citizens.npctypes.guards.FlagList;
import com.citizens.npctypes.guards.FlagList.FlagType;
import com.citizens.npctypes.guards.Guard;
import com.citizens.npctypes.guards.GuardManager.GuardType;
import com.citizens.properties.PropertyManager;
import com.citizens.properties.SettingsManager;
import com.citizens.resources.npclib.HumanNPC;

public class GuardProperties extends PropertyManager implements Saveable {
	private static final String isGuard = ".guard.toggle";
	private static final String type = ".guard.type";
	private static final String radius = ".guard.radius";
	private static final String blacklist = ".guard.blacklist";
	private static final String aggressive = ".guard.aggressive";

	private void saveProtectionRadius(int UID, double rad) {
		profiles.setDouble(UID + radius, rad);
	}

	private double getProtectionRadius(int UID) {
		return profiles.getDouble(UID + radius, SettingsManager
				.getDouble("range.guards.default-bouncer-protection-radius"));
	}

	private void saveAggressive(int UID, boolean aggro) {
		profiles.setBoolean(UID + aggressive, aggro);
	}

	private boolean isAggressive(int UID) {
		return profiles.getBoolean(UID + aggressive);
	}

	private GuardType getGuardType(int UID) {
		return GuardType.parse(profiles.getString(UID + type));
	}

	private void saveGuardType(int UID, GuardType guardType) {
		profiles.setString(UID + type, guardType.name());
	}

	private void loadFlags(Guard guard, int UID) {
		String root = UID + blacklist, path = root;
		if (!profiles.pathExists(root))
			return;
		FlagList flags = guard.getFlags();
		boolean isSafe;
		int priority;
		for (String key : profiles.getKeys(root)) {
			path = root + "." + key;
			isSafe = profiles.getBoolean(path + ".safe");
			priority = profiles.getInt(path + ".priority");
			flags.addFlag(FlagType.valueOf(profiles.getString(path + ".type")),
					FlagInfo.newInstance(key, priority, isSafe));
		}
	}

	private void saveFlags(int UID, FlagList flags) {
		String root = UID + blacklist, path = root;
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
				saveGuardType(npc.getUID(), guard.getGuardType());
				saveFlags(npc.getUID(), guard.getFlags());
				saveProtectionRadius(npc.getUID(), guard.getProtectionRadius());
				saveAggressive(npc.getUID(), guard.isAggressive());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("guard");
			Guard guard = npc.getType("guard");
			loadFlags(guard, npc.getUID());
			guard.setGuardType(getGuardType(npc.getUID()));
			guard.setProtectionRadius(getProtectionRadius(npc.getUID()));
			guard.setAggressive(isAggressive(npc.getUID()));
		}
		saveState(npc);
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
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
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isGuard)) {
			profiles.setString(nextUID + isGuard,
					profiles.getString(UID + isGuard));
		}
		if (profiles.pathExists(UID + type)) {
			profiles.setString(nextUID + type, profiles.getString(UID + type));
		}
		if (profiles.pathExists(UID + blacklist)) {
			profiles.setString(nextUID + blacklist,
					profiles.getString(UID + blacklist));
		}
		if (profiles.pathExists(UID + radius)) {
			profiles.setString(nextUID + radius,
					profiles.getString(UID + radius));
		}
		if (profiles.pathExists(UID + aggressive)) {
			profiles.setString(nextUID + aggressive,
					profiles.getString(UID + aggressive));
		}
	}
}