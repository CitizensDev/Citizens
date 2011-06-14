package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Citizens plugin;

	public QuesterExecutor(Citizens plugin) {
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
		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.canUse(player, npc, "quester")) {
					HelpUtils.sendQuesterHelp(sender);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			} else if (args.length == 2 && args[0].contains("assi")) {
				if (Permission.canModify(player, npc, "quester")) {
					assignQuest(player, npc, args[1]);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 2 && args[0].contains("rem")) {
				if (Permission.canModify(player, npc, "quester")) {
					removeQuest(player, npc, args[1]);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			PropertyManager.save(npc);
		}
		return returnval;
	}

	private void assignQuest(Player player, HumanNPC npc, String quest) {
		if (!QuestManager.validQuest(quest)) {
			player.sendMessage(ChatColor.GRAY
					+ "There is no quest by that name.");
			return;
		}
		npc.getQuester().addQuest(quest);
		player.sendMessage(ChatColor.GREEN + "Quest " + StringUtils.wrap(quest)
				+ " added to " + StringUtils.wrap(npc.getName())
				+ "'s quests. " + StringUtils.wrap(npc.getName()) + " now has "
				+ StringUtils.wrap(npc.getQuester().getQuests().size())
				+ " quests.");
	}

	private void removeQuest(Player player, HumanNPC npc, String quest) {
		if (!QuestManager.validQuest(quest)) {
			player.sendMessage(ChatColor.GRAY
					+ "There is no quest by that name.");
			return;
		}
		npc.getQuester().removeQuest(quest);
		player.sendMessage(ChatColor.GREEN + "Quest " + StringUtils.wrap(quest)
				+ " added to " + StringUtils.wrap(npc.getName())
				+ "'s quests. " + StringUtils.wrap(npc.getName()) + " now has "
				+ StringUtils.wrap(npc.getQuester().getQuests().size())
				+ " quests.");
	}
}