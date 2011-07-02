package com.citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.Economy.EconomyHandler;
import com.citizens.Economy.EconomyHandler.Operation;
import com.citizens.NPCTypes.Healers.HealerNPC;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Resources.sk89q.Command;
import com.citizens.Resources.sk89q.CommandContext;
import com.citizens.Resources.sk89q.CommandPermissions;
import com.citizens.Resources.sk89q.CommandRequirements;
import com.citizens.Utils.HelpUtils;
import com.citizens.Utils.StringUtils;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "healer")
public class HealerCommands {

	@CommandRequirements()
	@Command(
			aliases = "healer",
			usage = "help",
			desc = "view the healer help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.healer")
	public static void sendHealerHelp(CommandContext args, Player player,
			HumanNPC npc) {
		HelpUtils.sendHealerHelp(player);
	}

	@Command(
			aliases = "healer",
			usage = "status",
			desc = "view the status of a healer",
			modifiers = "status",
			min = 1,
			max = 1)
	@CommandPermissions("use.healer")
	public static void displayStatus(CommandContext args, Player player,
			HumanNPC npc) {
		HealerNPC healer = npc.getToggleable("healer");
		player.sendMessage(ChatColor.GREEN + "========== "
				+ StringUtils.wrap(npc.getStrippedName() + "'s Healer Status")
				+ " ==========");
		player.sendMessage(ChatColor.YELLOW + "Health: " + ChatColor.GREEN
				+ healer.getHealth() + ChatColor.RED + "/"
				+ healer.getMaxHealth());
		player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.GREEN
				+ healer.getLevel() + ChatColor.RED + "/10");
	}

	@Command(
			aliases = "healer",
			usage = "level-up (levels)",
			desc = "level-up a healer",
			modifiers = "level-up",
			min = 1,
			max = 2)
	@CommandPermissions("modify.healer")
	public static void levelUp(CommandContext args, Player player, HumanNPC npc) {
		if (EconomyHandler.useEconomy()) {
			HealerNPC healer = npc.getToggleable("healer");
			int level = healer.getLevel();
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
					healer.setLevel(newLevel);
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