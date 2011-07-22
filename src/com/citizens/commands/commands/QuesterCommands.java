package com.citizens.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.npctypes.questers.QuesterNPC;
import com.citizens.npctypes.questers.quests.QuestManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.utils.HelpUtils;
import com.citizens.utils.StringUtils;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "quester")
public class QuesterCommands {

	@CommandRequirements()
	@Command(
			aliases = "quester",
			usage = "help",
			desc = "view the quester help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("quester.use.help")
	public static void sendQuesterHelp(CommandContext args, Player player,
			HumanNPC npc) {
		HelpUtils.sendQuesterHelp(player);
	}

	@Command(
			aliases = "quester",
			usage = "assign [quest]",
			desc = "assign a quest to an NPC",
			modifiers = "assign",
			min = 2,
			max = 2)
	@CommandPermissions("quester.modify.assignquest")
	public static void assignQuest(CommandContext args, Player player,
			HumanNPC npc) {
		QuesterNPC quester = npc.getToggleable("quester");
		if (!QuestManager.validQuest(args.getString(1))) {
			player.sendMessage(ChatColor.GRAY
					+ "There is no quest by that name.");
			return;
		}
		quester.addQuest(args.getString(1));
		player.sendMessage(ChatColor.GREEN + "Quest "
				+ StringUtils.wrap(args.getString(1)) + " added to "
				+ StringUtils.wrap(npc.getName()) + "'s quests. "
				+ StringUtils.wrap(npc.getName()) + " now has "
				+ StringUtils.wrap(quester.getQuests().size()) + " quests.");
	}

	@Command(
			aliases = "quester",
			usage = "remove [quest]",
			desc = "remove a quest from an NPC",
			modifiers = "remove",
			min = 2,
			max = 2)
	@CommandPermissions("quester.modify.removequest")
	public static void removeQuest(CommandContext args, Player player,
			HumanNPC npc) {
		QuesterNPC quester = npc.getToggleable("quester");
		if (!QuestManager.validQuest(args.getString(1))) {
			player.sendMessage(ChatColor.GRAY
					+ "There is no quest by that name.");
			return;
		}
		quester.removeQuest(args.getString(1));
		player.sendMessage(ChatColor.GREEN + "Quest "
				+ StringUtils.wrap(args.getString(1)) + " removed from "
				+ StringUtils.wrap(npc.getName()) + "'s quests. "
				+ StringUtils.wrap(npc.getName()) + " now has "
				+ StringUtils.wrap(quester.getQuests().size()) + " quests.");
	}
}