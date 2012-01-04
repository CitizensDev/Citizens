package net.citizensnpcs.properties;

import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.properties.properties.UtilityProperties;

public class PropertyManager {
	protected static final DataSource profiles = new CachedYAMLHandler(
			"plugins/Citizens/npc-profiles.yml");

	public static void copyNPCs(int UID, int newUID) {
		profiles.getKey(Integer.toString(UID)).copy(Integer.toString(newUID));
	}

	protected static boolean exists(HumanNPC npc) {
		return profiles.getKey("").keyExists(Integer.toString(npc.getUID()));
	}

	public static int getNewNpcID() {
		DataKey root = profiles.getKey("");
		int i = 0;
		for (; root.keyExists(Integer.toString(i)); ++i)
			;
		return i;
	}

	public static DataSource getNPCProfiles() {
		return profiles;
	}

	public static void loadAll() {
		profiles.load();
		for (HumanNPC npc : NPCManager.getNPCs()) {
			npc.load();
		}
	}

	public static boolean npcHasType(HumanNPC npc, String type) {
		return profiles.getKey(Integer.toString(npc.getUID())).keyExists(type);
	}

	public static void registerProperties() {
		UtilityProperties.load();
	}

	public static void remove(HumanNPC npc) {
		profiles.getKey("").removeKey(Integer.toString(npc.getUID()));
	}

	public static void saveState() {
		for (HumanNPC npc : NPCManager.getNPCs()) {
			npc.save();
		}
		profiles.save();
	}
}