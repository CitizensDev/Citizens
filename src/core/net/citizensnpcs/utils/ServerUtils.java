package net.citizensnpcs.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import net.citizensnpcs.Citizens;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerUtils {

	// Get a player object from the provided name
	public static Player matchPlayer(String name) {
		List<Player> players = Bukkit.getServer().matchPlayer(name);
		if (!players.isEmpty()) {
			if (players.get(0) != null) {
				return players.get(0);
			}
		}
		return null;
	}

	// Check the Citizens thread on the Bukkit forums if there is a new version
	// available, or use Citizens.getLatestBuildVersion() for devBuilds.
	public static void checkForUpdates(Player player) {
		try {
			if (Citizens.localVersion().contains("devBuild")) {
				if (!("devBuild-" + fetchLatestBuildVersion()).equals(Citizens
						.localVersion())) {
					Messaging
							.send(player,
									StringUtils.wrap("**ALERT** ")
											+ "A new development version of Citizens is available!");
					return;
				}
			} else {
				if (!fetchLatestVersion().equals(Citizens.localVersion())) {
					Messaging.send(player, StringUtils.wrap("**ALERT** ")
							+ "A new version of Citizens is available!");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
}