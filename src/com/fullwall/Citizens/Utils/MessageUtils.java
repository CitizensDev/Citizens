package com.fullwall.Citizens.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class MessageUtils {

	public static String stripWhite(String check) {
		if (check.equals("§f"))
			return "";
		return check;
	}

	public static void sendText(HumanNPC npc, Entity entity, Citizens plugin) {
		String name = StringUtils.stripColour(npc.getSpacedName());
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
						npc.getSpacedName())
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
		String name = StringUtils.stripColour(npc.getSpacedName());
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
						npc.getSpacedName())
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
}
