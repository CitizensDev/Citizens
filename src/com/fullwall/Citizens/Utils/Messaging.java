package com.fullwall.Citizens.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

public class Messaging {
	private static Logger log = Logger.getLogger("Minecraft");
	private final static boolean debug = false;

	public static void send(Player player, String message) {
		message = StringUtils.colourise(message);
		message = message.replace("<h>", "" + player.getHealth());
		message = message.replace("<name>", player.getName());
		message = message.replace("<world>", player.getWorld().getName());
		player.sendMessage(message);
	}

	public static void log(String message) {
		log(message, Level.INFO);
	}

	public static void log(String message, Level level) {
		log.log(level, "[Citizens]: " + message);
	}

	public static void log(int message) {
		log("" + message);
	}

	public static void debug(String message) {
		if (debug)
			log(message);
	}
}
