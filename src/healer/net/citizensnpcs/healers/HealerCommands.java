package net.citizensnpcs.healers;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.economy.Economy;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.sk89q.Command;
import net.citizensnpcs.sk89q.CommandContext;
import net.citizensnpcs.sk89q.CommandPermissions;
import net.citizensnpcs.sk89q.CommandRequirements;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "healer")
public class HealerCommands extends CommandHandler {
	public static final HealerCommands INSTANCE = new HealerCommands();

	private HealerCommands() {
	}

	@CommandRequirements()
	@Command(
			aliases = "healer",
			usage = "help",
			desc = "view the healer help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("healer.use.help")
	public static void healerHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		INSTANCE.sendHelpPage(sender);
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
				+ StringUtils.listify(StringUtils.wrap(npc.getName()
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
			modifiers = { "level-up", "lvl", "levelup", "lvlup" },
			min = 1,
			max = 2)
	@CommandPermissions("healer.modify.levelup")
	public static void levelUp(CommandContext args, Player player, HumanNPC npc) {
		int levelsUp = 1;
		if (args.argsLength() == 2) {
			if (StringUtils.isNumber(args.getString(1))) {
				levelsUp = args.getInteger(1);
			} else {
				Messaging.sendError(player, "That is not a valid number.");
				return;
			}
		}
		Healer healer = npc.getType("healer");
		int level = healer.getLevel();
		int newLevel;
		if (level + levelsUp <= 10) {
			newLevel = level + levelsUp;
			healer.setLevel(newLevel);
		} else {
			player.sendMessage(StringUtils.wrap(npc.getName())
					+ " has reached the maximum level.");
			return;
		}
		String noPaymentMsg = ChatColor.GREEN
				+ "You have leveled up the healer "
				+ StringUtils.wrap(npc.getName()) + " to "
				+ StringUtils.wrap("level " + newLevel) + ".";
		if (Economy.useEconPlugin()) {
			double price = UtilityProperties.getPrice("healer.levelup")
					* levelsUp;
			if (Economy.hasEnough(player, price)) {
				double paid = Economy.pay(player, price);
				if (paid > 0) {
					player.sendMessage(ChatColor.GREEN
							+ "You have leveled up the healer "
							+ StringUtils.wrap(npc.getName()) + " to "
							+ StringUtils.wrap("level " + newLevel) + " for "
							+ StringUtils.wrap(Economy.format(paid))
							+ ".");
				} else if (paid == 0) {
					player.sendMessage(noPaymentMsg);
				}
			} else {
				Messaging.sendError(player,
						"You need " + Economy.format(price)
								+ " more to level-up " + npc.getName()
								+ ".");
			}
		} else {
			player.sendMessage(noPaymentMsg);
		}
	}

	@Override
	public void addPermissions() {
		PermissionManager.addPermission("healer.use.help");
		PermissionManager.addPermission("healer.use.status");
		PermissionManager.addPermission("healer.modify.levelup");
		PermissionManager.addPermission("healer.use.heal");
	}

	@Override
	public void sendHelpPage(CommandSender sender) {
		HelpUtils.header(sender, "Healer", 1, 1);
		HelpUtils.format(sender, "healer", "status",
				"view the health and level of a healer");
		HelpUtils.format(sender, "healer", "level-up (levels)",
				"level-up a healer");
		HelpUtils.footer(sender);
	}
}