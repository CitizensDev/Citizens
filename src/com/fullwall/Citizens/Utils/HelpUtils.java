package com.fullwall.Citizens.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpUtils {

	/**
	 * Sends the help page for /citizens help
	 * 
	 * @param sender
	 */
	public static void sendHelpPage(CommandSender sender, int page) {
		switch (page) {
		case 1:
			header(sender, "General", 1, 2);
			sender.sendMessage(ChatColor.GREEN + "  []"
					+ StringUtils.wrap(" - required") + "  ()"
					+ StringUtils.wrap(" - optional"));
			format(sender, "toggle", "[type]", "toggle an NPC type");
			format(sender, "toggle", "[all] [on/off]",
					"toggle all types for an NPC");
			format(sender, "basic", "help [page]", "basic NPC help pages");
			format(sender, "bandit", "help", "bandit NPC help page");
			format(sender, "blacksmith", "help", "blacksmith NPC help page");
			format(sender, "guard", "help", "guard NPC help page");
			format(sender, "healer", "help", "healer NPC help page");
			footer(sender);
			break;
		case 2:
			header(sender, "General", 2, 2);
			format(sender, "quester", "help", "quester NPC help page");
			format(sender, "trader", "help [page]", "trader NPC help page");
			format(sender, "wizard", "help", "wizard NPC help page");
			footer(sender);
			break;
		default:
			sender.sendMessage(MessageUtils.getMaxPagesMessage(page, 2));
			break;
		}
	}

	/**
	 * Sends the help page for the basic npc type
	 * 
	 * @param sender
	 * @param page
	 */
	public static void sendBasicHelpPage(CommandSender sender, int page) {
		switch (page) {
		case 1:
			header(sender, "Basic NPC", 1, 3);
			format(sender, "npc", "create [name]", "create an NPC");
			format(sender, "npc", "set [text]", "set the text of an NPC");
			format(sender, "npc", "add [text]", "add text to an NPC");
			format(sender, "npc", "reset", "reset the text of an NPC");
			format(sender, "npc", "name [name]", "set the new name of an NPC");
			format(sender, "npc", "remove (all)", "remove and despawn NPC(s)");
			format(sender, "npc", "item [itemID|item-name]",
					"set the item that an NPC holds");
			format(sender, "npc", "[slot] [itemID|name]",
					"set the armor slot of an NPC");
			format(sender, "npc", "move", "move an NPC to your location");
			break;
		case 2:
			header(sender, "Basic NPC", 2, 3);
			format(sender, "npc", "tp", "teleport to the location of an NPC");
			format(sender, "npc", "copy",
					"make a clone of an NPC at your location");
			format(sender, "npc", "id", "get the ID of an NPC");
			format(sender, "npc", "select [ID]",
					"select an NPC with the given ID");
			format(sender, "npc", "owner", "get the owner of an NPC");
			format(sender, "npc", "setowner [name]", "set the owner of an NPC");
			format(sender, "npc", "talkwhenclose [true|false]",
					"make an NPC talk to players");
			format(sender, "npc", "lookatplayers [true|false]",
					"make an NPC look at players");
			footer(sender);
			break;
		case 3:
			header(sender, "Basic NPC", 3, 3);
			format(sender, "npc", "list (name) (page)", "show a list of NPCs");

			footer(sender);
			break;
		default:
			sender.sendMessage(MessageUtils.getMaxPagesMessage(page, 2));
			break;
		}
	}

	/**
	 * Sends the help page for the trader npc type
	 * 
	 * @param sender
	 */
	public static void sendTraderHelp(CommandSender sender) {
		header(sender, "Trader", 1, 1);
		format(sender, "trader", "list [buy|sell] (page)",
				"list a trader's buy/sell list");
		format(sender, "trader",
				"[buy|sell] [itemID(:amount:data)] [itemID(:amount:data)]",
				"start an NPC stocking an item");
		format(sender, "trader", "[buy|sell] remove [itemID]",
				"stop the item from being stocked");
		format(sender, "trader", "balance [give|take]",
				"set a trader's balance if using iConomy");
		format(sender, "trader", "unlimited [true|false]",
				"set whether a trader has unlimited stock");
		format(sender, "trader", "money", "check how much money a trader has");
		footer(sender);
	}

	/**
	 * Sends the help page for the healer npc type
	 * 
	 * @param sender
	 */
	public static void sendHealerHelp(CommandSender sender) {
		header(sender, "Healer", 1, 1);
		format(sender, "healer", "status",
				"view the health and level of a healer");
		format(sender, "healer", "level-up (levels)", "level-up a healer");
		footer(sender);
	}

	/**
	 * Sends the help page for the wizard npc type
	 * 
	 * @param sender
	 */
	public static void sendWizardHelp(CommandSender sender) {
		header(sender, "Wizard", 1, 1);
		format(sender, "wizard", "locations",
				"view the tp locations of a wizard");
		format(sender, "wizard", "addloc [name]",
				"add a tp location to the wizard");
		format(sender, "wizard", "removeloc [id]", "remove the tp location");
		format(sender, "wizard", "mode [mode]", "change the mode of a wizard");
		format(sender, "wizard", "status", "display the mode/mana of a wizard");
		footer(sender);
	}

	/**
	 * Sends the help page for the quester npc type
	 * 
	 * @param sender
	 */
	public static void sendQuesterHelp(CommandSender sender) {
		header(sender, "Quester", 1, 1);
		format(sender, "quester", "assign [quest]", "assign a quest to an NPC");
		footer(sender);
	}

	/**
	 * Sends the help page for the blacksmith npc type
	 * 
	 * @param sender
	 */
	public static void sendBlacksmithHelp(CommandSender sender) {
		header(sender, "Blacksmith", 1, 1);
		format(sender, "blacksmith", "repair [type]", "repair your armor");
		format(sender, "blacksmith", "list", "list the available armor names");
		format(sender, "blacksmith", "uses",
				"show how many uses your item has left");
		footer(sender);
	}

	/**
	 * Sends the help page for the bandit npc type
	 * 
	 * @param sender
	 */
	public static void sendBanditHelp(CommandSender sender) {
		header(sender, "Bandit", 1, 1);
		format(sender, "bandit", "stealable [add|remove] [id]",
				"specify stealable items");
		footer(sender);
	}

	/**
	 * Sends the help page for the guard npc type
	 * 
	 * @param sender
	 */
	public static void sendGuardHelp(CommandSender sender) {
		header(sender, "Guard", 1, 1);
		format(sender, "guard", "[type]",
				"toggle the type of guard that an NPC is");
		format(sender, "guard", "blacklist (mob)",
				"add mob to a guard's blacklist");
		format(sender, "guard", "whitelist (player)",
				"add player to a guard's whitelist");
		format(sender, "guard", "radius [amount]",
				"set the radius of a bouncer's zone");
		footer(sender);
	}

	/**
	 * Prints the header for the help menus
	 * 
	 * @param sender
	 * @param npcType
	 * @param page
	 * @param maxPages
	 */
	private static void header(CommandSender sender, String npcType, int page,
			int maxPages) {
		sender.sendMessage(ChatColor.GREEN + StringUtils.wrap("==========[ ")
				+ "Citizens " + npcType + " Help" + ChatColor.WHITE + " <"
				+ page + "/" + maxPages + ">"
				+ StringUtils.wrap(" ]=========="));
	}

	/**
	 * Formats commands to fit a uniform style
	 * 
	 * @param sender
	 * @param command
	 * @param desc
	 */
	private static void format(CommandSender sender, String command,
			String args, String desc) {
		String message = "";
		if (args.isEmpty()) {
			message = StringUtils.wrap("/") + command + StringUtils.wrap(" - ")
					+ desc;
		} else {
			message = StringUtils.wrap("/") + command + ChatColor.RED + " "
					+ args + StringUtils.wrap(" - ") + desc;
		}
		sender.sendMessage(message);
	}

	/**
	 * Prints the footer for the help menus
	 * 
	 * @param sender
	 */
	private static void footer(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GRAY
				+ "=====[ fullwall, NeonMaster, TheMPC, aPunch ]=====");
	}
}