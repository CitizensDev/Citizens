package com.Citizens.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpUtils {

	/**
	 * Sends the help page for /citizens help
	 * 
	 * @param player
	 * @param page
	 */
	public static void sendHelpPage(Player player, int page) {
		switch (page) {
		case 1:
			header(player, "General", 1, 2);
			player.sendMessage(ChatColor.GREEN + "  []"
					+ StringUtils.wrap(" - required") + "  ()"
					+ StringUtils.wrap(" - optional"));
			format(player, "citizens", "info", "display Citizens information");
			format(player, "citizens", "reload", "toggle all types for an NPC");
			format(player, "toggle", "[type]", "toggle an NPC type");
			format(player, "toggle", "all [on/off]",
					"toggle all types for an NPC");
			format(player, "basic", "help [page]", "basic NPC help pages");
			format(player, "blacksmith", "help", "blacksmith NPC help page");
			format(player, "guard", "help", "guard NPC help page");
			footer(player);
			break;
		case 2:
			header(player, "General", 2, 2);
			format(player, "healer", "help", "healer NPC help page");
			format(player, "quester", "help", "quester NPC help page");
			format(player, "trader", "help", "trader NPC help page");
			format(player, "wizard", "help", "wizard NPC help page");
			footer(player);
			break;
		default:
			player.sendMessage(MessageUtils.getMaxPagesMessage(page, 2));
			break;
		}
	}

	/**
	 * Sends the help page for the basic npc type
	 * 
	 * @param player
	 * @param page
	 */
	public static void sendBasicHelpPage(Player player, int page) {
		switch (page) {
		case 1:
			header(player, "Basic NPC", 1, 3);
			format(player, "npc", "create [name]", "create an NPC");
			format(player, "npc", "set [text]", "set the text of an NPC");
			format(player, "npc", "add [text]", "add text to an NPC");
			format(player, "npc", "reset", "reset the text of an NPC");
			format(player, "npc", "rename [name]", "rename an NPC");
			format(player, "npc", "remove (all)", "remove all NPC(s)");
			format(player, "npc", "item [item]",
					"set the item that an NPC holds");
			format(player, "npc", "armor [slot] [item]",
					"set the armor slot of an NPC");
			format(player, "npc", "move", "move an NPC to your location");
			break;
		case 2:
			header(player, "Basic NPC", 2, 3);
			format(player, "npc", "moveTo [x y z]", "move an NPC to a location");
			format(player, "npc", "tp", "teleport to the location of an NPC");
			format(player, "npc", "copy",
					"make a clone of an NPC at your location");
			format(player, "npc", "id", "get the ID of an NPC");
			format(player, "npc", "select [ID]",
					"select an NPC with the given ID");
			format(player, "npc", "owner", "get the owner of an NPC");
			format(player, "npc", "setowner [name]", "set the owner of an NPC");
			format(player, "npc", "talkwhenclose [true|false]",
					"make an NPC talk to players");
			footer(player);
			break;
		case 3:
			header(player, "Basic NPC", 3, 3);
			format(player, "npc", "lookatplayers [true|false]",
					"make an NPC look at players");
			format(player, "npc", "list (name) (page)", "show a list of NPCs");
			footer(player);
			break;
		default:
			player.sendMessage(MessageUtils.getMaxPagesMessage(page, 3));
			break;
		}
	}

	/**
	 * Sends the help page for the trader npc type
	 * 
	 * @param player
	 */
	public static void sendTraderHelp(Player player) {
		header(player, "Trader", 1, 1);
		format(player, "trader", "list [buy|sell] (page)",
				"list a trader's buy/sell list");
		format(player, "trader",
				"[buy|sell] [itemID(:amount:data)] [itemID(:amount:data)]",
				"start an NPC stocking an item");
		format(player, "trader", "[buy|sell] remove [itemID]",
				"stop the item from being stocked");
		format(player, "trader", "balance [give|take]",
				"set a trader's balance if using iConomy");
		format(player, "trader", "unlimited [true|false]",
				"set whether a trader has unlimited stock");
		format(player, "trader", "money", "check how much money a trader has");
		footer(player);
	}

	/**
	 * Sends the help page for the healer npc type
	 * 
	 * @param player
	 */
	public static void sendHealerHelp(Player player) {
		header(player, "Healer", 1, 1);
		format(player, "healer", "status",
				"view the health and level of a healer");
		format(player, "healer", "level-up (levels)", "level-up a healer");
		footer(player);
	}

	/**
	 * Sends the help page for the wizard npc type
	 * 
	 * @param player
	 */
	public static void sendWizardHelp(Player player) {
		header(player, "Wizard", 1, 1);
		format(player, "wizard", "locations",
				"view the tp locations of a wizard");
		format(player, "wizard", "addloc [name]",
				"add a tp location to the wizard");
		format(player, "wizard", "removeloc [id]", "remove the tp location");
		format(player, "wizard", "mode [mode]", "change the mode of a wizard");
		format(player, "wizard", "status", "display the mode/mana of a wizard");
		footer(player);
	}

	/**
	 * Sends the help page for the quester npc type
	 * 
	 * @param player
	 */
	public static void sendQuesterHelp(Player player) {
		header(player, "Quester", 1, 1);
		format(player, "quester", "assign [quest]", "assign a quest to an NPC");
		format(player, "quester", "remove [quest]",
				"remove a quest from an NPC");
		footer(player);
	}

	/**
	 * Sends the help page for the blacksmith npc type
	 * 
	 * @param player
	 */
	public static void sendBlacksmithHelp(Player player) {
		header(player, "Blacksmith", 1, 1);
		format(player, "blacksmith", "uses",
				"show how many uses your item has left");
		footer(player);
	}

	/**
	 * Sends the help page for the guard npc type
	 * 
	 * @param player
	 */
	public static void sendGuardHelp(Player player) {
		header(player, "Guard", 1, 1);
		format(player, "guard", "[type]",
				"toggle the type of guard that an NPC is");
		format(player, "guard", "blacklist (mob)",
				"add mob to a guard's blacklist");
		format(player, "guard", "whitelist (player)",
				"add player to a guard's whitelist");
		format(player, "guard", "radius [amount]",
				"set the radius of a bouncer's zone");
		footer(player);
	}

	/**
	 * Prints the header for the help menus
	 * 
	 * @param player
	 * @param npcType
	 * @param page
	 * @param maxPages
	 */
	private static void header(Player player, String npcType, int page,
			int maxPages) {
		player.sendMessage(ChatColor.GREEN + StringUtils.wrap("==========[ ")
				+ "Citizens " + npcType + " Help" + ChatColor.WHITE + " <"
				+ page + "/" + maxPages + ">"
				+ StringUtils.wrap(" ]=========="));
	}

	/**
	 * Formats commands to fit a uniform style
	 * 
	 * @param player
	 * @param command
	 * @param desc
	 */
	private static void format(Player player, String command, String args,
			String desc) {
		String message = "";
		if (args.isEmpty()) {
			message = StringUtils.wrap("/") + command + StringUtils.wrap(" - ")
					+ desc;
		} else {
			message = StringUtils.wrap("/") + command + ChatColor.RED + " "
					+ args + StringUtils.wrap(" - ") + desc;
		}
		player.sendMessage(message);
	}

	/**
	 * Prints the footer for the help menus
	 * 
	 * @param player
	 */
	private static void footer(Player player) {
		player.sendMessage(ChatColor.DARK_GRAY
				+ "=====[ Coded by: fullwall and aPunch ]=====");
	}
}