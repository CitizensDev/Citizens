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
		Constants.convertSlashes = UtilityProperties.settings
				.getBoolean("slashes-to-spaces");
		Constants.defaultFollowingEnabled = UtilityProperties.settings
				.getBoolean("default-enable-following");
		Constants.defaultTalkWhenClose = UtilityProperties.settings
				.getBoolean("default-talk-when-close");
		Constants.useSaveTask = UtilityProperties.settings
				.getBoolean("use-save-task");
		Constants.saveOften = UtilityProperties.settings
				.getBoolean("save-delay");
		Constants.useNPCColours = UtilityProperties.settings
				.getBoolean("use-npc-colours");
		// String defaults
		Constants.chatFormat = UtilityProperties.settings
				.getString("chat-format");
		Constants.NPCColour = UtilityProperties.settings
				.getString("npc-colour");
		// Double defaults
		Constants.npcRange = UtilityProperties.settings.getDouble("look-range");
		// Int defaults
		Constants.maxNPCsPerPlayer = UtilityProperties.settings
				.getInt("max-npcs-per-player");
		Constants.healerGiveHealthItem = UtilityProperties.settings
				.getInt("healer-give-health-item");
		Constants.healerTakeHealthItem = UtilityProperties.settings
				.getInt("healer-take-health-item");
		Constants.healerHealthRegenIncrement = UtilityProperties.settings
				.getInt("healer-health-regen-increment");
		Constants.tickDelay = UtilityProperties.settings.getInt("tick-delay");
		Constants.saveDelay = UtilityProperties.settings
				.getInt("save-tick-delay");
		Constants.wizardMaxLocations = UtilityProperties.settings
				.getInt("wizard-max-locations");
		Constants.wizardInteractItem = UtilityProperties.settings
				.getInt("wizard-interact-item");
	}
}