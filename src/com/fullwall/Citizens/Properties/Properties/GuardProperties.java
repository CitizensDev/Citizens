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
	private final PropertyHandler radius = new PropertyHandler(
			"plugins/Citizens/Guards/Bouncers/radius.citizens");
	private final PropertyHandler blacklist = new PropertyHandler(
			"plugins/Citizens/Guards/blacklist.citizens");
	private final PropertyHandler whitelist = new PropertyHandler(
			"plugins/Citizens/Guards/whitelist.citizens");
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

	private List<String> getBlacklist(int UID) {
		String save = blacklist.getString(UID);
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
		blacklist.setString(UID, save);
	}

	private List<String> getWhitelist(int UID) {
		String save = whitelist.getString(UID);
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
		whitelist.setString(UID, save);
	}

	@Override
	public void saveFiles() {
		guards.save();
		guardTypes.save();
		blacklist.save();
		whitelist.save();
		radius.save();
		aggressive.save();
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
	public void removeFromFiles(HumanNPC npc) {
		guards.removeKey(npc.getUID());
		guardTypes.removeKey(npc.getUID());
		blacklist.removeKey(npc.getUID());
		whitelist.removeKey(npc.getUID());
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
		if (blacklist.keyExists(UID)) {
			blacklist.setString(nextUID, blacklist.getString(UID));
		}
		if (whitelist.keyExists(UID)) {
			whitelist.setString(nextUID, whitelist.getString(UID));
		}
		if (radius.keyExists(UID)) {
			radius.setString(nextUID, radius.getString(UID));
		}
		if (aggressive.keyExists(UID)) {
			aggressive.setString(nextUID, aggressive.getString(UID));
		}
	}
}