package net.citizensnpcs.properties.properties;

import java.util.Random;

import net.citizensnpcs.Settings;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.properties.DataSource;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UtilityProperties {
	private static final DataSource config = new ConfigurationHandler(
			"plugins/Citizens/citizens.yml");
	private static final DataSource mobs = new ConfigurationHandler(
			"plugins/Citizens/mobs.yml");

	public static void load() {
		config.load();
		mobs.load();
	}

	public static DataSource getConfig() {
		return config;
	}

	public static DataSource getMobSettings() {
		return mobs;
	}

	public static double getPrice(String path) {
		return config.getKey("economy.prices").getDouble(path);
	}

	public static int getCurrencyID(String string) {
		int ID = config.getKey(string).getInt("");
		return ID == -1 ? 1 : ID;
	}

	public static String getItemOverride(int ID) {
		return config.getKey("items.overrides").getString(Integer.toString(ID));
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
		for (HumanNPC npc : NPCManager.getNPCs()) {
			if (npc.getOwner().equals(name)) {
				count++;
			}
		}
		return count;
	}

	// returns whether the given item ID is usable as a tool
	public static boolean isHoldingTool(String key, Player player) {
		String[] item = config.getKey(Settings.getPath(key)).getString("")
				.split(",");
		for (String s : item) {
			if (s.equals("*"))
				return true;
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