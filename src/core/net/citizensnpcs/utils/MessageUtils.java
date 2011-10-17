package net.citizensnpcs.utils;

import java.util.Arrays;
import java.util.Deque;
import java.util.Random;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.PageUtils.PageInstance;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MessageUtils {
	public static final String noPermissionsMessage = ChatColor.RED
			+ "You don't have permission to do that.";
	public static String notEnoughMoneyMessage = ChatColor.GRAY
			+ "You don't have enough money to do that.";
	public static final String mustBeIngameMessage = "You must use this command ingame";
	public static final String mustHaveNPCSelectedMessage = ChatColor.GRAY
			+ "You must have an NPC selected (right click).";
	public static final String notOwnerMessage = ChatColor.RED
			+ "You are not the owner of this NPC.";
	public static String invalidItemIDMessage = ChatColor.RED
			+ "That is not a valid item ID.";
	public static final String noEconomyMessage = ChatColor.GRAY
			+ "This server is not using an economy plugin.";
	public static final String reachedNPCLimitMessage = ChatColor.RED
			+ "You have reached the NPC-creation limit.";
	public static final String invalidNPCTypeMessage = "Invalid NPC type.";

	// Parses a basic npc's text for sending.
	public static void sendText(HumanNPC npc, Player player) {
		if (!npc.getNPCData().isTalk())
			return;
		String text = getText(npc, player);
		if (!text.isEmpty()) {
			Messaging.send(player, npc, text);
		}
	}

	// Gets the text to be said for a basic NPC
	public static String getText(HumanNPC npc, Player player) {
		String name = StringUtils.stripColour(npc.getStrippedName());
		Deque<String> array = NPCDataManager.getText(npc.getUID());
		String text = "";
		if (array != null && array.size() > 0) {
			text = array.pop();
			array.addLast(text);
			NPCDataManager.setText(npc.getUID(), array);
		}
		if (text.isEmpty()) {
			text = getRandomMessage(SettingsManager.getString("DefaultText"));
		}
		if (!text.isEmpty()) {
			if (SettingsManager.getBoolean("UseNPCColors")) {
				text = StringUtils.colourise(SettingsManager.getString(
						"ChatFormat").replace("%name%", npc.getStrippedName()))
						+ text;
			} else {
				text = StringUtils.colourise(SettingsManager.getString(
						"ChatFormat").replace(
						"%name%",
						"&" + SettingsManager.getString("NPCColor") + name
								+ ChatColor.WHITE))
						+ text;
			}
			return text;
		}
		return "";
	}

	// Formats the not enough money message for an operation.
	public static String getNoMoneyMessage(Player player, String path) {
		String message;
		message = ChatColor.RED
				+ "You need "
				+ StringUtils.wrap(
						EconomyManager.format(EconomyManager.getRemainder(
								player, UtilityProperties.getPrice(path))),
						ChatColor.RED) + " more to do that.";
		return message;
	}

	// Formats the paid message for an operation.
	public static String getPaidMessage(Player player, String npcType,
			String path, String npcName, boolean useType) {
		String message;
		message = ChatColor.GREEN
				+ "Paid "
				+ StringUtils.wrap(EconomyManager.format(UtilityProperties
						.getPrice(path))) + " for ";
		if (useType) {
			message += StringUtils.wrap(npcName) + " to become a "
					+ StringUtils.wrap(npcType) + ".";
		} else {
			message += StringUtils.wrap(npcName) + ".";
		}
		return message;
	}

	public static String getStackString(ItemStack stack, ChatColor colour) {
		if (stack == null) {
			return "";
		}
		return StringUtils.wrap(StringUtils.pluralise(stack.getAmount() + " "
				+ getMaterialName(stack.getTypeId()), stack.getAmount()),
				colour);
	}

	public static String getItemName(int itemID) {
		return UtilityProperties.getItemOverride(itemID).isEmpty() ? getMaterialName(itemID)
				: UtilityProperties.getItemOverride(itemID);
	}

	public static String getMaterialName(int itemID) {
		String[] parts = Material.getMaterial(itemID).name().toLowerCase()
				.split("_");
		int count = 0;
		for (String s : parts) {
			parts[count] = StringUtils.capitalise(s);
			if (count < parts.length - 1) {
				parts[count] += " ";
			}
			++count;
		}
		return Arrays.toString(parts).replace("[", "").replace("]", "")
				.replace(", ", "");
	}

	public static String getStackString(ItemStack item) {
		return getStackString(item, ChatColor.YELLOW);
	}

	// Get the max-pages message
	public static String getMaxPagesMessage(int page, int maxPages) {
		return ChatColor.GRAY + "The total number of pages is "
				+ StringUtils.wrap(maxPages, ChatColor.GRAY) + ", page "
				+ StringUtils.wrap(page, ChatColor.GRAY) + " is not available.";
	}

	// Pulls a random message from a string of messages split by a semi-colon
	public static String getRandomMessage(String messages) {
		String[] split = messages.split(";");
		String text = split[new Random().nextInt(split.length)];
		if (text.equals(SettingsManager.getString("DefaultText"))) {
			return text.replace('&', '§');
		}
		return text;
	}

	// Display a list of NPCs owned by a player
	public static void displayNPCList(Player sender, Player toDisplay,
			HumanNPC npc, String passed) {
		PageInstance paginate = PageUtils.newInstance(sender);
		for (HumanNPC hnpc : NPCManager.getList().values()) {
			if (hnpc.getOwner().equals(toDisplay.getName())) {
				paginate.push(ChatColor.GRAY + "" + hnpc.getUID()
						+ ChatColor.YELLOW + " " + hnpc.getStrippedName());
			}
		}
		int page = Integer.parseInt(passed);
		if (page == 0) {
			page = 1;
		}
		if (page <= paginate.maxPages()) {
			paginate.header(ChatColor.GREEN + "========== NPC List for "
					+ StringUtils.wrap(toDisplay.getName())
					+ " (%x/%y) ==========");
			paginate.process(page);
		} else {
			sender.sendMessage(MessageUtils.getMaxPagesMessage(page,
					paginate.maxPages()));
		}
	}
}