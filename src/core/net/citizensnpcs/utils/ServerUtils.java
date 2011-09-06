package net.citizensnpcs.utils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

import net.citizensnpcs.Citizens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
			if(Citizens.getVersion().contains("devBuild"))
			{
				
				if ( !("devBuild-" + Citizens.getLatestBuildVersion()).equals(Citizens.getVersion()) ) {
					Messaging.send(player, null, ChatColor.YELLOW + "**ALERT** "
							+ ChatColor.GREEN
							+ "There is a new development version of Citizens available!");
					return;
				}
			} else {
				if ( !Citizens.getLatestVersion().equals(Citizens.getVersion()) ) {
					Messaging.send(player, null, ChatColor.YELLOW + "**ALERT** "
							+ ChatColor.GREEN
							+ "There is a new version of Citizens available!");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}