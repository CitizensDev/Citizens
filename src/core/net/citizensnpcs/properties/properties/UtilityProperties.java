package net.citizensnpcs.properties.properties;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.citizensnpcs.Settings;
import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPCType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UtilityProperties {
	private static ConfigurationHandler settings;
	private static ConfigurationHandler mobs;

	public static void initialize() {
		settings = new ConfigurationHandler("plugins/Citizens/citizens.yml");
		mobs = new ConfigurationHandler("plugins/Citizens/mobs.yml");
	}

	public static Storage getSettings() {
		return settings;
	}

	public static Storage getMobSettings() {
		return mobs;
	}

	public static double getPrice(String path) {
		return settings.getDouble("economy.prices." + path);
	}

	public static int getCurrencyID(String string) {
		int ID = settings.getInt(string);
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

	// returns whether the given item ID is usable as a tool
	public static boolean isHoldingTool(String key, Player player) {
		List<String> item = Arrays.asList(getSettings().getString(
				Settings.getPath(key)).split(","));
		if (item.contains("*")) {
			return true;
		}
		for (String s : item) {
			boolean isShift = false;
			if (s.contains("SHIFT-")) {
				s = s.replace("SHIFT-", "");
				isShift = true;
			}
			if (Integer.parseInt(s) == player.getItemInHand().getTypeId()
					&& isShift == player.isSneaking()) {
				return true;
			}
		}
		return false;
	}
}