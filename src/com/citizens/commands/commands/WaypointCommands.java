package com.citizens.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.npcs.NPCDataManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.utils.ConversationUtils;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.StringUtils;
import com.citizens.waypoints.Waypoint;
import com.citizens.waypoints.WaypointModifierType;

@CommandRequirements(requireSelected = true, requireOwnership = true)
public class WaypointCommands {

	@Command(
			aliases = { "wp", "waypoint" },
			usage = "modifier [type]",
			desc = "add a modifier",
			modifiers = { "modifier", "mod" },
			min = 2,
			max = 2)
	public static void addWaypointModifier(CommandContext args, Player player,
			HumanNPC npc) {
		if (NPCDataManager.pathEditors.get(player.getName()) == null) {
			player.sendMessage(ChatColor.GRAY
					+ "You must be editing your NPC's path.");
			return;
		}
		String type = args.getString(2);
		WaypointModifierType modifier = WaypointModifierType.value(type
				.toUpperCase());
		if (modifier == null) {
			player.sendMessage(ChatColor.GRAY + "Invalid modifier type.");
			return;
		}
		if (!Permission.generic(player, "citizens.waypoints.modifier"
				+ modifier.name().toLowerCase())) {
			player.sendMessage(MessageUtils.noPermissionsMessage);
			return;
		}
		player.sendMessage(ChatColor.AQUA
				+ StringUtils.listify(StringUtils.wrap(StringUtils
						.capitalise(modifier.name().toLowerCase()))
						+ " chat editor" + ChatColor.AQUA));
		Waypoint waypoint = npc.getWaypoints().getLast();
		ConversationUtils.addConverser(player, modifier.create(waypoint));
	}
}