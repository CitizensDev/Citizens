package net.citizensnpcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.utils.Messaging;

public class SettingsManager {
	private static List<Node> nodes = new ArrayList<Node>();
	private static Map<String, Node> loadedNodes = new HashMap<String, Node>();

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
		Storage local = null;
		// Only load settings for loaded NPC types
		for (String t : Citizens.loadedTypes) {
			nodes.add(new Node("", SettingsType.GENERAL, "economy.prices." + t
					+ ".creation", 100));
			if (NPCTypeManager.getType(t).getProperties().getNodes() == null) {
				continue;
			}
			for (Node node : NPCTypeManager.getType(t).getProperties()
					.getNodes()) {
				nodes.add(node);
			}
		}
		for (Node node : nodes) {
			local = node.getFile();
			if (!local.keyExists(node.getPath())) {
				Messaging
						.log("Writing default setting " + node.getPath() + ".");
				local.setRaw(node.getPath(), node.getValue());
			} else {
				node.set(local.getRaw(node.getPath()));
			}
			loadedNodes.put(node.getName(), node);
			local.save();
		}
	}

	static {
		// citizens.yml
		nodes.add(new Node("MaxStationaryTicks", SettingsType.GENERAL,
				"ticks.pathing.max-stationary", -1));
		nodes.add(new Node("MaxPathingTicks", SettingsType.GENERAL,
				"ticks.pathing.max-pathing", -1));
		nodes.add(new Node("SavingDelay", SettingsType.GENERAL,
				"ticks.saving.delay", 72000));
		nodes.add(new Node("RightClickPause", SettingsType.GENERAL,
				"ticks.waypoints.right-click-pause", 70));
		nodes.add(new Node("SavingDelay", SettingsType.GENERAL,
				"ticks.saving.delay", 72000));
		nodes.add(new Node("NPCRange", SettingsType.GENERAL, "range.look", 5));
		nodes.add(new Node("ChatFormat", SettingsType.GENERAL,
				"general.chat.format", "[%name%]: "));
		nodes.add(new Node(
				"DefaultText",
				SettingsType.GENERAL,
				"general.chat.default-text",
				"Hello.;How are you today?;Having a nice day?;Good weather today.;Stop hitting me!;I'm bored.;"));
		nodes.add(new Node("NPCColor", SettingsType.GENERAL,
				"general.colors.npc-colour", "f"));
		nodes.add(new Node("TalkItems", SettingsType.GENERAL,
				"items.talk-items", "340,"));
		nodes.add(new Node("SelectItems", SettingsType.GENERAL,
				"items.select-items", "*"));
		nodes.add(new Node("SelectionMessage", SettingsType.GENERAL,
				"general.chat.selection-message",
				"<g>You selected <y><npc><g> (ID <y><npcid><g>)."));
		nodes.add(new Node("CreationMessage", SettingsType.GENERAL,
				"general.chat.creation-message",
				"<g>The NPC <y><npc><g> was born!"));
		nodes.add(new Node("SpaceChar", SettingsType.GENERAL,
				"general.chat.space-char", "/"));
		nodes.add(new Node("DefaultLookAt", SettingsType.GENERAL,
				"general.defaults.enable-following", true));
		nodes.add(new Node("SaveOften", SettingsType.GENERAL,
				"ticks.saving.save-often", true));
		nodes.add(new Node("UseNPCColors", SettingsType.GENERAL,
				"general.colors.use-npc-colours", true));
		nodes.add(new Node("UseSaveTask", SettingsType.GENERAL,
				"ticks.saving.use-task", true));
		nodes.add(new Node("QuickSelect", SettingsType.GENERAL,
				"general.quick-select", false));
		nodes.add(new Node("DebugMode", SettingsType.GENERAL,
				"debug.debug-mode", false));
		nodes.add(new Node("NotifyUpdates", SettingsType.GENERAL,
				"general.notify-updates", true));
		nodes.add(new Node("DefaultTalkClose", SettingsType.GENERAL,
				"general.defaults.talk-when-close", false));
		nodes.add(new Node("DenyBlockedPVPTargets", SettingsType.GENERAL,
				"general.pvp.deny.attack-disabled-pvp-players", true));
		nodes.add(new Node("PathfindingRange", SettingsType.GENERAL,
				"range.pathfinding", 16F));
		nodes.add(new Node("UseEconomy", SettingsType.GENERAL,
				"economy.use-economy", true));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.basic.creation", 100));
		nodes.add(new Node("ErrorReporting", SettingsType.GENERAL,
				"debug.enable-error-reporting", true));
		nodes.add(new Node("ErrorReportingIdent", SettingsType.GENERAL,
				"debug.error-reporting-ident", "Anonymous"));
		// mobs.yml
		nodes.add(new Node("CreatureNPCSpawnDelay", SettingsType.MOB,
				"general.spawn.delay", 200));
		nodes.add(new Node("EvilTameItem", SettingsType.MOB,
				"evil.items.tame-item", 354));
		nodes.add(new Node("EvilTameChance", SettingsType.MOB,
				"evil.misc.tame-chance", 5));
		nodes.add(new Node("MaxEvils", SettingsType.MOB, "evil.spawn.max", 2));
		nodes.add(new Node("EvilSpawnChance", SettingsType.MOB,
				"evil.spawn.chance", 100));
		nodes.add(new Node("EvilNames", SettingsType.MOB, "evil.misc.names",
				"Evil_aPunch,Evil_fullwall,Evil_Notch,Herobrine,"));
		nodes.add(new Node("EvilHealth", SettingsType.MOB, "evil.misc.health",
				20));
		nodes.add(new Node("EvilDrops", SettingsType.MOB, "evil.items.drops",
				"260,357,2256,"));
		nodes.add(new Node("SpawnEvils", SettingsType.MOB, "evil.spawn.spawn",
				false));
		nodes.add(new Node("EvilFailedTameMessages", SettingsType.MOB,
				"evil.misc.failed-tame-messages",
				"Ha! You can't tame me!;Nice try, <name>!;Muahahaha, I am evil!;"));
	}
}