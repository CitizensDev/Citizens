package com.fullwall.Citizens;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;

public class Constants {
	public static int tickDelay = 1;
	public static int saveDelay = 72000;
	public static int maxNPCsPerPlayer = 10;
	public static int healerGiveHealthItem = 35;
	public static int healerTakeHealthItem = 276;
	public static int wizardMaxLocations = 10;
	public static int wizardInteractItem = 288;
	public static int questerInteractItem = 339;
	public static int questAcceptItem = 341;
	public static int questDenyItem = 338;
	public static int healerHealthRegenIncrement = 12000;
	public static int banditStealRadius = 5;
	public static int maxStationaryTicks = 25;
	public static int maxPathingTicks = -1;

	public static double npcRange = 5;
	public static double defaultGuardProtectionRadius = 10;
	public static final double JUMP_FACTOR = 0.1D;
	public static final double GRAVITY = 0.02D;
	public static final double PATH_LEEWAY = 0.5D;

	public static float pathFindingRange = 16F;

	public static String chatFormat = "[%name%]: ";
	public static String convertToSpaceChar = "/";
	public static String NPCColour = "�f";

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
		PropertyHandler settings = UtilityProperties.settings;

		// Boolean defaults
		convertSlashes = settings.getBoolean("slashes-to-spaces");
		defaultFollowingEnabled = settings
				.getBoolean("default-enable-following");
		defaultTalkWhenClose = settings.getBoolean("default-talk-when-close");
		useSaveTask = settings.getBoolean("use-save-task");
		saveOften = settings.getBoolean("save-often");
		useNPCColours = settings.getBoolean("use-npc-colours");

		// String defaults
		chatFormat = settings.getString("chat-format");
		NPCColour = UtilityProperties.settings.getString("npc-colour");

		// Double defaults
		npcRange = settings.getDouble("look-range");
		pathFindingRange = (float) settings.getDouble("pathfinding-range");
		defaultGuardProtectionRadius = settings
				.getDouble("guard-protection-radius");

		// int defaults
		maxNPCsPerPlayer = settings.getInt("max-npcs-per-player");
		healerGiveHealthItem = settings.getInt("healer-give-health-item");
		healerTakeHealthItem = settings.getInt("healer-take-health-item");
		healerHealthRegenIncrement = settings
				.getInt("healer-health-regen-increment");
		tickDelay = settings.getInt("tick-delay");
		saveDelay = settings.getInt("save-tick-delay");
		wizardMaxLocations = settings.getInt("wizard-max-locations");
		wizardInteractItem = settings.getInt("wizard-interact-item");
		banditStealRadius = settings.getInt("bandit-steal-radius");
		maxPathingTicks = settings.getInt("max-pathing-ticks");
		maxStationaryTicks = settings.getInt("max-stationary-ticks");
	}
}