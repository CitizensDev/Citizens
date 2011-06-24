package com.fullwall.Citizens.Commands.Commands;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.ChatManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.sk89q.commands.Command;
import com.fullwall.resources.sk89q.commands.CommandContext;
import com.fullwall.resources.sk89q.commands.CommandPermissions;
import com.fullwall.resources.sk89q.commands.CommandRequirements;

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