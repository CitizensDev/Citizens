package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithPropertyPool {
	public static final PropertyHandler blacksmiths = new PropertyHandler(
			"plugins/Citizens/Blacksmiths/Citizens.blacksmiths");

	public static void saveAll() {
		blacksmiths.save();
	}

	public static void saveState(HumanNPC npc) {
		if (isBlacksmith(npc.getUID())) {
			saveBlacksmith(npc.getUID(), npc.isBlacksmith());
		}
	}

	public static void removeFromFiles(int UID) {
		blacksmiths.removeKey(UID);
	}

	public static void saveBlacksmith(int UID, boolean state) {
		blacksmiths.setBoolean(UID, state);
	}

	public static boolean isBlacksmith(int UID) {
		return blacksmiths.keyExists(UID);
	}

	public static boolean getBlacksmithState(int UID) {
		return blacksmiths.getBoolean(UID);
	}

	/**
	 * Copies all data from one ID to another.
	 * 
	 * @param UID
	 * @param nextUID
	 */
	public static void copyProperties(int UID, int nextUID) {
		if (blacksmiths.keyExists(UID))
			blacksmiths.setString(nextUID, blacksmiths.getString(UID));
		saveAll();
	}
}