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
