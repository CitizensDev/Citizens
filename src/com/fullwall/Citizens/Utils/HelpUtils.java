package com.fullwall.Citizens.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Help pages for Citizens
 */
public class HelpUtils {
	/**
	 * Sends the help page for /citizens help.
	 * 
	 * @param sender
	 */
	public static void sendHelp(CommandSender sender) {
	}

	/**
	 * Sends the help page for the basic npc type.
	 * 
	 * @param sender
	 * @param page
	 */
	public static void sendBasicHelpPage(CommandSender sender, int page) {
		switch (page) {
		case 1:
			header(sender, "Basic", 1, 4);
			formatCommand(sender, "create", "create an NPC");
			break;
		case 2:
			header(sender, "Basic", 2, 4);
			break;
		case 3:
			header(sender, "Basic", 3, 4);
			break;
		case 4:
			header(sender, "Basic", 4, 4);
			break;
		default:
			maxPages(sender, page, 4);
			break;
		}
	}

	/**
	 * Sends the help page for the trader npc type.
	 * 
	 * @param sender
	 * @param page
	 */
	public static void sendTraderHelpPage(CommandSender sender, int page) {
		switch (page) {
		case 1:
			header(sender, "Trader", page, 2);
			break;
		case 2:
			header(sender, "Trader", page, 2);
			break;
		default:
			maxPages(sender, page, 2);
			break;
		}
	}

	/**
	 * Sends the help page for the healer npc type.
	 * 
	 * @param sender
	 * @param page
	 */
	public static void sendHealerHelp(CommandSender sender) {
		header(sender, "Healer", 1, 1);
	}

	/**
	 * Prints the header for the Citizens help menus
	 * 
	 * @param sender
	 * @param npcType
	 * @param page
	 * @param maxPages
	 */
	private static void header(CommandSender sender, String npcType, int page,
			int maxPages) {
		sender.sendMessage(ChatColor.GREEN
				+ StringUtils.yellowify("==========[ ") + "Citizens " + npcType
				+ " Help" + ChatColor.WHITE + " <" + page + "/" + maxPages
				+ ">" + StringUtils.yellowify(" ]=========="));
	}

	/**
	 * Sends the max-pages message
	 * 
	 * @param sender
	 * @param page
	 * @param maxPages
	 */
	private static void maxPages(CommandSender sender, int page, int maxPages) {
		sender.sendMessage(ChatColor.GRAY + "The total number of pages is "
				+ maxPages + ", page: " + page + " is not available.");
	}

	/**
	 * Formats commands to fit a uniform style
	 * 
	 * @param sender
	 * @param command
	 * @param desc
	 */
	private static void formatCommand(CommandSender sender, String command,
			String desc) {
		sender.sendMessage(StringUtils.yellowify("/") + command
				+ StringUtils.yellowify(" - ") + desc);
	}
}
