package com.fullwall.Citizens.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Traders.ItemPrice;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class MessageUtils {
	public static String noPermissionsMessage = ChatColor.RED
			+ "You don't have permission to use that command.";
	public static String notEnoughMoneyMessage = ChatColor.GRAY
			+ "You don't have enough money to do that.";
	public static String mustBeIngameMessage = "You must use this command ingame";
	public static String mustHaveNPCSelectedMessage = ChatColor.GRAY
			+ "You must have an NPC selected (right click).";
	public static String notOwnerMessage = ChatColor.RED
			+ "You are not the owner of this NPC.";

	public static String stripWhite(String check) {
		if (check.equals("§f"))
			return "";
		return check;
	}

	public static void sendText(HumanNPC npc, Entity entity, Citizens plugin) {
		String name = StringUtils.stripColour(npc.getStrippedName());
		int UID = npc.getUID();
		ArrayList<String> array = NPCManager.getBasicNPCText(UID);
		String text = "";
		if (array != null && array.size() > 0) {
			text = array.get(plugin.handler.ran.nextInt(array.size()));
		}
		if (text.isEmpty())
			text = PropertyPool.getDefaultText();
		if (!text.isEmpty()) {
			if (Citizens.useNPCColours)
				text = Citizens.chatFormat.replace("&", "§").replace("%name%",
						npc.getStrippedName())
						+ text;
			else
				text = Citizens.chatFormat.replace("%name%",
						Citizens.NPCColour + name + ChatColor.WHITE).replace(
						"&", "§")
						+ text;
			((Player) entity).sendMessage(text);
		}
	}

	public static String getText(HumanNPC npc, Entity entity, Citizens plugin) {
		String name = StringUtils.stripColour(npc.getStrippedName());
		int UID = npc.getUID();
		ArrayList<String> array = NPCManager.getBasicNPCText(UID);
		String text = "";
		if (array != null && array.size() > 0) {
			text = array.get(plugin.handler.ran.nextInt(array.size()));
		}
		if (text.isEmpty())
			text = PropertyPool.getDefaultText();
		if (!text.isEmpty()) {
			if (Citizens.useNPCColours) {
				text = Citizens.chatFormat.replace("&", "§").replace("%name%",
						npc.getStrippedName())
						+ text;
			} else
				text = Citizens.chatFormat.replace("%name%",
						Citizens.NPCColour + name + ChatColor.WHITE).replace(
						"&", "§")
						+ text;
			return text;
		}
		return "";
	}

	public static String getNoMoneyMessage(Operation op, Player player) {
		String message = "";
		message = ChatColor.RED
				+ "You need "
				+ EconomyHandler.getRemainder(Operation.BASIC_NPC_CREATE,
						player) + " more "
				+ EconomyHandler.getPaymentType(Operation.BASIC_NPC_CREATE)
				+ "(s) to do that.";
		return message;
	}

	public static String getPaidMessage(Operation op, int paid, String npcName,
			String type, boolean useType) {
		String message = "";
		message = ChatColor.GREEN + "Paid "
				+ StringUtils.yellowify("" + paid, ChatColor.GREEN) + " "
				+ EconomyHandler.getPaymentType(Operation.TRADER_NPC_CREATE)
				+ " for ";
		if (useType)
			message += StringUtils.yellowify(npcName, ChatColor.GREEN)
					+ " to become a "
					+ StringUtils.yellowify(type, ChatColor.GREEN) + ".";
		else
			message += StringUtils.yellowify(npcName, ChatColor.GREEN) + ".";
		return message;
	}

	public static String getPriceMessage(ItemPrice price) {
		String message = "";
		message += ChatColor.YELLOW + "" + price.getPrice() + " "
				+ EconomyHandler.getCurrency(price) + "(s)";
		return message;
	}
}
