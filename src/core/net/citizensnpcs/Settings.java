package net.citizensnpcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.properties.DataSource;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.Setting;
import net.citizensnpcs.utils.Messaging;

public class Settings {
	private static List<Setting> nodes = new ArrayList<Setting>();
	private static Map<String, Setting> loadedNodes = new HashMap<String, Setting>();

	public enum SettingsType {
		/**
		 * citizens.yml
		 */
		GENERAL,
		/**
		 * mobs.yml
		 */
		MOB;
	}

	public static String getPath(String key) {
		return loadedNodes.get(key).getPath();
	}

	public static boolean getBoolean(String name) {
		try {
			return (Boolean) loadedNodes.get(name).getValue();
		} catch (NullPointerException e) {
			Messaging.log("Report this error ASAP.");
			e.printStackTrace();
			return false;
		}
	}

	public static int getInt(String name) {
		try {
			return (Integer) loadedNodes.get(name).getValue();
		} catch (NullPointerException e) {
			Messaging.log("Report this error ASAP.");
			e.printStackTrace();
			return 0;
		}
	}

	public static String getString(String name) {
		try {
			return (String) loadedNodes.get(name).getValue();
		} catch (NullPointerException e) {
			Messaging.log("Report this error ASAP.");
			e.printStackTrace();
			return "";
		}
	}

	public static double getDouble(String name) {
		try {
			Object value = loadedNodes.get(name).getValue();
			if (value instanceof Float) {
				return (Float) value;
			} else if (value instanceof Double) {
				return (Double) value;
			} else {
				return (Integer) value;
			}
		} catch (NullPointerException e) {
			Messaging.log("Report this error ASAP.");
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Sets up miscellaneous variables, mostly reading from property files.
	 */
	public static void setupVariables() {
		PropertyManager.registerProperties();
		DataSource local = null;
		// Only load settings for loaded NPC types
		for (String t : Citizens.loadedTypes) {
			nodes.add(new Setting("", SettingsType.GENERAL, "economy.prices."
					+ t + ".creation", 100));
			List<Setting> settings = NPCTypeManager.getType(t).getSettings();
			if (settings == null) {
				continue;
			}
			nodes.addAll(settings);
		}
		for (Setting setting : nodes) {
			local = setting.getFile();
			DataKey key = local.getKey("");
			if (!key.keyExists(setting.getPath())) {
				Messaging.log("Writing default setting " + setting.getPath()
						+ ".");
				key.setRaw(setting.getPath(), setting.getValue());
			} else {
				setting.set(key.getRaw(setting.getPath()));
			}
			loadedNodes.put(setting.getName(), setting);
			local.save();
		}
	}

	static {
		// citizens.yml
		nodes.add(new Setting("MinArrowRange", SettingsType.GENERAL,
				"range.arrow.min", 3));
		nodes.add(new Setting("MaxArrowRange", SettingsType.GENERAL,
				"range.arrow.max", 13));
		nodes.add(new Setting("MaxStationaryTicks", SettingsType.GENERAL,
				"ticks.pathing.max-stationary", -1));
		nodes.add(new Setting("MaxPathingTicks", SettingsType.GENERAL,
				"ticks.pathing.max-pathing", -1));
		nodes.add(new Setting("SavingDelay", SettingsType.GENERAL,
				"ticks.saving.delay", 72000));
		nodes.add(new Setting("RightClickPause", SettingsType.GENERAL,
				"ticks.waypoints.right-click-pause", 70));
		nodes.add(new Setting("SavingDelay", SettingsType.GENERAL,
				"ticks.saving.delay", 72000));
		nodes.add(new Setting("NPCRange", SettingsType.GENERAL, "range.look", 5));
		nodes.add(new Setting("ChatFormat", SettingsType.GENERAL,
				"general.chat.format", "[%name%]: "));
		nodes.add(new Setting(
				"DefaultText",
				SettingsType.GENERAL,
				"general.chat.default-text",
				"Hello.;How are you today?;Having a nice day?;Good weather today.;Stop hitting me!;I'm bored.;"));
		nodes.add(new Setting("NPCColor", SettingsType.GENERAL,
				"general.colors.npc-colour", "f"));
		nodes.add(new Setting("TalkItems", SettingsType.GENERAL,
				"items.talk-items", "340,"));
		nodes.add(new Setting("SelectItems", SettingsType.GENERAL,
				"items.select-items", "*"));
		nodes.add(new Setting("SelectionMessage", SettingsType.GENERAL,
				"general.chat.selection-message",
				"<g>You selected <y><npc><g> (ID <y><npcid><g>)."));
		nodes.add(new Setting("CreationMessage", SettingsType.GENERAL,
				"general.chat.creation-message",
				"<g>The NPC <y><npc><g> was born!"));
		nodes.add(new Setting("SpaceChar", SettingsType.GENERAL,
				"general.chat.space-char", "/"));
		nodes.add(new Setting("DefaultLookAt", SettingsType.GENERAL,
				"general.defaults.enable-following", true));
		nodes.add(new Setting("SaveOften", SettingsType.GENERAL,
				"ticks.saving.save-often", true));
		nodes.add(new Setting("UseNPCColors", SettingsType.GENERAL,
				"general.colors.use-npc-colours", true));
		nodes.add(new Setting("UseSaveTask", SettingsType.GENERAL,
				"ticks.saving.use-task", true));
		nodes.add(new Setting("QuickSelect", SettingsType.GENERAL,
				"general.quick-select", false));
		nodes.add(new Setting("DebugMode", SettingsType.GENERAL,
				"debug.debug-mode", false));
		nodes.add(new Setting("NotifyUpdates", SettingsType.GENERAL,
				"general.notify-updates", true));
		nodes.add(new Setting("DefaultTalkClose", SettingsType.GENERAL,
				"general.defaults.talk-when-close", false));
		nodes.add(new Setting("DenyBlockedPVPTargets", SettingsType.GENERAL,
				"general.pvp.deny.attack-disabled-pvp-players", true));
		nodes.add(new Setting("PathfindingRange", SettingsType.GENERAL,
				"range.pathfinding", 16F));
		nodes.add(new Setting("UseEconomy", SettingsType.GENERAL,
				"economy.use-economy", true));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.basic.creation", 100));
		nodes.add(new Setting("ErrorReporting", SettingsType.GENERAL,
				"debug.enable-error-reporting", true));
		nodes.add(new Setting("ErrorReportingIdent", SettingsType.GENERAL,
				"debug.error-reporting-ident", "Anonymous"));
		// mobs.yml
		nodes.add(new Setting("CreatureNPCSpawnDelay", SettingsType.MOB,
				"general.spawn.delay", 200));
		nodes.add(new Setting("EvilTameItem", SettingsType.MOB,
				"evil.items.tame-item", 354));
		nodes.add(new Setting("EvilTameChance", SettingsType.MOB,
				"evil.misc.tame-chance", 5));
		nodes.add(new Setting("MaxEvils", SettingsType.MOB, "evil.spawn.max", 2));
		nodes.add(new Setting("EvilSpawnChance", SettingsType.MOB,
				"evil.spawn.chance", 100));
		nodes.add(new Setting("EvilNames", SettingsType.MOB, "evil.misc.names",
				"Evil_aPunch,Evil_fullwall,Evil_Notch,Herobrine,"));
		nodes.add(new Setting("EvilHealth", SettingsType.MOB,
				"evil.misc.health", 20));
		nodes.add(new Setting("EvilDrops", SettingsType.MOB,
				"evil.items.drops", "260,357,2256,"));
		nodes.add(new Setting("SpawnEvils", SettingsType.MOB,
				"evil.spawn.spawn", false));
		nodes.add(new Setting("EvilFailedTameMessages", SettingsType.MOB,
				"evil.misc.failed-tame-messages",
				"Ha! You can't tame me!;Nice try, <name>!;Muahahaha, I am evil!;"));
	}
}