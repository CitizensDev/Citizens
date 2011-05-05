package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;

public class QuesterPropertyPool {
	public static final PropertyHandler questers = new PropertyHandler(
			"plugins/Citizens/Questers/Citizens.questers");
	public static final PropertyHandler questSettings = new PropertyHandler(
			"plugins/Citizens/Questers/Citizens.questSettings");

	public static void saveAll() {
		questers.save();
		questSettings.save();
	}

	public static void saveState() {

	}

	public static void saveQuester(int UID, boolean state) {
		questers.setBoolean(UID, state);
	}

	public static boolean isQuester(int UID) {
		return questers.keyExists(UID);
	}

	public static boolean getQuesterState(int UID) {
		return questers.getBoolean(UID);
	}

	public static void removeQuester(int UID) {
		questers.removeKey(UID);
	}

	public static void removeFromFiles(int UID) {
		questers.removeKey(UID);
		questSettings.removeKey(UID);
	}
}