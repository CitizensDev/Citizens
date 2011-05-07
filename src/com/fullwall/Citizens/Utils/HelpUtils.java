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
		sender.sendMessage(ChatColor.GREEN
				+ StringUtils.yellowify("==========[ ") + "Citizens Help"
				+ StringUtils.yellowify(" ]=========="));
		sender.sendMessage(ChatColor.GREEN + "  []"
				+ StringUtils.yellowify(" - required") + "  ()"
				+ StringUtils.yellowify(" - optional"));
		sender.sendMessage(ChatColor.YELLOW
				+ "=================================");
		formatCommand(sender, "basic", "help [page]", "basic NPC help pages");
		formatCommand(sender, "trader", "help [page]", "trader NPC help page");
		formatCommand(sender, "healer", "help", "healer NPC help page");
		formatCommand(sender, "npc", "create [name]", "create a basic NPC");
		formatCommand(sender, "toggle", "[type]", "toggle an NPC type");
		formatCommand(sender, "toggle", "[all] [on/off]",
				"toggle all types for an NPC");
		footer(sender);
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
			header(sender, "Basic", 1, 2);
			formatCommand(sender, "npc", "create [name]", "create an NPC");
			formatCommand(sender, "npc", "set [text]", "set the text of an NPC");
			formatCommand(sender, "npc", "add [text]", "add text to an NPC");
			formatCommand(sender, "npc", "reset", "reset the text of an NPC");
			formatCommand(sender, "npc", "name [name]",
					"set the new name of an NPC");
			formatCommand(sender, "npc", "remove (all)",
					"remove and despawn NPC(s)");
			formatCommand(sender, "npc", "item [itemID|item-name]",
					"set the item that an NPC holds");
			formatCommand(sender, "npc", "[slot] [itemID|name]",
					"set the armor slot of an NPC");
			formatCommand(sender, "npc", "move", "move an NPC to your location");
			break;
		case 2:
			header(sender, "Basic", 2, 2);
			formatCommand(sender, "npc", "tp",
					"teleport to the location of an NPC");
			formatCommand(sender, "npc", "copy",
					"make a clone of an NPC at your location");
			formatCommand(sender, "npc", "getid", "get the ID of an NPC");
			formatCommand(sender, "npc", "select [ID]",
					"select an NPC with the given ID");
			formatCommand(sender, "npc", "getowner", "get the owner of an NPC");
			formatCommand(sender, "npc", "setowner [name]",
					"set the owner of an NPC");
			formatCommand(sender, "npc", "talkwhenclose [true|false]",
					"make an NPC talk to players");
			formatCommand(sender, "npc", "lookwhenclose [true|false]",
					"make an NPC look at players");
			footer(sender);
			break;
		default:
			maxPagesMessage(sender, page, 2);
			break;
		}
	}

	/**
	 * Sends the help page for the trader npc type.
	 * 
	 * @param sender
	 * @param page
	 */
	public static void sendTraderHelp(CommandSender sender) {
		header(sender, "Trader", 1, 1);
		formatCommand(sender, "trader", "list [buy|sell] (page)",
				"list a trader's buy/sell list");
		formatCommand(sender, "trader",
				"[buy|sell] [itemID(:amount:data)] [itemID(:amount:data)]",
				"start an NPC stocking an item");
		formatCommand(sender, "trader", "[buy|sell] remove [itemID]",
				"stop the item from being stocked");
		formatCommand(sender, "trader", "balance [give|take]",
				"set a trader's balance if using iConomy");
		formatCommand(sender, "trader", "unlimited [true|false]",
				"set whether a trader has unlimited stock");
		formatCommand(sender, "trader", "money",
				"checks how much money a trader has");
		footer(sender);
	}

	/**
	 * Sends the help page for the healer npc type.
	 * 
	 * @param sender
	 * @param page
	 */
	public static void sendHealerHelp(CommandSender sender) {
		header(sender, "Healer", 1, 1);
		formatCommand(sender, "healer", "help", "view this menu");
		formatCommand(sender, "healer", "status",
				"view the health and level of a healer");
		formatCommand(sender, "healer", "level-up (levels)",
				"level-up a healer");
		footer(sender);
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
	private static void maxPagesMessage(CommandSender sender, int page,
			int maxPages) {
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
			String args, String desc) {
		String message = "";
		if (args.isEmpty()) {
			message = StringUtils.yellowify("/") + command
					+ StringUtils.yellowify(" - ") + desc;
		} else {
			message = StringUtils.yellowify("/") + command + ChatColor.RED
					+ " " + args + StringUtils.yellowify(" - ") + desc;
		}
		sender.sendMessage(message);
	}

	private static void footer(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GRAY
				+ "=====[ fullwall, NeonMaster, TheMPC, aPunch ]=====");
	}
}
