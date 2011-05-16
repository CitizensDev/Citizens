package com.fullwall.Citizens.Properties.Properties;

import java.util.Random;

import com.fullwall.Citizens.PropertyHandler;

public class UtilityProperties {
	public static PropertyHandler economy = new PropertyHandler(
			"plugins/Citizens/Citizens.economy");
	public static PropertyHandler itemlookups = new PropertyHandler(
			"plugins/Citizens/Citizens.itemlookup");
	public static PropertyHandler settings = new PropertyHandler(
			"plugins/Citizens/Citizens.settings");

	public static int getMaxNPCsPerPlayer() {
		return settings.getInt("max-NPCs-per-player");
	}

	public static String getDefaultText() {
		String[] split = settings.getString("default-text").split(";");
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
		return economy.getBoolean("use-economy");
	}

	public static boolean checkServerEconomyEnabled() {
		return economy.getBoolean("use-econplugin");
	}
}
