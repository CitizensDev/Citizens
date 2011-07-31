package com.citizens.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.citizens.Citizens;
import com.citizens.interfaces.Storage;
import com.citizens.npctypes.CitizensNPCManager;
import com.citizens.utils.Messaging;

public class SettingsManager {
	private static List<Node> nodes = new ArrayList<Node>();
	private static Map<String, Object> loadedNodes = new HashMap<String, Object>();

	public enum SettingsType {
		/**
		 * citizens.yml
		 */
		GENERAL,
		/**
		 * economy.yml
		 */
		ECONOMY,
		/**
		 * mobs.yml
		 */
		MOB;
	}

	public static boolean getBoolean(String path) {
		return (Boolean) loadedNodes.get(path);
	}

	public static int getInt(String path) {
		return (Integer) loadedNodes.get(path);
	}

	public static String getString(String path) {
		return (String) loadedNodes.get(path);
	}

	public static double getDouble(String path) {
		if (loadedNodes.get(path) instanceof Float) {
			return (Float) loadedNodes.get(path);
		} else if (loadedNodes.get(path) instanceof Double) {
			return (Double) loadedNodes.get(path);
		} else {
			return (Integer) loadedNodes.get(path);
		}
	}

	/**
	 * Sets up miscellaneous variables, mostly reading from property files.
	 */
	public static void setupVariables() {
		Messaging.log("Loading settings...");
		PropertyManager.registerProperties();
		Storage local = null;
		// Load our non-type-specific settings
		loadSettings();
		// Only load settings for loaded NPC types
		for (String t : Citizens.loadedTypes) {
			nodes.add(new Node(SettingsType.ECONOMY, "prices." + t
					+ ".creation", 100));
			for (Node node : CitizensNPCManager.getType(t).getNodes()) {
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
				node.set(node.getFile().getRaw(node.getPath()));
			}
			loadedNodes.put(node.getPath(), node.getValue());
			local.save();
		}
		// local.save();
	}

	private static void loadSettings() {
		// citizens.yml
		nodes.add(new Node(SettingsType.GENERAL,
				"ticks.pathing.max-stationary", -1));
		nodes.add(new Node(SettingsType.GENERAL, "ticks.pathing.max-pathing",
				-1));
		nodes.add(new Node(SettingsType.GENERAL, "ticks.saving.delay", 72000));
		nodes.add(new Node(SettingsType.GENERAL,
				"ticks.waypoints.right-click-pause", 70));
		nodes.add(new Node(SettingsType.GENERAL, "ticks.general.delay", 1));
		nodes.add(new Node(SettingsType.GENERAL, "ticks.saving.delay", 72000));
		nodes.add(new Node(SettingsType.GENERAL, "range.basic.look", 5));
		nodes.add(new Node(SettingsType.GENERAL, "general.chat.format",
				"[%name%]: "));
		nodes.add(new Node(
				SettingsType.GENERAL,
				"general.chat.default-text",
				"Hello.;How are you today?;Having a nice day?;Good weather today.;Stop hitting me!;I'm bored.;"));
		nodes.add(new Node(SettingsType.GENERAL, "general.colors.npc-colour",
				"f"));
		nodes.add(new Node(SettingsType.GENERAL, "items.basic.talk-items",
				"340,"));
		nodes.add(new Node(SettingsType.GENERAL, "items.basic.select-items",
				"*"));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.chat.selection-message",
				"<g>You selected <y><npc><g> (ID <y><npcid><g>)."));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.chat.creation-message",
				"<g>The NPC <y><npc><g> was born!"));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.defaults.enable-following", true));
		nodes.add(new Node(SettingsType.GENERAL, "ticks.saving.save-often",
				true));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.colors.use-npc-colours", true));
		nodes.add(new Node(SettingsType.GENERAL, "ticks.saving.use-task", true));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.selection.quick-select", false));
		nodes.add(new Node(SettingsType.GENERAL, "general.debug-mode", false));
		nodes.add(new Node(SettingsType.GENERAL, "general.notify-updates", true));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.chat.slashes-to-spaces", true));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.defaults.talk-when-close", false));
		nodes.add(new Node(SettingsType.GENERAL,
				"general.use-bukkit-permissions", false));
		nodes.add(new Node(SettingsType.GENERAL, "range.pathfinding", 16F));
		// economy.yml
		nodes.add(new Node(SettingsType.ECONOMY, "economy.use-economy", true));
		nodes.add(new Node(SettingsType.ECONOMY, "prices.basic.creation", 100));
		// mobs.yml
		nodes.add(new Node(SettingsType.MOB, "general.spawn.delay", 200));
		nodes.add(new Node(SettingsType.MOB, "evil.items.tame-item", 354));
		nodes.add(new Node(SettingsType.MOB, "evil.misc.tame-chance", 5));
		nodes.add(new Node(SettingsType.MOB, "evil.spawn.max", 2));
		nodes.add(new Node(SettingsType.MOB, "evil.misc.names",
				"Evil_aPunch,Evil_fullwall,Evil_Notch,Herobrine,"));
		nodes.add(new Node(SettingsType.MOB, "evil.items.drops",
				"260,357,2256,"));
		nodes.add(new Node(SettingsType.MOB, "evil.spawn.spawn", false));
		nodes.add(new Node(SettingsType.MOB, "evil.misc.failed-tame-messages",
				"Ha! You can't tame me!;Nice try, <name>!;Muahahaha, I am evil!;"));
	}
}