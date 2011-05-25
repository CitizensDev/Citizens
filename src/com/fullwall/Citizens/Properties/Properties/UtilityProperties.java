package com.fullwall.Citizens.Properties.Properties;

import java.util.Random;

import com.fullwall.Citizens.ConfigurationHandler;
import com.fullwall.Citizens.PropertyHandler;

public class UtilityProperties {
	public static final ConfigurationHandler economy = new ConfigurationHandler(
			"plugins/Citizens/economy.yml");
	public static final PropertyHandler itemlookups = new PropertyHandler(
			"plugins/Citizens/Citizens.itemlookup");
	public static final ConfigurationHandler settings = new ConfigurationHandler(
			"plugins/Citizens/config.yml");

	public static int getMaxNPCsPerPlayer() {
		return settings.getInt("GeneralSettings.max-NPCs-per-player");
	}

	public static String getDefaultText() {
		String[] split = settings.getString("GeneralSettings.Chat.default-text").split(";");
		String text;
		if (split != null) {
			text = split[new Random(System.currentTimeMillis())
					.nextInt(split.length)];
			if (text == null)
				text = "";
		} else
			text = "";
		return text.replace('&', '§');
	}

	public static double getPrice(String operation) {
		return economy.getDouble(operation);
	}

	public static int getCurrencyID(String string) {
		int ID = economy.getInt(string);
		return ID == -1 ? 1 : ID;
	}

	public static boolean checkEconomyEnabled() {
		return economy.getBoolean("economy.use-economy");
	}

	public static boolean checkServerEconomyEnabled() {
		return economy.getBoolean("economy.use-econplugin");
	}
}