package com.fullwall.Citizens;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;

public class Constants {
	public static int tickDelay = 1;
	public static int saveDelay = 72000;
	public static int maxNPCsPerPlayer = 10;
	public static int healerGiveHealthItem = 35;
	public static int healerTakeHealthItem = 276;
	public static int wizardMaxLocations = 10;
	public static int wizardInteractItem = 288;
	public static int evilNPCTameItem = 354;
	public static int healerHealthRegenIncrement = 12000;
	public static int banditStealRadius = 5;
	public static int maxStationaryTicks = 25;
	public static int maxPathingTicks = -1;

	public static double npcRange = 5;
	public static double defaultBouncerProtectionRadius = 10;
	public static final double JUMP_FACTOR = 0.1D;
	public static final double GRAVITY = 0.02D;
	public static final double PATH_LEEWAY = 0.5D;

	public static float pathFindingRange = 16F;

	public static String chatFormat = "[%name%]: ";
	public static String convertToSpaceChar = "/";
	public static String npcColour = "�f";

	public static boolean convertSlashes = false;
	public static boolean defaultFollowingEnabled = true;
	public static boolean defaultTalkWhenClose = false;
	public static boolean saveOften = true;
	public static boolean useSaveTask = true;
	public static boolean useNPCColours = true;

	/**
	 * Sets up miscellaneous variables, mostly reading from property files.
	 */
	public static void setupVariables() {
		EconomyHandler.setUpVariables();
		Storage settings = UtilityProperties.settings;

		// Boolean defaults
		convertSlashes = settings
				.getBoolean("GeneralSettings.Chat.slashes-to-spaces");
		defaultFollowingEnabled = settings
				.getBoolean("GeneralSettings.default-enable-following");
		defaultTalkWhenClose = settings
				.getBoolean("GeneralSettings.default-talk-when-close");
		useSaveTask = settings.getBoolean("TickSettings.Saving.use-save-task");
		saveOften = settings.getBoolean("TickSettings.Saving.save-often");
		useNPCColours = settings
				.getBoolean("GeneralSettings.Colors.use-npc-colours");

		// String defaults
		chatFormat = settings.getString("GeneralSettings.Chat.chat-format");
		npcColour = UtilityProperties.settings
				.getString("GeneralSettings.Colors.npc-colour");

		// Double defaults
		npcRange = settings.getDouble("RangeSettings.Basic.look-range");
		pathFindingRange = (float) settings
				.getDouble("RangeSettings.Guards.pathfinding-range");
		defaultBouncerProtectionRadius = settings
				.getDouble("RangeSettings.Guards.default-bouncer-protection-radius");

		// int defaults
		maxNPCsPerPlayer = settings
				.getInt("GeneralSettings.max-NPCs-per-player");
		healerGiveHealthItem = settings
				.getInt("ItemSettings.Healers.healer-give-health-item");
		healerTakeHealthItem = settings
				.getInt("ItemSettings.Healers.healer-take-health-item");
		healerHealthRegenIncrement = settings
				.getInt("TickSettings.Healers.health-regen-increment");
		tickDelay = settings.getInt("TickSettings.General.tick-delay");
		saveDelay = settings.getInt("TickSettings.Saving.save-tick-delay");
		wizardMaxLocations = settings
				.getInt("GeneralSettings.Wizards.wizard-max-locations");
		wizardInteractItem = settings
				.getInt("ItemSettings.Wizards.wizard-interact-item");
		evilNPCTameItem = settings
				.getInt("ItemSettings.EvilNPCs.evil-npc-tame-item");
		banditStealRadius = settings
				.getInt("RangeSettings.Bandits.bandit-steal-radius");
		maxPathingTicks = settings
				.getInt("TickSettings.Pathing.max-pathing-ticks");
		maxStationaryTicks = settings
				.getInt("TickSettings.Pathing.max-stationary-ticks");
	}
}