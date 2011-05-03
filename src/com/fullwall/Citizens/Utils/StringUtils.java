package com.fullwall.Citizens.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;

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

	public static ArrayList<String> colourise(ArrayList<String> text) {
		int index = 0;
		ArrayList<String> newText = new ArrayList<String>();
		for (String string : text) {
			newText.add(index, string.replace("&", "§"));
			index += 1;
		}
		return newText;
	}

	public static Material parseMaterial(String material) {
		Material mat = Material.matchMaterial(material);
		if (mat == null) {
			if (!PropertyPool.itemlookups.getString(material).isEmpty()) {
				mat = parseMaterial(PropertyPool.itemlookups
						.getString(material));
			} else if (isNumber(material)) {
				mat = Material.getMaterial(Integer.parseInt(material));
			} else {
				return mat;
			}
		}
		return mat;
	}

	private static boolean isNumber(String material) {
		try {
			Integer.parseInt(material);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static String yellowify(String string) {
		return ChatColor.YELLOW + string + ChatColor.GREEN;
	}

	public static String yellowify(double string) {
		return ChatColor.YELLOW + "" + string + ChatColor.GREEN;
	}

	public static String yellowify(String string, ChatColor temp) {
		return ChatColor.YELLOW + string + temp;
	}

	public static String yellowify(double string, ChatColor temp) {
		return ChatColor.YELLOW + "" + string + temp;
	}
}
