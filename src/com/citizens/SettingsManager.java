package com.citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.citizens.interfaces.Storage;
import com.citizens.npctypes.CitizensNPCManager;
import com.citizens.properties.properties.UtilityProperties;
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

	public enum Constant {
		// citizens.yml
		GuardRespawnDelay(Config.SETTINGS, "ticks.guards.respawn-delay", 100),
		HealerGiveHealthItem(
				Config.SETTINGS,
				"items.healers.give-health-item",
				35), HealerTakeHealthItem(
				Config.SETTINGS,
				"items.healers.take-health-item",
				278), HealerHealthRegenIncrement(
				Config.SETTINGS,
				"ticks.healers.health-regen-increment",
				12000), MaxWizardMana(
				Config.SETTINGS,
				"general.wizards.max-mana",
				100), WizardMaxLocations(
				Config.SETTINGS,
				"general.wizards.wizard-max-locations",
				10), WizardInteractItem(
				Config.SETTINGS,
				"items.wizards.interact-item",
				288), WizardManaRegenItem(
				Config.SETTINGS,
				"items.wizards.mana-regen-item",
				348), WizardManaRegenRate(
				Config.SETTINGS,
				"ticks.wizards.mana-regen-rate",
				6000), DefaultBouncerProtectionRadius(
				Config.SETTINGS,
				"range.guards.default-bouncer-protection-radius",
				10), PayForHealerHeal(
				Config.SETTINGS,
				"general.healers.pay-for-heal",
				true), RegenHealerHealth(
				Config.SETTINGS,
				"general.healers.regen-health",
				true), RegenWizardMana(
				Config.SETTINGS,
				"general.wizards.regen-mana",
				true),
		// mobs.yml
		EvilNPCTameItem(Config.MOB, "evil.items.tame-item", 354),
		EvilNPCTameChance(Config.MOB, "evil.misc.tame-chance", 5),
		MaxEvils(Config.MOB, "evil.spawn.max", 2),
		MaxPirates(Config.MOB, "pirates.spawn.max", 2),
		SpawnTaskDelay(Config.MOB, "general.spawn.delay", 200),
		EvilNames(
				Config.MOB,
				"evil.misc.names",
				"Evil_aPunch,Evil_fullwall,Evil_Notch,Herobrine,"),
		PirateNames(
				Config.MOB,
				"pirates.misc.names",
				"Pirate_Pete,Piratebay,Jack_Sparrow,"),
		FailureToTameMessages(
				Config.MOB,
				"evil.misc.failed-tame-messages",
				"Ha! You can't tame me!;Nice try, <name>!;Muahahaha, I am evil!;"),
		PirateStealMessages(
				Config.MOB,
				"pirates.misc.steal-messages",
				"I stole yer booty.;Aaargh.;"),
		EvilDrops(Config.MOB, "evil.items.drops", "260,357,2256,"),
		SpawnEvils(Config.MOB, "evil.spawn.spawn", false),
		SpawnPirates(Config.MOB, "pirates.spawn.spawn", false);
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
		Messaging.debug("Loading settings");
		Storage local = null;
		boolean found = false;
		for (SettingsType type : SettingsType.values()) {
			if (local != null && found) {
				local.save();
				found = false;
			}
			switch (type) {
			case GENERAL:
				local = UtilityProperties.getSettings();
				break;
			case ECONOMY:
				local = UtilityProperties.getEconomySettings();
				break;
			case MOB:
				local = UtilityProperties.getMobSettings();
				break;
			default:
				local = UtilityProperties.getSettings();
				break;
			}
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
				if (!local.keyExists(node.getPath())) {
					Messaging.log("Writing default setting " + node.getPath()
							+ ".");
					local.setRaw(node.getPath(), node.getValue());
					found = true;
				} else {
					node.set(local.getRaw(node.getPath()));
				}
				loadedNodes.put(node.getPath(), node.getValue());
			}
		}
		if (found) {
			local.save();
		}
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
		nodes.add(new Node(SettingsType.GENERAL, "range.guards.pathfinding",
				16F));
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
		nodes.add(new Node(SettingsType.GENERAL, "items.item-list-on", true));
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
		// economy.yml
		nodes.add(new Node(SettingsType.ECONOMY, "economy.use-economy", true));
	}
}