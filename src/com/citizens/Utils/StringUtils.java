package com.citizens.Utils;

import java.util.ArrayDeque;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.citizens.Properties.Properties.UtilityProperties;

public class StringUtils {
	public static String pluralise(String string, int size) {
		return size > 0 ? string + "s" : string;
	}

	public static String parseColour(String s) {
		if (s.startsWith("&")) {
			String colour = "";
			colour = s.replace("&", "\u00A7").substring(0, 1);
			return colour;
		}
		return "\u00A7f";
	}

	public static String stripColour(String toStrip) {
		return ChatColor.stripColor(toStrip);
	}

	public static String bracketize(String string) {
		return "[" + string + "]";
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
			Material newMat = Material.getMaterial(material);
			if (newMat != null) {
				if (!UtilityProperties.getItemOverride(newMat.getId())
						.isEmpty()) {
					mat = parseMaterial(UtilityProperties
							.getItemOverride(newMat.getId()));
				} else if (isNumber(material)) {
					mat = Material.getMaterial(Integer.parseInt(material));
				}
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
	public static String wrap(Object string) {
		return ChatColor.YELLOW + string.toString() + ChatColor.GREEN;
	}

	/**
	 * Makes a double a yellow string with a trailing green colour.
	 * 
	 * @param string
	 * @return
	 */
	public static String wrap(double string) {
		if (string % 1 == 0) {
			return ChatColor.YELLOW + "" + (int) string + ChatColor.GREEN;
		} else {
			return ChatColor.YELLOW + "" + string + ChatColor.GREEN;
		}
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

	public static int getLevenshteinDistance(String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		/*
		 * The difference between this impl. and the previous is that, rather
		 * than creating and retaining a matrix of size s.length()+1 by
		 * t.length()+1, we maintain two single-dimensional arrays of length
		 * s.length()+1. The first, d, is the 'current working' distance array
		 * that maintains the newest distance cost counts as we iterate through
		 * the characters of String s. Each time we increment the index of
		 * String t we are comparing, d is copied to p, the second int[]. Doing
		 * so allows us to retain the previous cost counts as required by the
		 * algorithm (taking the minimum of the cost count to the left, up one,
		 * and diagonally up and to the left of the current cost count being
		 * calculated). (Note that the arrays aren't really copied anymore, just
		 * switched...this is clearly much better than cloning an array or doing
		 * a System.arraycopy() each time through the outer loop.)
		 * 
		 * Effectively, the difference between the two implementations is this
		 * one does not cause an out of memory condition when calculating the LD
		 * over two very large strings.
		 */

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left
				// and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
						+ cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}
}