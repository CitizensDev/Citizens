package net.citizensnpcs.questers;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.resources.sk89q.ServerCommand;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "quester")
public class QuesterCommands implements CommandHandler {
	public static final QuesterCommands INSTANCE = new QuesterCommands();

	private QuesterCommands() {
	}

	@CommandRequirements()
	@ServerCommand()
	@Command(
			aliases = "quester",
			usage = "help",
			desc = "view the quester help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("quester.use.help")
	public static void questerHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		HelpUtils.sendQuesterHelp(sender);
	}

	@Command(
			aliases = "quester",
			usage = "assign [quest]",
			desc = "assign a quest to an NPC",
			modifiers = "assign",
			min = 2,
			max = 2)
	@CommandPermissions("quester.modify.assignquest")
	public static void assign(CommandContext args, Player player, HumanNPC npc) {
		Quester quester = npc.getType("quester");
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
	public static void remove(CommandContext args, Player player, HumanNPC npc) {
		Quester quester = npc.getType("quester");
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

	@Command(
			aliases = "quests",
			usage = "edit",
			desc = "modify server quests",
			modifiers = "edit",
			min = 1,
			max = 1)
	@CommandPermissions("quester.modify.questedit")
	public static void edit(CommandContext args, Player player, HumanNPC npc) {
	}

	@Override
	public void addPermissions() {
		PermissionManager.addPerm("quester.use.help");
		PermissionManager.addPerm("quester.modify.assignquest");
		PermissionManager.addPerm("quester.modify.removequest");
		PermissionManager.addPerm("quester.modify.questedit");
	}
}