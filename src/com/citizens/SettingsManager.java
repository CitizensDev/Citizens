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
	public static final Map<String, Object> economyDefaults = writeEconomySettings();

	private enum Config {
		ECONOMY, MOB, SETTINGS;

		private final List<Constant> settings = new ArrayList<Constant>();

		public void add(Constant constant) {
			this.settings.add(constant);
		}

		public List<Constant> get() {
			return this.settings;
		}
	}

	public enum Constant {
		// citizens.yml
		GuardRespawnDelay(Config.SETTINGS, "ticks.guards.respawn-delay", 100),
		HealerGiveHealthItem(
				Config.SETTINGS,
				"items.healers.give-health-item",
				35),
		HealerTakeHealthItem(
				Config.SETTINGS,
				"items.healers.take-health-item",
				278),
		HealerHealthRegenIncrement(
				Config.SETTINGS,
				"ticks.healers.health-regen-increment",
				12000),
		MaxStationaryTicks(Config.SETTINGS, "ticks.pathing.max-stationary", -1),
		MaxPathingTicks(Config.SETTINGS, "ticks.pathing.max-pathing", -1),
		MaxWizardMana(Config.SETTINGS, "general.wizards.max-mana", 100),
		SaveDelay(Config.SETTINGS, "ticks.saving.delay", 72000),
		RightClickPause(
				Config.SETTINGS,
				"ticks.waypoints.right-click-pause",
				70),
		TickDelay(Config.SETTINGS, "ticks.general.delay", 1),
		WizardMaxLocations(
				Config.SETTINGS,
				"general.wizards.wizard-max-locations",
				10),
		WizardInteractItem(Config.SETTINGS, "items.wizards.interact-item", 288),
		WizardManaRegenItem(
				Config.SETTINGS,
				"items.wizards.mana-regen-item",
				348),
		WizardManaRegenRate(
				Config.SETTINGS,
				"ticks.wizards.mana-regen-rate",
				6000),
		NPCRange(Config.SETTINGS, "range.basic.look", 5),
		DefaultBouncerProtectionRadius(
				Config.SETTINGS,
				"range.guards.default-bouncer-protection-radius",
				10),
		PathfindingRange(Config.SETTINGS, "range.guards.pathfinding", 16F),
		ChatFormat(Config.SETTINGS, "general.chat.format", "[%name%]: "),
		DefaultText(
				Config.SETTINGS,
				"general.chat.default-text",
				"Hello.;How are you today?;Having a nice day?;Good weather today.;Stop hitting me!;I'm bored.;"),
		NPCColour(Config.SETTINGS, "general.colors.npc-colour", "f"),
		TalkItems(Config.SETTINGS, "items.basic.talk-items", "340,"),
		SelectItems(Config.SETTINGS, "items.basic.select-items", "*"),
		SelectionMessage(
				Config.SETTINGS,
				"general.chat.selection-message",
				"<g>You selected <y><npc><g> (ID <y><npcid><g>)."),
		CreationMessage(
				Config.SETTINGS,
				"general.chat.creation-message",
				"<g>The NPC <y><npc><g> was born!"),
		DefaultFollowingEnabled(
				Config.SETTINGS,
				"general.defaults.enable-following",
				true),
		PayForHealerHeal(Config.SETTINGS, "general.healers.pay-for-heal", true),
		RegenHealerHealth(Config.SETTINGS, "general.healers.regen-health", true),
		RegenWizardMana(Config.SETTINGS, "general.wizards.regen-mana", true),
		SaveOften(Config.SETTINGS, "ticks.saving.save-often", true),
		UseItemList(Config.SETTINGS, "items.item-list-on", true),
		UseNPCColours(Config.SETTINGS, "general.colors.use-npc-colours", true),
		UseSaveTask(Config.SETTINGS, "ticks.saving.use-task", true),
		QuickSelect(Config.SETTINGS, "general.selection.quick-select", false),
		DebugMode(Config.SETTINGS, "general.debug-mode", false),
		NotifyUpdates(Config.SETTINGS, "general.notify-updates", true),
		ConvertSlashes(Config.SETTINGS, "general.chat.slashes-to-spaces", true),
		DefaultTalkWhenClose(
				Config.SETTINGS,
				"general.defaults.talk-when-close",
				false),
		UseSuperPerms(Config.SETTINGS, "general.use-bukkit-permissions", false),
		// economy.yml
		UseEconPlugin(Config.ECONOMY, "economy.use-econplugin", false),
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

		private final Config config;
		private final String path;
		private Object value;

		Constant(Config config, String path, Object value) {
			this.config = config;
			this.path = path;
			this.value = value;
			config.add(this);
		}

		public boolean toBoolean() {
			return (Boolean) this.getValue();
		}

		public double toDouble() {
			if (this.getValue() instanceof Float) {
				return (Float) this.getValue();
			} else if (this.getValue() instanceof Double) {
				return (Double) this.getValue();
			} else {
				return (Integer) this.getValue();
			}
		}

		public int toInt() {
			return (Integer) this.getValue();
		}

		public String getString() {
			return (String) this.getValue();
		}

		public Object getValue() {
			return this.value;
		}

		public void set(Object value) {
			this.value = value;
		}

		public String getPath() {
			return path;
		}

		public Config getType() {
			return config;
		}
	}

	/**
	 * Sets up miscellaneous variables, mostly reading from property files.
	 */
	public static void setupVariables() {
		Messaging.debug("Loading settings");
		Storage local = null;
		boolean found = false;
		for (Config config : Config.values()) {
			if (local != null && found) {
				local.save();
				found = false;
			}
			switch (config) {
			case ECONOMY:
				local = UtilityProperties.getEconomySettings();
				break;
			case MOB:
				local = UtilityProperties.getMobSettings();
				break;
			case SETTINGS:
				local = UtilityProperties.getSettings();
				break;
			default:
				local = UtilityProperties.getSettings();
				break;
			}
			for (Constant constant : config.get()) {
				if (!local.keyExists(constant.getPath())) {
					Messaging.log("Writing default setting "
							+ constant.getPath() + ".");
					local.setRaw(constant.getPath(), constant.getValue());
					found = true;
				} else {
					constant.set(local.getRaw(constant.getPath()));
				}
			}
		}
		if (found) {
			local.save();
		}
	}

	private static HashMap<String, Object> writeEconomySettings() {
		HashMap<String, Object> nodes = new HashMap<String, Object>();
		nodes.put("economy.use-economy", true);
		nodes.put("economy.use-econplugin", false);
		for (String type : Citizens.loadedTypes) {
			nodes.put("prices." + type + ".creation", 100);
			for (Entry<String, Object> entry : CitizensNPCManager.getType(type)
					.getDefaultSettings().entrySet()) {
				nodes.put(entry.getKey(), entry.getValue());
			}
		}
		return nodes;
	}
}