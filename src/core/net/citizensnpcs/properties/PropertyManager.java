package net.citizensnpcs.properties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.properties.properties.BasicProperties;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;

public class PropertyManager {
	private static final Map<String, Properties> properties = new HashMap<String, Properties>();
	protected static final CachedYAMLHandler profiles = new CachedYAMLHandler(
			"plugins/Citizens/npc-profiles.yml");

	public static void registerProperties() {
		add("basic", new BasicProperties());
		UtilityProperties.load();
	}

	public static void add(String type, Properties saveable) {
		properties.put(type, saveable);
	}

	public static CachedYAMLHandler getNPCProfiles() {
		return profiles;
	}

	public static boolean npcHasType(HumanNPC npc, String type) {
		return profiles.keyExists(npc.getUID() + "." + type);
	}

	protected static boolean exists(HumanNPC npc) {
		return profiles.keyExists("" + npc.getUID());
	}

	public static BasicProperties getBasic() {
		return (BasicProperties) get("basic");
	}

	public static void load(HumanNPC npc) {
		for (Properties saveable : properties.values()) {
			if (!exists(npc) || !saveable.isEnabled(npc))
				continue;
			saveable.loadState(npc);
		}
	}

	public static void load(String type, HumanNPC npc) {
		if (!exists(npc) || !get(type).isEnabled(npc))
			return;
		get(type).loadState(npc);
	}

	public static void save(HumanNPC npc) {
		for (Properties saveable : properties.values()) {
			if (!saveable.isEnabled(npc))
				continue;
			saveable.saveState(npc);
		}
	}

	public static void save(String type, HumanNPC npc) {
		if (!exists(npc) || !get(type).isEnabled(npc))
			return;
		get(type).saveState(npc);
	}

	public static void remove(HumanNPC npc) {
		profiles.removeKey(npc.getUID());
	}

	public static Properties get(String string) {
		return properties.get(string);
	}

	public static void copyNPCs(int UID, int newUID) {
		for (Properties saveable : properties.values()) {
			Collection<String> copyNodes = saveable.getNodesForCopy();
			if (copyNodes == null)
				continue;
			for (String node : copyNodes) {
				if (node.startsWith("."))
					node = node.replaceFirst("\\.", "");
				recurseCopy("." + node, UID, newUID);
			}
		}
	}

	private static void recurseCopy(String root, int UID, int newUID) {
		if (!profiles.keyExists(UID + root))
			return;
		if (!profiles.getString(UID + root).isEmpty()) {
			profiles.setString(newUID + root, profiles.getString(UID + root));
		}
		for (Object deeper : profiles.getKeys(UID + root)) {
			recurseCopy(root + "." + deeper, UID, newUID);
		}
	}

	private static void saveAllNPCs() {
		for (HumanNPC npc : NPCManager.getList().values()) {
			save(npc);
		}
	}

	public static void saveState() {
		saveAllNPCs();
		profiles.save();
	}

	public static void loadAll() {
		profiles.load();
		for (HumanNPC npc : NPCManager.getList().values()) {
			load(npc);
		}
	}
}