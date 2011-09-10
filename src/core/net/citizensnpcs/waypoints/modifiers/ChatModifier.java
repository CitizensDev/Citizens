package net.citizensnpcs.waypoints.modifiers;

import java.util.ArrayList;
import java.util.List;

import joptsimple.internal.Strings;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.ConversationUtils.ChatType;
import net.citizensnpcs.utils.ConversationUtils.ConversationMessage;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointModifier;
import net.citizensnpcs.waypoints.WaypointModifierType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.base.Splitter;

public class ChatModifier extends WaypointModifier {
	private final List<String> messages = new ArrayList<String>();

	public ChatModifier(Waypoint waypoint) {
		super(waypoint);
	}

	@Override
	public void onReach(HumanNPC npc) {
		for (String message : messages) {
			npc.getPlayer().chat(message);
		}
	}

	@Override
	public void parse(Storage storage, String root) {
		for (String string : Splitter.on(",").split(
				storage.getString(root + ".messages"))) {
			messages.add(string);
		}
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".messages", Strings.join(messages, ","));
	}

	@Override
	public WaypointModifierType getType() {
		return WaypointModifierType.CHAT;
	}

	@Override
	public void begin(Player player) {
		// Commands will be checked against your own permissions.
		player.sendMessage(ChatColor.GREEN + "Enter messages to say.");
		player.sendMessage(ChatColor.GREEN + "Type "
				+ StringUtils.wrap("finish") + " to end.");
	}

	@Override
	public boolean converse(Player player, ConversationMessage message) {
		super.resetExit();
		if (StringUtils.isCommand(message.getMessage())) {
			player.sendMessage(ChatColor.GRAY + "Can't perform commands yet.");
			// player.sendMessage(ChatColor.GREEN + "Added command "
			// + StringUtils.wrap(ChatColor.stripColor(text)) + ".");
		} else {
			String text = StringUtils.colourise(message.getMessage());
			messages.add(text);
			player.sendMessage(ChatColor.GREEN + "Added "
					+ StringUtils.wrap(ChatColor.stripColor(text)) + ".");
		}
		return false;
	}

	@Override
	public boolean special(Player player, ChatType type) {
		if (type == ChatType.RESTART) {
			messages.clear();
		} else if (type == ChatType.UNDO && allowExit()) {
			messages.remove(messages.size() - 1);
		}
		return super.special(player, type);
	}

	@Override
	public boolean allowExit() {
		return messages.size() > 0;
	}

	@Override
	protected void onExit() {
		waypoint.addModifier(this);
	}
}