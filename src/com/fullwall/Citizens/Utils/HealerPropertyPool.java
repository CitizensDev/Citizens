package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerPropertyPool {
	public static final PropertyHandler healers = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.healers");
	public static final PropertyHandler strength = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.strength");
	public static final PropertyHandler levels = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.levels");

	public static void saveAll() {
		healers.save();
		strength.save();
		levels.save();
	}

	public static void saveState(HumanNPC npc) {
		if (isHealer(npc.getUID())) {
			saveHealer(npc.getUID(), npc.isHealer());
			saveStrength(npc.getUID(), getStrength(npc.getUID()));
			saveLevel(npc.getUID(), getLevel(npc.getUID()));
		}
	}

	public static void removeFromFiles(int UID) {
		healers.removeKey(UID);
		strength.removeKey(UID);
		levels.removeKey(UID);
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

	public static void saveLevel(int UID, int level) {
		levels.setInt(UID, level);
	}

	public static int getLevel(int UID) {
		return levels.getInt(UID);
	}

	public static boolean getHealerState(int UID) {
		return healers.getBoolean(UID);
	}

	public static int getMaxStrength(int UID) {
		return getLevel(UID) * 10;
	}
}