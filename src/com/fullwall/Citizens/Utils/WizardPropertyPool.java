package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardPropertyPool {
	public static final PropertyHandler wizards = new PropertyHandler(
			"plugins/Citizens/Wizards/Citizens.wizards");
	public static final PropertyHandler locations = new PropertyHandler(
			"plugins/Citizens/Wizards/Citizens.locations");

	public static void saveAll() {
		wizards.save();
		locations.save();
	}

	public static void saveState(HumanNPC npc) {
		if (isWizard(npc.getUID())) {
			saveWizard(npc.getUID(), npc.isWizard());
			saveLocations(npc.getUID(), npc.getWizard().getLocations());
		}
	}

	public static void removeFromFiles(int UID) {
		wizards.removeKey(UID);
		locations.removeKey(UID);
	}

	public static void saveWizard(int UID, boolean state) {
		wizards.setBoolean(UID, state);
	}

	public static boolean isWizard(int UID) {
		return wizards.keyExists(UID);
	}

	public static void removeWizard(int UID) {
		wizards.removeKey(UID);
	}

	public static void saveLocations(int UID, String locationString) {
		locations.setString(UID, locationString.replace(")(", "):("));
	}

	public static String getLocations(int UID) {
		return locations.getString(UID).replace(")(", "):(");
	}

	public static boolean getWizardState(int UID) {
		return wizards.getBoolean(UID);
	}
}