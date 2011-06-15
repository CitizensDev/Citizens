package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.ChatManager;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuestsExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Citizens plugin;

	public QuestsExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player player = (Player) sender;
		HumanNPC npc = null;
		boolean returnval = false;
		if (NPCManager.validateSelected((Player) sender)) {
			npc = NPCManager.get(NPCManager.selectedNPCs.get(player.getName()));
		} else {
			sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			sender.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isQuester()) {
			sender.sendMessage(ChatColor.RED + "Your NPC isn't a quester yet.");
			return true;
		}
		if (args.length == 1 && commandLabel.equalsIgnoreCase("quests")) {
			if (Permission.canModify(player, npc, "quester")) {
				if (args[0].equalsIgnoreCase("edit")) {
					ChatManager.enterEditMode(player.getName());
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			returnval = true;
		}
		return returnval;
	}
}