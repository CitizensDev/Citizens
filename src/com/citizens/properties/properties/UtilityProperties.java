package com.citizens.properties.properties;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.citizens.interfaces.Storage;
import com.citizens.npcs.NPCManager;
import com.citizens.properties.ConfigurationHandler;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.npclib.creatures.CreatureNPCType;

public class UtilityProperties {
	private static ConfigurationHandler economy;
	private static ConfigurationHandler settings;
	private static ConfigurationHandler mobs;

	public static void initialize() {
		economy = new ConfigurationHandler("plugins/Citizens/economy.yml");
		settings = new ConfigurationHandler("plugins/Citizens/citizens.yml");
		mobs = new ConfigurationHandler("plugins/Citizens/mobs.yml");
	}

	public static Storage getSettings() {
		return settings;
	}

	public static Storage getEconomySettings() {
		return economy;
	}

	public static Storage getMobSettings() {
		return mobs;
	}

	public static double getPrice(String operation) {
		return economy.getDouble(operation);
	}

	public static int getCurrencyID(String string) {
		int ID = economy.getInt(string);
		return ID == -1 ? 1 : ID;
	}

	public static String getItemOverride(int ID) {
		return settings.getString("items.overrides." + ID);
	}

	public static String getRandomName(CreatureNPCType type) {
		String[] split = type.getPossibleNames().split(",");
		return split[new Random().nextInt(split.length)];
	}

	public static ItemStack getRandomDrop(String drops) {
		String[] split = drops.split(",");
		int id = Integer.parseInt(split[new Random().nextInt(split.length)]);
		int amount = new Random().nextInt(3);
		if (Material.getMaterial(id) != null) {
			if (amount != 0) {
				return new ItemStack(id, amount);
			}
		}
		return null;
	}

	public static int getNPCCount(String name) {
		int count = 0;
		for (HumanNPC npc : NPCManager.getList().values()) {
			if (npc.getOwner().equals(name)) {
				count++;
			}
		}
		return count;
	}
}