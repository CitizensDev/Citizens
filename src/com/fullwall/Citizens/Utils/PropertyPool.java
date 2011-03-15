package com.fullwall.Citizens.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Location;

import com.fullwall.Citizens.NPCManager;
import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Citizens;

public class PropertyPool {
	public static Logger log = Logger.getLogger("Minecraft");
	public static final PropertyHandler settings = new PropertyHandler(
			"plugins/Citizens/Citizens.settings");
	public static final PropertyHandler texts = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.texts");
	public static final PropertyHandler locations = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.locations");
	public static final PropertyHandler colours = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.colours");
	public static final PropertyHandler items = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.items");

	public static void saveLocation(String name, Location loc) {
		String location = loc.getWorld().getName() + "," + loc.getX() + ","
				+ loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + ","
				+ loc.getPitch();
		locations.setString(name, location);
		if (!locations.getString("list").contains(name))
			locations.setString("list", locations.getString("list") + name
					+ ",");
	}

	public static void saveItems(String name, ArrayList<Integer> items2) {
		String toSave = "";
		for (Integer i : items2) {
			toSave += "" + i + ",";
		}
		items.setString(name, toSave);
	}

	public static void saveColour(String name, String colour) {
		colours.setString(name, "" + colour);
	}

	public static void saveText(String name, ArrayList<String> text) {
		String adding = "";
		for (String string : text) {
			adding += string + ";";
		}
	}

	public static void getSetText(String name) {
		String current = texts.getString(name);
		if (!current.isEmpty()) {
			ArrayList<String> text = new ArrayList<String>();
			for (String string : current.split(";")) {
				text.add(string);
			}
			NPCManager.setBasicNPCText(name, text);
		}
	}

	public static ArrayList<String> getText(String name) {
		String current = texts.getString(name);
		if (!current.isEmpty()) {
			ArrayList<String> text = new ArrayList<String>();
			for (String string : current.split(";")) {
				text.add(string);
			}
			return text;
		} else
			return null;
	}

	public static String getColour(String name) {
		return colours.getString(name);
	}

	public void removeFromFiles(String name) {
		PropertyPool.colours.removeKey(name);
		PropertyPool.items.removeKey(name);
		PropertyPool.locations.removeKey(name);
		PropertyPool.locations.setString("list", PropertyPool.locations
				.getString("list").replace(name + ",", ""));
		PropertyPool.texts.removeKey(name);
		NPCManager.BasicNPCTexts.remove(name);
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

	public static ArrayList<Integer> getItemsFromFile(String name) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		String current = items.getString(name);
		if (current.isEmpty()) {
			current = "0,0,0,0,0,";
			items.setString(name, current);
		}
		for (String s : current.split(",")) {
			array.add(Integer.parseInt(s));
		}
		return array;
	}
	public static Location getLocationFromName(String name) {
		String[] values = PropertyPool.locations.getString(name).split(",");
		if (values.length != 6) { 
			log.info("gotLocationFromName didn't have 6 values in values variable!");
			return null;
		}else{
		Location loc = new Location(Citizens.plugin.getServer().getWorld(
				values[0]), Double.parseDouble(values[1]),
				Double.parseDouble(values[2]), Double.parseDouble(values[3]),
				Float.parseFloat(values[4]), Float.parseFloat(values[5]));
		return loc;
		}
	}

	public static void changeName(String name, String changeTo) {
		ArrayList<String> texts = PropertyPool.getText(name);
		String colour = PropertyPool.getColour(name);
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(name);
		PropertyPool.saveColour(changeTo, colour);
		PropertyPool.saveText(changeTo, texts);
		PropertyPool.saveItems(changeTo, items);
	}
}
