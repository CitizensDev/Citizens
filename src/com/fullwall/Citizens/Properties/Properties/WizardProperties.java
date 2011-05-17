package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardProperties extends Saveable {
	private final PropertyHandler wizards = new PropertyHandler(
			"plugins/Citizens/Wizards/Citizens.wizards");
	private final PropertyHandler locations = new PropertyHandler(
			"plugins/Citizens/Wizards/Citizens.locations");

	private void saveLocations(int UID, String locationString) {
		locations.setString(UID, locationString.replace(")(", "):("));
	}

	private String getLocations(int UID) {
		return locations.getString(UID).replace(")(", "):(");
	}

	@Override
	public void saveFiles() {
		wizards.save();
		locations.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isWizard());
			saveLocations(npc.getUID(), npc.getWizard().getLocations());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setWizard(getEnabled(npc));
		npc.getWizard().setLocations(getLocations(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		wizards.removeKey(npc.getUID());
		locations.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		wizards.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return wizards.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return wizards.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.WIZARD;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (wizards.keyExists(UID))
			wizards.setString(nextUID, wizards.getString(UID));
		if (locations.keyExists(UID))
			locations.setString(nextUID, locations.getString(UID));
	}
}