package net.citizensnpcs.utils;

import net.citizensnpcs.npctypes.CitizensNPC;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpUtils {

	/**
	 * Sends the help page for /citizens help
	 * 
	 * @param sender
	 * @param page
	 */
	public static void sendHelpPage(CommandSender sender, int page) {
		switch (page) {
		case 1:
			header(sender, "General", 1, 2);
			sender.sendMessage(ChatColor.GREEN + "  []"
					+ StringUtils.wrap(" - required") + "  ()"
					+ StringUtils.wrap(" - optional"));
			format(sender, "citizens", "", "display Citizens information");
			format(sender, "citizens", "reload", "reload Citizens files");
			format(sender, "citizens", "save", "force a save of Citizens files");
			format(sender, "citizens", "debug", "toggle Citizens debug mode");
			format(sender, "citizens", "clean", "remove ghost NPCs");
			format(sender, "toggle", "list (page)",
					"view available toggleable types");
			format(sender, "toggle", "[type]", "toggle an NPC type");
			format(sender, "toggle", "all [on/off]",
					"toggle all types for an NPC");
			break;
		case 2:
			header(sender, "General", 2, 2);
			format(sender, "npc", "help [page]", "basic NPC help pages");
			format(sender, "[type]", "help",
					"view the help page for an NPC type");
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
			header(sender, "General", 1, 3);
			format(sender, "npc", "", "view an NPC's information");
			format(sender, "npc", "create [name]", "create an NPC");
			format(sender, "npc", "set [text]", "set the text of an NPC");
			format(sender, "npc", "add [text]", "add text to an NPC");
			format(sender, "npc", "reset", "reset the text of an NPC");
			format(sender, "npc", "rename [name]", "rename an NPC");
			format(sender, "npc", "remove (all)", "remove all NPC(s)");
			format(sender, "npc", "item [item]",
					"set the item that an NPC holds");
			format(sender, "npc", "armor [slot] [item]",
					"set the armor slot of an NPC");
			break;
		case 2:
			header(sender, "General", 2, 3);
			format(sender, "npc", "move", "move an NPC to your location");
			format(sender, "npc", "moveto [x y z](world pitch yaw)",
					"move NPC to a location");
			format(sender, "npc", "tp", "teleport to the location of an NPC");
			format(sender, "npc", "copy",
					"make a clone of an NPC at your location");
			format(sender, "npc", "select [ID]",
					"select an NPC with the given ID");
			format(sender, "npc", "setowner [name]", "set the owner of an NPC");
			format(sender, "npc", "talkclose", "make an NPC talk to players");
			format(sender, "npc", "lookat", "make an NPC look at players");
			format(sender, "npc", "list (name) (page)", "show a list of NPCs");
			break;
		case 3:
			header(sender, "General", 3, 3);
			format(sender, "npc", "[path|waypoints] (reset)",
					"control an NPC's waypoints");
			format(sender, "waypoint", "modifier [type]",
					"add a waypoint modifier to an NPC");
			footer(sender);
			break;
		default:
			sender.sendMessage(MessageUtils.getMaxPagesMessage(page, 3));
			break;
		}
	}

	/**
	 * Prints the header for the help menus
	 * 
	 * @param sender
	 * @param npcType
	 * @param page
	 * @param maxPages
	 */
	public static void header(CommandSender sender, String npcType, int page,
			int maxPages) {
		sender.sendMessage(ChatColor.YELLOW
				+ StringUtils.listify(ChatColor.GREEN + "Citizens " + npcType
						+ " Help" + ChatColor.WHITE + " <" + page + "/"
						+ maxPages + ">" + ChatColor.YELLOW));
	}

	/**
	 * Formats commands to fit a uniform style
	 * 
	 * @param sender
	 * @param command
	 * @param desc
	 */
	public static void format(CommandSender sender, String command,
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
	public static void footer(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GRAY
				+ StringUtils.listify("Coded by fullwall and aPunch"));
	}

	/**
	 * Helper method to send the help pages for an NPC type
	 * 
	 * @param type
	 *            NPC type
	 * @param sender
	 *            Sender to send help to
	 * @param page
	 *            Page number
	 */
	public static void sendHelp(CitizensNPC type, CommandSender sender, int page) {
		type.getCommands().sendHelp(sender, page);
	}
}