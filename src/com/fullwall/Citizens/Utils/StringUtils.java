package com.fullwall.Citizens.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import com.fullwall.Citizens.Citizens;

public class StringUtils {

	public static String getColourFromString(String s) {
		if (s.startsWith("&")) {
			String colour = "";
			colour = s.replace("&", "§").substring(0, 1);
			return colour;
		}
		return "§f";
	}

	public static String stripColour(String toStrip) {
		return ChatColor.stripColor(toStrip);
	}

	public static Location getLocationFromName(String name) {
		String[] values = PropertyPool.locations.getString(name).split(",");
		Location loc = new Location(Citizens.plugin.getServer().getWorld(
				values[0]), Double.parseDouble(values[1]),
				Double.parseDouble(values[2]), Double.parseDouble(values[3]),
				Float.parseFloat(values[4]), Float.parseFloat(values[5]));
		return loc;
	}

	public static ArrayList<Integer> getItemsFromFile(String name) {
		if (!PropertyPool.items.getString(name).isEmpty()) {
			ArrayList<Integer> items = new ArrayList<Integer>();
			for (String s : PropertyPool.items.getString(name).split(",")) {
				items.add(Integer.parseInt(s));
			}
			return items;
		} else {
			ArrayList<Integer> items = new ArrayList<Integer>();
			for (int i = 0; i != 5; ++i)
				items.add(0);
			return items;
		}
	}

	public static ArrayList<String> colourise(ArrayList<String> text) {
		int index = 0;
		ArrayList<String> newText = new ArrayList<String>();
		for (String string : text) {
			newText.add(index, string.replace("&", "§"));
			index += 1;
		}
		return newText;
	}

	public static Material parseMaterial(String string) {
		Material mat = Material.matchMaterial(string);
		if (mat == null) {
			if (Character.isDigit(string.charAt(0))) {
				mat = Material.getMaterial(Integer.parseInt(string));
				if (mat == null) {
					return mat;
				}
			} else {
				return mat;
			}
		}
		return mat;
	}
}
