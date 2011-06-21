package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.ChatManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandRequirements;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "quester")
public class QuestsExecutor {

	@Command(
			aliases = "quests",
			usage = "/quests [edit]",
			desc = "modify a quester's quests",
			modifier = "edit",
			min = 1,
			max = 1)
	@CommandPermissions("modify.quester")
	public void editQuests(Player player, HumanNPC npc, String[] args) {
		ChatManager.enterEditMode(player.getName());
	}
}