package com.fullwall.Citizens.Properties.Properties;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.Enums.GuardType;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardProperties extends Saveable {
	private final PropertyHandler guards = new PropertyHandler(
			"plugins/Citizens/Guards/guards.citizens");
	private final PropertyHandler guardTypes = new PropertyHandler(
			"plugins/Citizens/Guards/guardtypes.citizens");
	private final PropertyHandler bouncerMobBlacklist = new PropertyHandler(
			"plugins/Citizens/Guards/Bouncers/mobblacklist.citizens");
	private final PropertyHandler bouncerWhitelist = new PropertyHandler(
			"plugins/Citizens/Guards/Bouncers/whitelist.citizens");
	private final PropertyHandler radius = new PropertyHandler(
			"plugins/Citizens/Guards/Bouncers/radius.citizens");
	private final PropertyHandler bodyguardMobBlacklist = new PropertyHandler(
			"plugins/Citizens/Guards/Bodyguards/mobblacklist.citizens");
	private final PropertyHandler bodyguardWhitelist = new PropertyHandler(
			"plugins/Citizens/Guards/Bodyguards/whitelist.citizens");
	private final PropertyHandler aggressive = new PropertyHandler(
			"plugins/Citizens/Guards/Bodyguards/aggressive.citizens");

	private double getProtectionRadius(int UID) {
		return radius.getDouble(UID, Constants.defaultBouncerProtectionRadius);
	}

	private void saveAggressive(int UID, boolean aggro) {
		aggressive.setBoolean(UID, aggro);
	}

	private boolean getAggressive(int UID) {
		return aggressive.getBoolean(UID);
	}

	private void saveProtectionRadius(int UID, double rad) {
		radius.setDouble(UID, rad);
	}

	private GuardType getGuardType(int UID) {
		return GuardType.parse(guardTypes.getString(UID));
	}

	private void saveGuardType(int UID, GuardType guardType) {
		guardTypes.setString(UID, guardType.toString().toLowerCase());
	}

	private List<String> getBodyguardMobBlacklist(int UID) {
		String save = bodyguardMobBlacklist.getString(UID);
		List<String> mobs = new ArrayList<String>();
		for (String s : save.split(",")) {
			mobs.add(s);
		}
		return mobs;
	}

	private void saveBodyguardMobBlacklist(int UID, List<String> mobs) {
		String save = "";
		for (int x = 0; x < mobs.size(); x++) {
			save += mobs.get(x) + ",";
		}
		bodyguardMobBlacklist.setString(UID, save);
	}

	private List<String> getBodyguardWhitelist(int UID) {
		String save = bodyguardWhitelist.getString(UID);
		List<String> players = new ArrayList<String>();
		for (String s : save.split(",")) {
			players.add(s);
		}
		return players;
	}

	private void saveBodyguardWhitelist(int UID, List<String> players) {
		String save = "";
		for (int x = 0; x < players.size(); x++) {
			save += players.get(x) + ",";
		}
		bodyguardWhitelist.setString(UID, save);
	}

	private List<String> getBouncerMobBlacklist(int UID) {
		String save = bouncerMobBlacklist.getString(UID);
		List<String> mobs = new ArrayList<String>();
		for (String s : save.split(",")) {
			mobs.add(s);
		}
		return mobs;
	}

	private void saveBouncerMobBlacklist(int UID, List<String> mobs) {
		String save = "";
		for (int x = 0; x < mobs.size(); x++) {
			save += mobs.get(x) + ",";
		}
		bouncerMobBlacklist.setString(UID, save);
	}

	private List<String> getBouncerWhitelist(int UID) {
		String save = bouncerWhitelist.getString(UID);
		List<String> players = new ArrayList<String>();
		for (String s : save.split(",")) {
			players.add(s);
		}
		return players;
	}

	private void saveBouncerWhitelist(int UID, List<String> players) {
		String save = "";
		for (int x = 0; x < players.size(); x++) {
			save += players.get(x) + ",";
		}
		bouncerWhitelist.setString(UID, save);
	}

	@Override
	public void saveFiles() {
		guards.save();
		guardTypes.save();
		bouncerMobBlacklist.save();
		bouncerWhitelist.save();
		bodyguardMobBlacklist.save();
		bodyguardWhitelist.save();
		radius.save();
		aggressive.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isGuard());
			saveGuardType(npc.getUID(), npc.getGuard().getGuardType());
			saveBouncerMobBlacklist(npc.getUID(), npc.getGuard()
					.getBouncerMobBlacklist());
			saveBouncerWhitelist(npc.getUID(), npc.getGuard()
					.getBouncerWhitelist());
			saveBodyguardMobBlacklist(npc.getUID(), npc.getGuard()
					.getBodyguardMobBlacklist());
			saveBodyguardWhitelist(npc.getUID(), npc.getGuard()
					.getBodyguardWhitelist());
			saveProtectionRadius(npc.getUID(), npc.getGuard()
					.getProtectionRadius());
			saveAggressive(npc.getUID(), npc.getGuard().isAggressive());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setGuard(getEnabled(npc));
		npc.getGuard().setGuardType(getGuardType(npc.getUID()));
		npc.getGuard().setBouncerMobBlacklist(
				getBouncerMobBlacklist(npc.getUID()));
		npc.getGuard().setBouncerWhitelist(getBouncerWhitelist(npc.getUID()));
		npc.getGuard().setBodyguardMobBlacklist(
				getBodyguardMobBlacklist(npc.getUID()));
		npc.getGuard().setBodyguardWhitelist(
				getBodyguardWhitelist(npc.getUID()));
		npc.getGuard().setProtectionRadius(getProtectionRadius(npc.getUID()));
		npc.getGuard().setAggressive(getAggressive(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		guards.removeKey(npc.getUID());
		guardTypes.removeKey(npc.getUID());
		bouncerMobBlacklist.removeKey(npc.getUID());
		bouncerWhitelist.removeKey(npc.getUID());
		bodyguardMobBlacklist.removeKey(npc.getUID());
		bodyguardWhitelist.removeKey(npc.getUID());
		radius.removeKey(npc.getUID());
		aggressive.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		guards.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return guards.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return guards.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.GUARD;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (guards.keyExists(UID)) {
			guards.setString(nextUID, guards.getString(UID));
		}
		if (guardTypes.keyExists(UID)) {
			guards.setString(nextUID, guards.getString(UID));
		}
		if (bouncerMobBlacklist.keyExists(UID)) {
			bouncerMobBlacklist.setString(nextUID,
					bouncerMobBlacklist.getString(UID));
		}
		if (bouncerWhitelist.keyExists(UID)) {
			bouncerWhitelist
					.setString(nextUID, bouncerWhitelist.getString(UID));
		}
		if (bodyguardMobBlacklist.keyExists(UID)) {
			bodyguardMobBlacklist.setString(nextUID,
					bodyguardMobBlacklist.getString(UID));
		}
		if (bodyguardWhitelist.keyExists(UID)) {
			bodyguardWhitelist.setString(nextUID,
					bodyguardWhitelist.getString(UID));
		}
		if (radius.keyExists(UID)) {
			radius.setString(nextUID, radius.getString(UID));
		}
		if (aggressive.keyExists(UID)) {
			aggressive.setString(nextUID, aggressive.getString(UID));
		}
	}
}