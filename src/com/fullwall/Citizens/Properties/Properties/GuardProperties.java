package com.fullwall.Citizens.Properties.Properties;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.Enums.GuardType;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

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
				Constants.defaultBouncerProtectionRadius);
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
		profiles.setString(UID + type, guardType.toString().toLowerCase());
	}

	private List<String> getBlacklist(int UID) {
		String save = profiles.getString(UID + blacklist);
		List<String> mobs = new ArrayList<String>();
		for (String s : save.split(",")) {
			mobs.add(s);
		}
		return mobs;
	}

	private void saveBlacklist(int UID, List<String> mobs) {
		String save = "";
		for (int x = 0; x < mobs.size(); x++) {
			save += mobs.get(x) + ",";
		}
		profiles.setString(UID + blacklist, save);
	}

	private List<String> getWhitelist(int UID) {
		String save = profiles.getString(UID + whitelist);
		List<String> players = new ArrayList<String>();
		for (String s : save.split(",")) {
			players.add(s);
		}
		return players;
	}

	private void saveWhitelist(int UID, List<String> players) {
		String save = "";
		for (int x = 0; x < players.size(); x++) {
			save += players.get(x) + ",";
		}
		profiles.setString(UID + whitelist, save);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isGuard());
			saveGuardType(npc.getUID(), npc.getGuard().getGuardType());
			saveBlacklist(npc.getUID(), npc.getGuard().getBlacklist());
			saveWhitelist(npc.getUID(), npc.getGuard().getWhitelist());
			saveProtectionRadius(npc.getUID(), npc.getGuard()
					.getProtectionRadius());
			saveAggressive(npc.getUID(), npc.getGuard().isAggressive());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setGuard(getEnabled(npc));
		npc.getGuard().setGuardType(getGuardType(npc.getUID()));
		npc.getGuard().setBlacklist(getBlacklist(npc.getUID()));
		npc.getGuard().setWhitelist(getWhitelist(npc.getUID()));
		npc.getGuard().setProtectionRadius(getProtectionRadius(npc.getUID()));
		npc.getGuard().setAggressive(getAggressive(npc.getUID()));
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
	public PropertyType type() {
		return PropertyType.GUARD;
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