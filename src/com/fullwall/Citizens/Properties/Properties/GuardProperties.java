package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardProperties extends Saveable {
	private final PropertyHandler guards = new PropertyHandler(
			"plugins/Citizens/Guards/Citizens.guards");
	private final PropertyHandler guardTypes = new PropertyHandler(
			"plugins/Citizens/Guards/Citizens.guardtypes");

	public String getGuardType(int UID) {
		return guardTypes.getString(UID);
	}

	public void saveGuardType(int UID, String guardType) {
		guardTypes.setString(UID, guardType);
	}

	@Override
	public void saveFiles() {
		guards.save();
		guardTypes.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isGuard());
			saveGuardType(npc.getUID(), npc.getGuard().getGuardType());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setGuard(getEnabled(npc));
		npc.getGuard().setGuardType(getGuardType(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		guards.removeKey(npc.getUID());
		guardTypes.removeKey(npc.getUID());
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
	}
}