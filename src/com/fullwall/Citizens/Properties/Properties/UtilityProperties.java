package com.fullwall.Citizens.Properties.Properties;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.Properties.ConfigurationHandler;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.Creatures.CreatureNPCType;

public class UtilityProperties {
	private static ConfigurationHandler economy;
	public static PropertyHandler itemlookups;
	private static ConfigurationHandler settings;
	private static ConfigurationHandler mobs;

	public static void initialize() {
		economy = new ConfigurationHandler("plugins/Citizens/economy.yml");
		itemlookups = new PropertyHandler("plugins/Citizens/items.citizens");
		settings = new ConfigurationHandler("plugins/Citizens/citizens.yml");
		mobs = new ConfigurationHandler("plugins/Citizens/mobs.yml");
	}

	public static Storage getSettings() {
		return settings;
	}

	public static Storage getEconomySettings() {
		return economy;
	}

	public static double getPrice(String operation) {
		return economy.getDouble(operation);
	}

	public static int getCurrencyID(String string) {
		int ID = economy.getInt(string);
		return ID == -1 ? 1 : ID;
	}

	public static String getRandomName(CreatureNPCType type) {
		String[] split = type.getPossibleNames().split(",");
		return split[new Random().nextInt(split.length)];
	}

	public static ItemStack getRandomDrop() {
		String[] split = Constants.evilDrops.split(",");
		int id = Integer.parseInt(split[new Random().nextInt(split.length)]);
		int amount = new Random().nextInt(3);
		if (Material.getMaterial(id) != null) {
			if (amount != 0) {
				return new ItemStack(id, amount);
			}
		}
		return null;
	}

	public static Storage getMobSettings() {
		return mobs;
	}
}