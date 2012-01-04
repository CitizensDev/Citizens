package net.citizensnpcs.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

import net.citizensnpcs.properties.properties.UtilityProperties;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import com.google.common.base.Joiner;

public class StringUtils {
	public static class Formatter {
		private final String format;

		public Formatter(String string) {
			this.format = string;
		}

		public Formatter add(String other) {
			return new Formatter(format + " " + other);
		}

		public Formatter bracket() {
			return new Formatter(StringUtils.bracketize(format));
		}

		public Formatter capital() {
			return new Formatter(StringUtils.capitalise(format));
		}

		public Formatter colour() {
			return new Formatter(StringUtils.colourise(format));
		}

		public Formatter list() {
			return new Formatter(StringUtils.listify(format));
		}

		public Formatter plural(int amount) {
			return new Formatter(StringUtils.pluralise(format, amount));
		}

		public Formatter reset() {
			return new Formatter("");
		}

		public Formatter slice(int start, int end) {
			return new Formatter(format.substring(start, end));
		}

		@Override
		public String toString() {
			return format;
		}

		public Formatter wrap() {
			return new Formatter(StringUtils.wrap(format));
		}

		public Formatter wrap(ChatColor end) {
			return new Formatter(StringUtils.wrap(format, end));
		}
	}

	private static final Pattern INTEGER = Pattern.compile("^[0-9]+$");

	public static String bracketize(String string) {
		return "[" + string + "]";
	}

	public static String bracketize(String string, boolean soft) {
		return soft ? '(' + string + ')' : bracketize(string);
	}

	public static String capitalise(String toCapitalise) {
		return toCapitalise.replaceFirst("" + toCapitalise.charAt(0), ""
				+ Character.toUpperCase(toCapitalise.charAt(0)));
	}

	public static Deque<String> colourise(Deque<String> text) {
		Deque<String> newText = new ArrayDeque<String>();
		for (String string : text) {
			newText.add(string.replace("&", "\u00A7"));
		}
		return newText;
	}

	public static String colourise(String string) {
		return string.replace("&", "\u00A7");
	}

	public static String format(Enum<?> toFormat) {
		return format(toFormat, true);
	}

	public static String format(Enum<?> toFormat, boolean capitalise) {
		if (toFormat == null)
			return "";
		String[] split = toFormat.name().toLowerCase().split("_");
		if (capitalise)
			split[0] = capitalise(split[0]);
		return Joiner.on(" ").join(split);
	}

	public static String format(Location location) {
		return join(
				location.getBlockX(),
				location.getBlockY(),
				location.getBlockZ(),
				bracketize(StringUtils.wrap("world: ")
						+ location.getWorld().getName(), true));
	}

	public static Formatter formatter() {
		return new Formatter("");
	}

	public static Formatter formatter(Enum<?> toFormat) {
		return formatter(format(toFormat));
	}

	public static Formatter formatter(Object string) {
		return new Formatter(string.toString());
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

	public static boolean isCommand(String message) {
		return message.charAt(0) == '/';
	}

	public static boolean isNumber(String search) {
		return INTEGER.matcher(search).matches();
	}

	public static String join(Object... parts) {
		return Joiner.on(" ").join(parts);
	}

	public static String listify(String string) {
		return "========[ " + string + " ]========";
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

	public static String parseColour(String s) {
		if (s.startsWith("&")) {
			String colour = "";
			colour = s.replace("&", "\u00A7").substring(0, 1);
			return colour;
		}
		return "\u00A7f";
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

	public static String pluralise(String string, int size) {
		return size > 1 ? string + (string.endsWith("s") ? "es" : "s") : string;
	}

	public static String stripColour(String toStrip) {
		return ChatColor.stripColor(toStrip);
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
	 * Makes a string yellow, with a trailing green colour.
	 * 
	 * @param string
	 * @return
	 */
	public static String wrap(Object string) {
		return ChatColor.YELLOW + string.toString() + ChatColor.GREEN;
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
}