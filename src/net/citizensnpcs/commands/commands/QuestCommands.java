package net.citizensnpcs.commands.commands;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.questers.quests.ChatManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;

import org.bukkit.entity.Player;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "quester")
public class QuestCommands implements CommandHandler {

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