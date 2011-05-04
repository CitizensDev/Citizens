package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerPropertyPool {
	public static final PropertyHandler healers = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.healers");
	public static final PropertyHandler strength = new PropertyHandler(
			"plugin/Citizens/Healers/Citizens.strength");

	public static void saveAll() {
		healers.save();
		strength.save();
	}

	public static void saveHealer(int UID, boolean state) {
		healers.setBoolean(UID, state);
	}

	public static boolean isHealer(int UID) {
		return healers.keyExists(UID);
	}

	public static void removeHealer(int UID) {
		healers.removeKey(UID);
	}

	public static void saveStrength(int UID, int healPower) {
		strength.setInt(UID, healPower);
	}

	public static int getStrength(int UID) {
		return strength.getInt(UID);
	}

	public static void saveHealerState(HumanNPC npc) {
		if (isHealer(npc.getUID())) {
			saveHealer(npc.getUID(), npc.isTrader());
			saveStrength(npc.getUID(), getStrength(npc.getUID()));
		}
	}
	
	public static boolean getHealerState(int UID) {
		return healers.getBoolean(UID);
	}
}