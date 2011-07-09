package com.citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Defaults {
	public static final Map<String, String> settingsDefaults = writeDefaultSettings();
	public static final Map<String, String> mobDefaults = writeMobSettings();
	public static final Map<String, String> economyDefaults = writeEconomySettings();

	public static final Map<String, String> settingsRenames = writeSettingsRenames();
	public static final Map<String, String> economyRenames = writeEconomyRenames();
	public static final Map<String, String> mobRenames = writeMobRenames();

	public static final List<String> settingsDeletes = writeSettingsDeletes();
	public static final List<String> economyDeletes = writeEconomyDeletes();
	public static final List<String> mobDeletes = writeMobDeletes();

	private static HashMap<String, String> writeDefaultSettings() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		// General Settings
		nodes.put("general.convert-old", "true");
		nodes.put("general.limits.npcs-per-player", "10");
		nodes.put("general.selection.quick-select", "false");
		nodes.put("general.defaults.enable-following", "true");
		nodes.put("general.defaults.talk-when-close", "false");
		nodes.put("general.colors.use-npc-colours", "true");
		nodes.put("general.colors.npc-colour", "f");
		nodes.put("general.chat.format", "[%name%]: ");
		nodes.put("general.chat.slashes-to-spaces", "true");
		nodes.put("general.chat.selection-message",
				"<g>You selected <y><npc><g> (ID <y><npcid><g>).");
		nodes.put("general.chat.creation-message",
				"<g>The NPC <y><npc><g> was born!");
		nodes.put(
				"general.chat.default-text",
				"Hello.;How are you today?;Having a nice day?;Good weather today.;Stop hitting me!;I'm bored.;");
		nodes.put("general.wizards.wizard-max-locations", "10");
		nodes.put("general.healers.pay-for-heal", "true");
		nodes.put("general.healers.regen-health", "true");
		nodes.put("general.wizards.regen-mana", "true");
		nodes.put("general.wizards.max-mana", "100");
		// Item Settings
		nodes.put("items.overrides.30", "Cobweb");
		nodes.put("items.overrides.35", "Cloth");
		nodes.put("items.item-list-on", "true");
		nodes.put("items.basic.talk-items", "340,");
		nodes.put("items.basic.select-items", "*");
		nodes.put("items.healers.take-health-item", "276");
		nodes.put("items.healers.give-health-item", "35");
		nodes.put("items.wizards.interact-item", "288");
		nodes.put("items.wizards.mana-regen-item", "348");
		// Tick Settings
		nodes.put("ticks.general.delay", "1");
		nodes.put("ticks.guards.respawn-delay", "100");
		nodes.put("ticks.saving.use-task", "true");
		nodes.put("ticks.saving.save-often", "true");
		nodes.put("ticks.saving.delay", "72000");
		nodes.put("ticks.waypoints.right-click-pause", "70");
		nodes.put("ticks.pathing.max-stationary", "25");
		nodes.put("ticks.pathing.max-pathing", "-1");
		nodes.put("ticks.healers.health-regen-increment", "12000");
		nodes.put("ticks.wizards.mana-regen-rate", "6000");
		// Range Settings
		nodes.put("range.basic.look", "4");
		nodes.put("range.guards.default-bouncer-protection-radius", "5");
		nodes.put("range.guards.pathfinding", "16");
		return nodes;
	}

	private static HashMap<String, String> writeEconomySettings() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		nodes.put("economy.use-economy", "true");
		nodes.put("economy.use-econplugin", "false");

		nodes.put("prices.basic.creation.item", "10");
		nodes.put("prices.basic.creation.item-currency-id", "37");
		nodes.put("prices.basic.creation.econplugin", "100");

		nodes.put("prices.bandit.creation.item", "10");
		nodes.put("prices.bandit.creation.item-currency-id", "37");
		nodes.put("prices.bandit.creation.econplugin", "100");

		nodes.put("prices.blacksmith.creation.item", "10");
		nodes.put("prices.blacksmith.creation.item-currency-id", "37");
		nodes.put("prices.blacksmith.creation.econplugin", "100");

		nodes.put("prices.blacksmith.armorrepair.item-currency-id", "37");
		nodes.put("prices.blacksmith.armorrepair.econplugin.leather", "0.25");
		nodes.put("prices.blacksmith.armorrepair.econplugin.gold", "0.50");
		nodes.put("prices.blacksmith.armorrepair.econplugin.chainmail", "0.75");
		nodes.put("prices.blacksmith.armorrepair.econplugin.iron", "1");
		nodes.put("prices.blacksmith.armorrepair.econplugin.diamond", "1.25");
		nodes.put("prices.blacksmith.armorrepair.item.leather", "1");
		nodes.put("prices.blacksmith.armorrepair.item.gold", "2");
		nodes.put("prices.blacksmith.armorrepair.item.chainmail", "3");
		nodes.put("prices.blacksmith.armorrepair.item.iron", "4");
		nodes.put("prices.blacksmith.armorrepair.item.diamond", "5");

		nodes.put("prices.blacksmith.toolrepair.item-currency-id", "37");
		nodes.put("prices.blacksmith.toolrepair.econplugin.wood", "0.25");
		nodes.put("prices.blacksmith.toolrepair.econplugin.gold", "0.50");
		nodes.put("prices.blacksmith.toolrepair.econplugin.stone", "0.75");
		nodes.put("prices.blacksmith.toolrepair.econplugin.iron", "1");
		nodes.put("prices.blacksmith.toolrepair.econplugin.diamond", "1.25");
		nodes.put("prices.blacksmith.toolrepair.econplugin.misc", "0.50");
		nodes.put("prices.blacksmith.toolrepair.item.wood", "1");
		nodes.put("prices.blacksmith.toolrepair.item.gold", "2");
		nodes.put("prices.blacksmith.toolrepair.item.stone", "3");
		nodes.put("prices.blacksmith.toolrepair.item.iron", "4");
		nodes.put("prices.blacksmith.toolrepair.item.diamond", "5");
		nodes.put("prices.blacksmith.toolrepair.item.misc", "2");

		nodes.put("prices.guard.creation.item", "10");
		nodes.put("prices.guard.creation.item-currency-id", "37");
		nodes.put("prices.guard.creation.econplugin", "100");

		nodes.put("prices.healer.creation.item", "10");
		nodes.put("prices.healer.creation.item-currency-id", "37");
		nodes.put("prices.healer.creation.econplugin", "100");

		nodes.put("prices.healer.levelup.item", "10");
		nodes.put("prices.healer.levelup.item-currency-id", "37");
		nodes.put("prices.healer.levelup.econplugin", "100");

		nodes.put("prices.healer.heal.item", "10");
		nodes.put("prices.healer.heal.item-currency-id", "37");
		nodes.put("prices.healer.heal.econplugin", "100");

		nodes.put("prices.quester.creation.item", "10");
		nodes.put("prices.quester.creation.item-currency-id", "37");
		nodes.put("prices.quester.creation.econplugin", "100");

		nodes.put("prices.trader.creation.item", "20");
		nodes.put("prices.trader.creation.item-currency-id", "37");
		nodes.put("prices.trader.creation.econplugin", "250");

		nodes.put("prices.wizard.creation.item", "20");
		nodes.put("prices.wizard.creation.item-currency-id", "37");
		nodes.put("prices.wizard.creation.econplugin", "150");

		nodes.put("prices.wizard.teleport.item", "20");
		nodes.put("prices.wizard.teleport.item-currency-id", "37");
		nodes.put("prices.wizard.teleport.econplugin", "150");

		nodes.put("prices.wizard.changetime.item", "20");
		nodes.put("prices.wizard.changetime.item-currency-id", "37");
		nodes.put("prices.wizard.changetime.econplugin", "150");

		nodes.put("prices.wizard.spawnmob.item", "20");
		nodes.put("prices.wizard.spawnmob.item-currency-id", "37");
		nodes.put("prices.wizard.spawnmob.econplugin", "150");

		nodes.put("prices.wizard.togglestorm.item", "20");
		nodes.put("prices.wizard.togglestorm.item-currency-id", "37");
		nodes.put("prices.wizard.togglestorm.econplugin", "150");
		return nodes;
	}

	private static HashMap<String, String> writeMobSettings() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		nodes.put("evil.spawn.spawn", "false");
		nodes.put("pirates.spawn.spawn", "false");
		nodes.put("general.spawn.delay", "200");
		nodes.put("evil.spawn.max", "2");
		nodes.put("pirates.spawn.max", "2");
		nodes.put("evil.misc.names",
				"Evil_aPunch,Evil_fullwall,Evil_Notch,Herobrine,");
		nodes.put("pirates.misc.names", "Pirate_Pete,Piratebay,Jack_Sparrow,");
		nodes.put("evil.misc.tame-chance", "5");
		nodes.put("evil.items.tame-item", "354");
		nodes.put("evil.misc.failed-tame-messages",
				"Ha! You can't tame me!;Nice try, <name>!;Muahahaha, I am evil!;");
		nodes.put("pirates.misc.steal-messages", "I stole yer booty.;Aaargh.;");
		nodes.put("evil.items.drops", "260,357,2256,");
		return nodes;
	}

	private static HashMap<String, String> writeSettingsRenames() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		return nodes;
	}

	private static HashMap<String, String> writeEconomyRenames() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		return nodes;
	}

	private static Map<String, String> writeMobRenames() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		return nodes;
	}

	private static ArrayList<String> writeSettingsDeletes() {
		ArrayList<String> nodes = new ArrayList<String>();
		return nodes;
	}

	private static ArrayList<String> writeEconomyDeletes() {
		ArrayList<String> nodes = new ArrayList<String>();
		return nodes;
	}

	private static ArrayList<String> writeMobDeletes() {
		ArrayList<String> nodes = new ArrayList<String>();
		nodes.add("pirates.spawn.delay");
		nodes.add("evil.spawn.delay");
		return nodes;
	}
}