package net.citizensnpcs.healers;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "healer")
public class HealerCommands implements CommandHandler {

	@CommandRequirements()
	@Command(
			aliases = "healer",
			usage = "help",
			desc = "view the healer help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("healer.use.help")
	public static void healerHelp(CommandContext args, Player player,
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
	@CommandPermissions("healer.use.status")
	public static void status(CommandContext args, Player player, HumanNPC npc) {
		Healer healer = npc.getType("healer");
		player.sendMessage(ChatColor.GREEN
				+ StringUtils.listify(StringUtils.wrap(npc.getStrippedName()
						+ "'s Healer Status")));
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
			modifiers = { "level-up", "lvl" },
			min = 1,
			max = 2)
	@CommandPermissions("healer.modify.levelup")
	public static void levelUp(CommandContext args, Player player, HumanNPC npc) {
		if (EconomyManager.useEconPlugin()) {
			Healer healer = npc.getType("healer");
			int level = healer.getLevel();
			int levelsUp = 1;
			if (args.argsLength() == 2
					&& StringUtils.isNumber(args.getString(1))) {
				levelsUp = args.getInteger(1);
			}
			if ((level + levelsUp) > 10) {
				levelsUp = (10 - level);
				player.sendMessage(ChatColor.GRAY
						+ "Number of levels-up brought down to "
						+ StringUtils.wrap(levelsUp)
						+ " (maximum 10 healer levels).");
			}
			double paid = EconomyManager.pay(player,
					UtilityProperties.getPrice("healer.levelup") * levelsUp);
			if (paid > 0) {
				if (level < 10) {
					int newLevel = level + levelsUp;
					healer.setLevel(newLevel);
					player.sendMessage(ChatColor.GREEN
							+ "You have leveled up the healer "
							+ StringUtils.wrap(npc.getStrippedName()) + " to "
							+ StringUtils.wrap("Level " + newLevel) + " for "
							+ StringUtils.wrap(EconomyManager.format(paid))
							+ ".");
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

	@Override
	public void addPermissions() {
		PermissionManager.addPerm("healer.use.help");
		PermissionManager.addPerm("healer.use.status");
		PermissionManager.addPerm("healer.modify.levelup");
		PermissionManager.addPerm("healer.use.heal");
	}
}