package net.citizensnpcs.commands.commands;

import net.citizensnpcs.Permission;
import net.citizensnpcs.commands.CommandHandler;
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
public class WaypointCommands implements CommandHandler {

	@Command(
			aliases = { "wp", "waypoint" },
			usage = "modifier [type]",
			desc = "add a modifier",
			modifiers = { "modifier", "mod" },
			min = 2,
			max = 2)
	public static void modifier(CommandContext args, Player player, HumanNPC npc) {
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