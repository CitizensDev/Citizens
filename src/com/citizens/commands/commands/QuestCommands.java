package com.citizens.commands.commands;

import org.bukkit.entity.Player;

import com.citizens.npctypes.questers.quests.ChatManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;

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
	@CommandPermissions("quester.modify.questedit")
	public static void edit(CommandContext args, Player player, HumanNPC npc) {
		ChatManager.setEditMode(player.getName(), true);
	}
}