package com.citizens.utils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.Citizens;

public class ServerUtils {

	/**
	 * Get a player object from the provided name
	 * 
	 * @param name
	 */
	public static Player matchPlayer(String name) {
		List<Player> players = Bukkit.getServer().matchPlayer(name);
		if (!players.isEmpty()) {
			if (players.get(0) != null) {
				return players.get(0);
			}
		}
		return null;
	}

	/**
	 * Check the Citizens thread on the Bukkit forums if there is a new version
	 * available
	 * 
	 * @param player
	 */
	public static void checkForUpdates(Player player) {
		try {
			URI baseURI = new URI("http://forums.bukkit.org/threads/7173/");
			HttpURLConnection con = (HttpURLConnection) baseURI.toURL()
					.openConnection();
			con.setInstanceFollowRedirects(false);
			if (con.getHeaderField("Location") == null) {
				Messaging
						.log("Couldn't connect to Citizens thread to check for updates.");
				return;
			}
			String url = new URI(con.getHeaderField("Location")).toString();
			// TODO: This doesn't give the new version number or work backwards
			// (1.0.9d will be a 'new' version compared to 1.0.9c)
			if (!url.contains(Citizens.getVersion().replace(".", "-"))) {
				Messaging.send(player, null, ChatColor.YELLOW + "**ALERT** "
						+ ChatColor.GREEN
						+ "There is a new version of Citizens available!");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}