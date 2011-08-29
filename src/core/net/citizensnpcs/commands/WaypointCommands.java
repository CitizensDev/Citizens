package net.citizensnpcs.commands;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.npcs.NPCDataManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.utils.ConversationUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointModifierType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandRequirements(requireSelected = true, requireOwnership = true)
public class WaypointCommands extends CommandHandler {

	@Command(
			aliases = { "wp", "waypoint" },
			usage = "modifier [type]",
			desc = "add a modifier",
			modifiers = { "modifier", "mod" },
			min = 2,
			max = 2)
	public static void modifier(CommandContext args, Player player, HumanNPC npc) {
		if (!NPCDataManager.pathEditors.containsKey(player.getName())) {
			player.sendMessage(ChatColor.GRAY
					+ "You must be editing your NPC's path.");
			return;
		}
		WaypointModifierType modifier = WaypointModifierType.value(args
				.getString(1).toUpperCase());
		if (modifier == null) {
			player.sendMessage(ChatColor.GRAY + "Invalid modifier type.");
			return;
		}
		if (!PermissionManager.generic(player, "citizens.waypoints.modifier"
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

	@Override
	public void addPermissions() {
		for (WaypointModifierType modifier : WaypointModifierType.values()) {
			CitizensManager.addPermission("waypoints.modifier."
					+ modifier.name().toLowerCase());
		}
	}
}