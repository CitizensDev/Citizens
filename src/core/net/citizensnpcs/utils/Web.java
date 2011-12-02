package net.citizensnpcs.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Web {
	private static Set<Player> told = Sets.newHashSet();
	private static String build = "";
	private static final Handler handler = new LogHandler();

	// Check the Citizens thread on the Bukkit forums if there is a new version
	// available, or use Citizens.getLatestBuildVersion() for devBuilds.
	public static void notifyUpdate(Player player) {
		if (told.contains(player))
			return;
		if (build.isEmpty()) {
			if (Citizens.localVersion().contains("devBuild")) {
				String temp = fetchLatestBuildVersion();
				if (!temp.equals(Citizens.localVersion())) {
					build = temp;
				}
			} else {
				String temp = fetchLatestVersion();
				if (!temp.equals(Citizens.localVersion())) {
					build = temp;
				}
			}
		}
		if (!build.isEmpty()) {
			player.sendMessage(StringUtils.wrap("**ALERT** ")
					+ "A new version of Citizens is available!"
					+ " The latest version is " + StringUtils.wrap(build) + ".");
		}
	}

	/**
	 * Fetches the latest development build version.
	 * 
	 * @return a String representation of the latest build version.
	 */
	public static String fetchLatestBuildVersion() {
		return "devBuild-"
				+ fetchVersion("http://www.citizensnpcs.net/dev/latestdev.php");
	}

	/**
	 * Fetches the latest version from the citizens website.
	 * 
	 * @return the latest available version
	 */
	public static String fetchLatestVersion() {
		return fetchVersion("http://www.citizensnpcs.net/dev/latest.php");
	}

	private static String fetchVersion(String source) {
		BufferedReader reader = null;
		try {
			URL url = new URL(source);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			if ((line = reader.readLine()) != null) {
				reader.close(); // may be dangerous to not do this properly, but
								// the stream needs to be closed...
				return line.trim();
			}
		} catch (Exception e) {
			Messaging
					.log("Could not connect to citizensnpcs.net to determine latest version.");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Messaging.log("Unable to close the URL stream.");
				}
			}
		}
		return Citizens.localVersion();
	}

	public static void initErrorReporting() {
		if (!Settings.getBoolean("ErrorReporting")) {
			return;
		}
		Bukkit.getLogger().addHandler(handler);
	}

	public static void disableErrorReporting() {
		if (!Settings.getBoolean("ErrorReporting")) {
			return;
		}
		Bukkit.getLogger().removeHandler(handler);
	}

	private static void report(String error) {
		try {
			Messaging
					.log("An error has occurred, please wait while it is sent to the developers...");
			// Construct data
			String data = URLEncoder.encode("Exception", "UTF-8") + "="
					+ URLEncoder.encode(error, "UTF-8");
			data += "&" + URLEncoder.encode("Version", "UTF-8") + "="
					+ URLEncoder.encode(Citizens.localVersion(), "UTF-8");
			data += "&"
					+ URLEncoder.encode("Ident", "UTF-8")
					+ "="
					+ URLEncoder.encode(
							Settings.getString("ErrorReportingIdent"), "UTF-8");
			// Send data
			URL url = new URL("http://errorreport.citizensnpcs.net");
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String line;
			while ((line = rd.readLine()) != null) {
				Messaging.log(line);
			}

			wr.close();
			rd.close();
		} catch (Exception e) {
			Messaging
					.log("An error occurred whilst sending your error report.");
		} finally {
			Messaging
					.log("Below is the stacktrace that has been transmitted to the developers, no other information has been included other than Citizens version information.");
		}
	}

	private static String stackToString(Throwable e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "Invalid stacktrace";
		}
	}

	private static class LogHandler extends ConsoleHandler {
		@Override
		public void publish(LogRecord record) {
			if (record.getMessage() == null || record.getThrown() == null
					|| record.getLevel() != Level.SEVERE
					|| !record.getMessage().contains("Citizens"))
				return;
			for (String pattern : illegalStacks.keySet()) {
				if (record.getMessage().matches(pattern)) {
					if (!illegalStacks.get(pattern).isEmpty())
						Messaging.log(illegalStacks.get(pattern));
					return;
				}
			}
			report(stackToString(record.getThrown()));
		}
	}

	private static Map<String, String> illegalStacks = Maps.newHashMap();
	static {
		illegalStacks.put("org.yaml.snakeyaml",
				"Something went wrong with config.");
		illegalStacks
				.put("NoSuchFieldError",
						"A bukkit update has changed obfuscation, wait for a Citizens update.");
		illegalStacks
				.put("OutOfMemoryError",
						"Uh oh - your server is out of memory - can you report this on the forums?");
		illegalStacks
				.put("NoClassDefFoundError",
						"A required class is missing - usually, the type JARs are incompatible with the core Citizens JAR or there's an economy/permissions class missing.");
	}
}
