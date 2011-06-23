package com.fullwall.Citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandRequirements;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "quester")
public class QuesterCommands {

	@CommandRequirements(requiredType = "quester")
	@Command(
			aliases = "quester",
			usage = "help",
			desc = "view the quester help page",
			modifier = "help",
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
			modifier = "assign",
			min = 2,
			max = 2)
	@CommandPermissions("modify.quester")
	public static void assignQuest(CommandContext args, Player player,
			HumanNPC npc) {
		if (!QuestManager.validQuest(args.getString(1))) {
			player.sendMessage(ChatColor.GRAY
					+ "There is no quest by that name.");
			return;
		}
		npc.getQuester().addQuest(args.getString(1));
		player.sendMessage(ChatColor.GREEN + "Quest "
				+ StringUtils.wrap(args.getString(1)) + " added to "
				+ StringUtils.wrap(npc.getName()) + "'s quests. "
				+ StringUtils.wrap(npc.getName()) + " now has "
				+ StringUtils.wrap(npc.getQuester().getQuests().size())
				+ " quests.");
	}

	@Command(
			aliases = "quester",
			usage = "remove [quest]",
			desc = "remove a quest from an NPC",
			modifier = "remove",
			min = 2,
			max = 2)
	@CommandPermissions("modify.quester")
	public static void removeQuest(CommandContext args, Player player,
			HumanNPC npc) {
		if (!QuestManager.validQuest(args.getString(1))) {
			player.sendMessage(ChatColor.GRAY
					+ "There is no quest by that name.");
			return;
		}
		npc.getQuester().removeQuest(args.getString(1));
		player.sendMessage(ChatColor.GREEN + "Quest "
				+ StringUtils.wrap(args.getString(1)) + " removed from "
				+ StringUtils.wrap(npc.getName()) + "'s quests. "
				+ StringUtils.wrap(npc.getName()) + " now has "
				+ StringUtils.wrap(npc.getQuester().getQuests().size())
				+ " quests.");
	}
}