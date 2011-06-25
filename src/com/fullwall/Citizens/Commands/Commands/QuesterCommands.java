package com.fullwall.Citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.QuesterNPC;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.sk89q.commands.Command;
import com.fullwall.resources.sk89q.commands.CommandContext;
import com.fullwall.resources.sk89q.commands.CommandPermissions;
import com.fullwall.resources.sk89q.commands.CommandRequirements;

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
	@CommandPermissions("use.quester")
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
	@CommandPermissions("modify.quester")
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
	@CommandPermissions("modify.quester")
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