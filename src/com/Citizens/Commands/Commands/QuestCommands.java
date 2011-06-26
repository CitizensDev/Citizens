package com.citizens.Commands.Commands;

import org.bukkit.entity.Player;

import com.citizens.NPCTypes.Questers.Quests.ChatManager;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;
import com.citizens.resources.sk89q.commands.Command;
import com.citizens.resources.sk89q.commands.CommandContext;
import com.citizens.resources.sk89q.commands.CommandPermissions;
import com.citizens.resources.sk89q.commands.CommandRequirements;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "quester")
public class QuestCommands {

	@Command(
			aliases = "quests",
			usage = "edit",
			desc = "modify server quests",
			modifiers = "edit",
			min = 1,
			max = 1)
	@CommandPermissions("modify.quester")
	public static void editQuests(CommandContext args, Player player,
			HumanNPC npc) {
		ChatManager.setEditMode(player.getName(), true);
	}
}