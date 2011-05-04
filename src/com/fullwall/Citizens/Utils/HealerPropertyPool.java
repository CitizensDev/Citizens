package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;

public class HealerPropertyPool {
	public static final PropertyHandler healers = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.healers");

	public static void saveAll() {
		healers.save();
	}

	public static void saveHealer(int UID, boolean state) {
		healers.setBoolean(UID, state);
	}

	public static boolean isHealer(int UID) {
		return healers.keyExists(UID);
	}
}
