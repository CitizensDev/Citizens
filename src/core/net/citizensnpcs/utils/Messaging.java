package net.citizensnpcs.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Splitter;

public class Messaging {
	private static final Logger log = Logger.getLogger("Minecraft");
	private final static String[] colours = { "black", "dblue", "dgreen",
			"dteal", "dred", "purple", "gold", "gray", "dgray", "blue",
			"bgreen", "teal", "red", "pink", "yellow", "white" };

	public static void send(CommandSender sender, String message) {
		send(sender, null, message);
	}

	public static void send(CommandSender sender, HumanNPC npc, String messages) {
		for (String message : Splitter.on("<br>").omitEmptyStrings()
				.split(messages)) {
			if (sender instanceof Player) {
				Player player = ((Player) sender);
				message = message.replace("<h>", "" + player.getHealth());
				message = message.replace("<name>", player.getName());
				message = message.replace("<world>", player.getWorld()
						.getName());
			}
			message = colourise(StringUtils.colourise(message));
			if (npc != null) {
				message = message.replace("<npc>", npc.getStrippedName());
				message = message.replace("<npcid>", "" + npc.getUID());
			}
		}
	}

	public static void log(Object... messages) {
		StringBuilder builder = new StringBuilder();
		for (Object string : messages) {
			builder.append(string.toString() + " ");
		}
		log(builder.toString(), Level.INFO);
	}

	public static void log(Object message, Level level) {
		log.log(level, "[Citizens] " + message);
	}

	public static void log(Object message) {
		log(message, Level.INFO);
	}

	public static void debug(Object message) {
		if (SettingsManager.getBoolean("DebugMode")) {
			log(message);
		}
	}

	public static void debug(Object... messages) {
		if (SettingsManager.getBoolean("DebugMode")) {
			log(messages);
		}
	}

	public static void sendError(Player player, String error) {
		send(player, null, ChatColor.RED + error);
	}

	public static void sendError(CommandSender sender, String error) {
		send(sender, null, ChatColor.RED + error);
	}

	public static void sendUncertain(String name, String message) {
		Player player = Bukkit.getServer().getPlayer(name);
		if (player != null) {
			send(player, null, message);
		}
	}

	private static String colourise(String message) {
		String format = "<%s>";
		byte index = 0;
		for (String colour : colours) {
			message = message.replaceAll(String.format(format, colour), ""
					+ ChatColor.getByCode(index));
			++index;
		}
		for (int colour = 0; colour <= 16; ++colour) {
			message = message.replaceAll(String.format(format, colour), ""
					+ ChatColor.getByCode(colour));
		}
		message = message.replaceAll("<g>", "" + ChatColor.GREEN);
		message = message.replaceAll("<y>", "" + ChatColor.YELLOW);
		return message;
	}
}