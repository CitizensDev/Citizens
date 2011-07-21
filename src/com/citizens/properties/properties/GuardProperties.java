package com.citizens.properties.properties;

import java.util.Set;

import com.citizens.SettingsManager.Constant;
import com.citizens.interfaces.Saveable;
import com.citizens.npcs.NPCTypeManager;
import com.citizens.npctypes.guards.GuardManager.GuardType;
import com.citizens.npctypes.guards.GuardNPC;
import com.citizens.properties.PropertyManager;
import com.citizens.resources.npclib.HumanNPC;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

public class GuardProperties extends PropertyManager implements Saveable {
	private static final String isGuard = ".guard.toggle";
	private static final String type = ".guard.type";
	private static final String radius = ".guard.radius";
	private static final String blacklist = ".guard.blacklist";
	private static final String whitelist = ".guard.whitelist";
	private static final String aggressive = ".guard.aggressive";

	private void saveProtectionRadius(int UID, double rad) {
		profiles.setDouble(UID + radius, rad);
	}

	private double getProtectionRadius(int UID) {
		return profiles.getDouble(UID + radius,
				Constant.DefaultBouncerProtectionRadius.toDouble());
	}

	private void saveAggressive(int UID, boolean aggro) {
		profiles.setBoolean(UID + aggressive, aggro);
	}

	private boolean getAggressive(int UID) {
		return profiles.getBoolean(UID + aggressive);
	}

	private GuardType getGuardType(int UID) {
		return GuardType.parse(profiles.getString(UID + type));
	}

	private void saveGuardType(int UID, GuardType guardType) {
		profiles.setString(UID + type, guardType.name());
	}

	private Set<String> getBlacklist(int UID) {
		return Sets.newHashSet(profiles.getString(UID + blacklist).split(","));
	}

	private void saveBlacklist(int UID, Set<String> mobs) {
		profiles.setString(UID + blacklist, Joiner.on(",").join(mobs.toArray()));
	}

	private Set<String> getWhitelist(int UID) {
		return Sets.newHashSet(profiles.getString(UID + whitelist).split(","));
	}

	private void saveWhitelist(int UID, Set<String> players) {
		profiles.setString(UID + whitelist,
				Joiner.on(",").join(players.toArray()));
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("guard");
			setEnabled(npc, is);
			if (is) {
				GuardNPC guard = npc.getToggleable("guard");
				saveGuardType(npc.getUID(), guard.getGuardType());
				saveBlacklist(npc.getUID(), guard.getBlacklist());
				saveWhitelist(npc.getUID(), guard.getWhitelist());
				saveProtectionRadius(npc.getUID(), guard.getProtectionRadius());
				saveAggressive(npc.getUID(), guard.isAggressive());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("guard", NPCTypeManager.getFactory("guard"));
			GuardNPC guard = npc.getToggleable("guard");
			guard.setGuardType(getGuardType(npc.getUID()));
			guard.setBlacklist(getBlacklist(npc.getUID()));
			guard.setWhitelist(getWhitelist(npc.getUID()));
			guard.setProtectionRadius(getProtectionRadius(npc.getUID()));
			guard.setAggressive(getAggressive(npc.getUID()));
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
		if (profiles.pathExists(UID + whitelist)) {
			profiles.setString(nextUID + whitelist,
					profiles.getString(UID + whitelist));
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