package com.fullwall.Citizens.Properties.Properties;

import java.util.Random;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.Properties.ConfigurationHandler;
import com.fullwall.Citizens.Properties.PropertyHandler;

public class UtilityProperties {
	private static ConfigurationHandler economy;
	public static PropertyHandler itemlookups;
	private static ConfigurationHandler settings;

	public static void initialize() {
		economy = new ConfigurationHandler("plugins/Citizens/economy.yml");
		itemlookups = new PropertyHandler(
				"plugins/Citizens/Citizens.itemlookup");
		settings = new ConfigurationHandler("plugins/Citizens/citizens.yml");
	}

	public static Storage getSettings() {
		return settings;
	}

	public static Storage getEconomySettings() {
		return economy;
	}

	public static String getDefaultText() {
		String[] split = Constants.defaultText.split(";");
		String text = "";
		text = split[new Random().nextInt(split.length)];
		return text.replace('&', '§');
	}

	public static double getPrice(String operation) {
		return economy.getDouble(operation);
	}

	public static int getCurrencyID(String string) {
		int ID = economy.getInt(string);
		return ID == -1 ? 1 : ID;
	}

	public static String getRandomName() {
		String[] split = Constants.defaultEvilNames.split(",");
		return split[new Random().nextInt(split.length)];
	}
}