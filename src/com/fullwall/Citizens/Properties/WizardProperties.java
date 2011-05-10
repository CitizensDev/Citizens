package com.fullwall.Citizens.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardProperties extends Saveable {
	public PropertyHandler wizards;
	public PropertyHandler locations;

	public WizardProperties() {
		wizards = new PropertyHandler(
				"plugins/Citizens/Wizards/Citizens.wizards");
		locations = new PropertyHandler(
				"plugins/Citizens/Wizards/Citizens.locations");
	}

	public void saveWizard(int UID, boolean state) {
		wizards.setBoolean(UID, state);
	}

	public boolean isWizard(int UID) {
		return wizards.keyExists(UID);
	}

	public void removeWizard(int UID) {
		wizards.removeKey(UID);
	}

	public void saveLocations(int UID, String locationString) {
		locations.setString(UID, locationString.replace(")(", "):("));
	}

	public String getLocations(int UID) {
		return locations.getString(UID).replace(")(", "):(");
	}

	public boolean getWizardState(int UID) {
		return wizards.getBoolean(UID);
	}

	@Override
	public void saveFiles() {
		wizards.save();
		locations.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			set(npc, npc.isWizard());
			saveLocations(npc.getUID(), npc.getWizard().getLocations());
		}
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		wizards.removeKey(npc.getUID());
		locations.removeKey(npc.getUID());
	}

	@Override
	public void set(HumanNPC npc) {
		set(npc, true);
	}

	@Override
	public void set(HumanNPC npc, boolean value) {
		saveWizard(npc.getUID(), value);
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return isWizard(npc.getUID());
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
		saveFiles();
	}
}