package com.fullwall.Citizens;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;

public class Constants {
	public static int evilNPCTameItem = 354;
	public static int evilNPCTameChance = 5;
	public static int healerGiveHealthItem = 35;
	public static int healerHealthRegenIncrement = 12000;
	public static int healerTakeHealthItem = 276;
	public static int maxEvils = 2;
	public static int maxPirates = 2;
	public static int maxNPCsPerPlayer = 10;
	public static int maxStationaryTicks = -1;
	public static int maxPathingTicks = -1;
	public static int maxWizardMana = 100;
	public static int saveDelay = 72000;
	public static int spawnTaskDelay = 200;
	public static int spawnPiratesDelay = 200;
	public static int tickDelay = 1;
	public static int wizardMaxLocations = 10;
	public static int wizardInteractItem = 288;
	public static int wizardManaRegenItem = 348;
	public static int wizardManaRegenRate = 6000;

	public static double npcRange = 5;
	public static double defaultBouncerProtectionRadius = 10;
	public static final double JUMP_FACTOR = 0.08D;
	public static final double GRAVITY = 0.02D;
	public static final double PATH_LEEWAY = 0.5D;

	public static float pathFindingRange = 16F;

	public static String chatFormat = "[%name%]: ";
	public static String convertToSpaceChar = "/";
	public static String defaultText = "Hello.;How are you today?;Having a nice day?;Good weather today.;Stop hitting me!;I'm bored.;";
	public static String npcColour = "�f";
	public static String talkItems = "340,";
	public static String selectItems = "*";
	public static String evilNames = "Herobrine,aPunch,fullwall,Notch,EvilSeph,";
	public static String pirateNames = "Pirate_Pete,Piratebay,Jack_Sparrow,";
	public static String failureToTameMessages = "Ha! You can't tame me!;Nice try, <name>!;Muahahaha, I am evil!;";
	public static String pirateStealMessages = "I stole yer booty.;Aaargh.;I am Captain Jack Sparrow!;";
	public static String evilDrops = "260,357,2256,";

	public static boolean defaultFollowingEnabled = true;
	public static boolean payForHealerHeal = true;
	public static boolean regenHealerHealth = true;
	public static boolean regenWizardMana = true;
	public static boolean saveOften = true;
	public static boolean useEconomy = true;
	public static boolean useItemList = true;
	public static boolean useNPCColours = true;
	public static boolean useSaveTask = true;

	public static boolean convertSlashes = false;
	public static boolean defaultTalkWhenClose = false;
	public static boolean spawnEvils = false;
	public static boolean spawnPirates = false;
	public static boolean useEconplugin = false;

	/**
	 * Sets up miscellaneous variables, mostly reading from property files.
	 */
	public static void setupVariables() {
		// ####Citizens settings####
		Storage settings = UtilityProperties.getSettings();

		// Boolean defaults
		convertSlashes = settings.getBoolean("general.chat.slashes-to-spaces");
		defaultFollowingEnabled = settings
				.getBoolean("general.defaults.enable-following");
		defaultTalkWhenClose = settings
				.getBoolean("general.defaults.talk-when-close");
		useNPCColours = settings.getBoolean("general.colors.use-npc-colours");
		useItemList = settings.getBoolean("items.item-list-on");
		saveOften = settings.getBoolean("ticks.saving.save-often");
		useSaveTask = settings.getBoolean("ticks.saving.use-task");
		payForHealerHeal = settings.getBoolean("general.healers.pay-for-heal");
		regenHealerHealth = settings.getBoolean("general.healers.regen-health");
		regenWizardMana = settings.getBoolean("general.wizards.regen-mana");

		// String defaults
		chatFormat = settings.getString("general.chat.format");
		defaultText = settings.getString("general.chat.default-text");
		npcColour = settings.getString("general.colors.npc-colour");
		talkItems = settings.getString("items.basic.talk-items");
		selectItems = settings.getString("items.basic.select-items");

		// Double defaults
		defaultBouncerProtectionRadius = settings
				.getDouble("range.guards.default-bouncer-protection-radius");
		npcRange = settings.getDouble("range.basic.look");
		pathFindingRange = (float) settings
				.getDouble("range.guards.pathfinding");

		// int defaults
		healerGiveHealthItem = settings
				.getInt("items.healers.give-health-item");
		healerTakeHealthItem = settings
				.getInt("items.healers.take-health-item");
		healerHealthRegenIncrement = settings
				.getInt("ticks.healers.health-regen-increment");
		wizardManaRegenRate = settings.getInt("ticks.wizards.mana-regen-rate");
		maxNPCsPerPlayer = settings.getInt("general.limits.npcs-per-player");
		maxPathingTicks = settings.getInt("ticks.pathing.max-pathing");
		maxStationaryTicks = settings.getInt("ticks.pathing.max-stationary");
		saveDelay = settings.getInt("ticks.saving.delay");
		tickDelay = settings.getInt("ticks.general.delay");
		wizardMaxLocations = settings
				.getInt("general.wizards.wizard-max-locations");
		wizardInteractItem = settings.getInt("items.wizards.interact-item");
		wizardManaRegenItem = settings.getInt("items.wizards.mana-regen-item");
		maxWizardMana = settings.getInt("general.wizards.max-mana");

		// ####Economy settings####
		Storage economy = UtilityProperties.getEconomySettings();
		useEconomy = economy.getBoolean("economy.use-economy");
		useEconplugin = economy.getBoolean("economy.use-econplugin");

		EconomyHandler.setUpVariables(useEconomy, useEconplugin);

		// ####Mob settings####
		Storage mobs = UtilityProperties.getMobSettings();

		// Load evils.
		evilNames = mobs.getString("evil.misc.names");
		pirateNames = mobs.getString("pirates.misc.names");

		spawnEvils = mobs.getBoolean("evil.spawn.spawn");
		spawnPirates = mobs.getBoolean("pirates.spawn.spawn");

		maxEvils = mobs.getInt("evil.spawn.max");
		maxPirates = mobs.getInt("pirates.spawn.max");
		spawnTaskDelay = mobs.getInt("general.spawn.delay");
		evilNPCTameItem = mobs.getInt("evil.items.tame-item");
		evilNPCTameChance = mobs.getInt("evil.misc.tame-chance");
		failureToTameMessages = mobs
				.getString("evil.misc.failed-tame-messages");
		pirateStealMessages = mobs.getString("pirates.misc.steal-messages");
		evilDrops = mobs.getString("evil.items.drops");
	}
}