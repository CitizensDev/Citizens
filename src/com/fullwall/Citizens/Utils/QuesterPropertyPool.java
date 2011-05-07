package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterPropertyPool {
	public static final PropertyHandler questers = new PropertyHandler(
			"plugins/Citizens/Questers/Citizens.questers");

	public static void saveAll() {
		questers.save();
	}

	public static void saveState(HumanNPC npc) {
		if (isQuester(npc.getUID())) {
			saveQuester(npc.getUID(), npc.isHealer());
		}
	}

	public static void removeFromFiles(int UID) {
		questers.removeKey(UID);
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

	/**
	 * Copies all data from one ID to another.
	 * 
	 * @param UID
	 * @param nextUID
	 */
	public static void copyProperties(int UID, int nextUID) {
		if (questers.keyExists(UID))
			questers.setString(nextUID, questers.getString(UID));
		saveAll();
	}
}