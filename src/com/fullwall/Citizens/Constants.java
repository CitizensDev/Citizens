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

	public static double npcRange = 5;

	public static String chatFormat = "[%name%]: ";
	public static String convertToSpaceChar = "/";
	public static String NPCColour = "§f";

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
		// Boolean defaults
		convertSlashes = UtilityProperties.settings
				.getBoolean("slashes-to-spaces");
		defaultFollowingEnabled = UtilityProperties.settings
				.getBoolean("default-enable-following");
		defaultTalkWhenClose = UtilityProperties.settings
				.getBoolean("default-talk-when-close");
		useSaveTask = UtilityProperties.settings.getBoolean("use-save-task");
		saveOften = UtilityProperties.settings.getBoolean("save-delay");
		useNPCColours = UtilityProperties.settings
				.getBoolean("use-npc-colours");
		// String defaults
		chatFormat = UtilityProperties.settings.getString("chat-format");
		NPCColour = UtilityProperties.settings.getString("npc-colour");
		// Double defaults
		npcRange = UtilityProperties.settings.getDouble("look-range");
		// Int defaults
		maxNPCsPerPlayer = UtilityProperties.settings
				.getInt("max-npcs-per-player");
		healerGiveHealthItem = UtilityProperties.settings
				.getInt("healer-give-health-item");
		healerTakeHealthItem = UtilityProperties.settings
				.getInt("healer-take-health-item");
		healerHealthRegenIncrement = UtilityProperties.settings
				.getInt("healer-health-regen-increment");
		tickDelay = UtilityProperties.settings.getInt("tick-delay");
		saveDelay = UtilityProperties.settings.getInt("save-tick-delay");
		wizardMaxLocations = UtilityProperties.settings
				.getInt("wizard-max-locations");
		wizardInteractItem = UtilityProperties.settings
				.getInt("wizard-interact-item");
		banditStealRadius = UtilityProperties.settings
				.getInt("bandit-steal-radius");
	}
}