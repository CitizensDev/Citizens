package com.fullwall.Citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandRequirements;

public class HealerCommands {

	@CommandRequirements(
			requiredType = "healer")
	@Command(
			aliases = "healer",
			usage = "help",
			desc = "view the healer help page",
			modifier = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.healer")
	public static void sendHealerHelp(CommandContext args, Player player, HumanNPC npc) {
		HelpUtils.sendHealerHelp(player);
	}

	@CommandRequirements(
			requireSelected = true,
			requireOwnership = true,
			requiredType = "healer")
	@Command(
			aliases = "healer",
			usage = "status",
			desc = "view the status of a healer",
			modifier = "status",
			min = 1,
			max = 1)
	@CommandPermissions("use.healer")
	public static void displayStatus(CommandContext args, Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN + "========== "
				+ StringUtils.wrap(npc.getStrippedName() + "'s Healer Status")
				+ " ==========");
		player.sendMessage(ChatColor.YELLOW + "Health: " + ChatColor.GREEN
				+ npc.getHealer().getHealth() + ChatColor.RED + "/"
				+ npc.getHealer().getMaxHealth());
		player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.GREEN
				+ npc.getHealer().getLevel() + ChatColor.RED + "/10");
	}

	@CommandRequirements(
			requireSelected = true,
			requireOwnership = true,
			requiredType = "healer")
	@Command(
			aliases = "healer",
			usage = "level-up (levels)",
			desc = "level-up a healer",
			modifier = "level-up",
			min = 1,
			max = 2)
	@CommandPermissions("modify.healer")
	public static void levelUp(CommandContext args, Player player, HumanNPC npc) {
		if (EconomyHandler.useEconomy()) {
			int level = npc.getHealer().getLevel();
			int levelsUp = 1;
			if (args.argsLength() == 2) {
				if (StringUtils.isNumber(args.getString(1))) {
					levelsUp = Integer.parseInt(args.getString(1));
				}
			}
			double paid = EconomyHandler.pay(Operation.HEALER_LEVELUP, player,
					levelsUp);
			if (paid > 0) {
				if (level < 10) {
					int newLevel = level + levelsUp;
					npc.getHealer().setLevel(newLevel);
					player.sendMessage(ChatColor.GREEN
							+ "You have leveled up the healer "
							+ StringUtils.wrap(npc.getStrippedName())
							+ " to "
							+ StringUtils.wrap("Level " + newLevel)
							+ " for "
							+ StringUtils.wrap(EconomyHandler.getPaymentType(
									Operation.HEALER_LEVELUP, "" + paid
											* levelsUp)
									+ "."));
				} else {
					player.sendMessage(StringUtils.wrap(npc.getStrippedName())
							+ " has reached the maximum level.");
				}
			}
		} else {
			player.sendMessage(ChatColor.GRAY
					+ "Your server has not turned economy on for Citizens.");
		}
	}
}