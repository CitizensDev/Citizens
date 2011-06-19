package com.fullwall.Citizens.Utils;

import java.util.ArrayDeque;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.fullwall.Citizens.Properties.Properties.UtilityProperties;

public class StringUtils {
	public static String parseColour(String s) {
		if (s.startsWith("&")) {
			String colour = "";
			colour = s.replace("&", "�").substring(0, 1);
			return colour;
		}
		return "�f";
	}

	public static String stripColour(String toStrip) {
		return ChatColor.stripColor(toStrip);
	}

	public static String capitalise(String toCapitalise) {
		return toCapitalise.replaceFirst("" + toCapitalise.charAt(0), ""
				+ Character.toUpperCase(toCapitalise.charAt(0)));
	}

	public static ArrayDeque<String> colourise(ArrayDeque<String> text) {
		ArrayDeque<String> newText = new ArrayDeque<String>();
		for (String string : text) {
			newText.add(string.replace("&", "�"));
		}
		return newText;
	}

	public static String colourise(String string) {
		return string.replace("&", "�");
	}

	public static Material parseMaterial(String material) {
		Material mat = Material.matchMaterial(material);
		if (mat == null) {
			if (!UtilityProperties.getItemOverride(
					Material.getMaterial(material).getId()).isEmpty()) {
				mat = parseMaterial(UtilityProperties.getItemOverride(Material
						.getMaterial(material).getId()));
			} else if (isNumber(material)) {
				mat = Material.getMaterial(Integer.parseInt(material));
			}
		}
		return mat;
	}

	public static boolean isNumber(String material) {
		return material.matches("^[0-9]+$");
	}

	/**
	 * Makes a string yellow, with a trailing green colour.
	 * 
	 * @param string
	 * @return
	 */
	public static String wrap(String string) {
		return ChatColor.YELLOW + string + ChatColor.GREEN;
	}

	/**
	 * Makes a double a yellow string with a trailing green colour.
	 * 
	 * @param string
	 * @return
	 */
	public static String wrap(double string) {
		if (string % 1 == 0)
			return ChatColor.YELLOW + "" + (int) string + ChatColor.GREEN;
		else
			return ChatColor.YELLOW + "" + string + ChatColor.GREEN;
	}

	/**
	 * Makes a string yellow, with a given colour trailing.
	 * 
	 * @param string
	 * @param trailing
	 * @return
	 */
	public static String wrap(String string, ChatColor trailing) {
		return ChatColor.YELLOW + string + trailing;
	}

	/**
	 * Makes a double a yellow string, with a given colour trailing.
	 * 
	 * @param string
	 * @param trailing
	 * @return
	 */
	public static String wrap(double string, ChatColor trailing) {
		return ChatColor.YELLOW + "" + (int) string + trailing;
	}

	/**
	 * Makes a string yellow, with a trailing green colour. Based on '{' and '}'
	 * in the string.
	 * 
	 * @param string
	 * @return
	 */
	public static String wrapFull(String string) {
		return ChatColor.GREEN
				+ string.replace("{", "" + ChatColor.YELLOW).replace("}",
						"" + ChatColor.GREEN);
	}

	/**
	 * Makes a string yellow, with a specified tailing colour. Based on '{' and
	 * '}' in the string.
	 * 
	 * @param string
	 * @return
	 */
	public static String wrapFull(String string, ChatColor colour) {
		return colour
				+ string.replace("{", "" + ChatColor.YELLOW).replace("}",
						"" + colour);
	}

	/**
	 * Parse an integer from a string value
	 * 
	 * @param passed
	 * @return
	 */
	public static int parse(String passed) {
		return Integer.parseInt(passed);
	}
}