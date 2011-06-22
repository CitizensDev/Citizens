package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.ChatManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class QuestsExecutor {

	@Command(
			aliases = "quests",
			usage = "/quests [edit]",
			desc = "modify server quests",
			modifier = "edit",
			min = 1,
			max = 1)
	@CommandPermissions("modify.quester")
	public static void editQuests(CommandContext args, Player player,
			HumanNPC npc) {
		ChatManager.enterEditMode(player.getName());
	}
}