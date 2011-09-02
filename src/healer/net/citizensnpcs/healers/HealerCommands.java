package net.citizensnpcs.healers;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
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
		NPCTypeManager.getType("healer").getCommands().sendHelpPage(sender, 1);
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
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has reached the maximum level.");
			return;
		}
		String noPaymentMsg = ChatColor.GREEN
				+ "You have leveled up the healer "
				+ StringUtils.wrap(npc.getStrippedName()) + " to "
				+ StringUtils.wrap("level " + newLevel) + ".";
		if (EconomyManager.useEconPlugin()) {
			double price = UtilityProperties.getPrice("healer.levelup")
					* levelsUp;
			if (EconomyManager.hasEnough(player, price)) {
				double paid = EconomyManager.pay(player, price);
				if (paid > 0) {
					player.sendMessage(ChatColor.GREEN
							+ "You have leveled up the healer "
							+ StringUtils.wrap(npc.getStrippedName()) + " to "
							+ StringUtils.wrap("level " + newLevel) + " for "
							+ StringUtils.wrap(EconomyManager.format(paid))
							+ ".");
				} else if (paid == 0) {
					player.sendMessage(noPaymentMsg);
				}
			} else {
				Messaging.sendError(player,
						"You need " + EconomyManager.format(price)
								+ " more to level-up " + npc.getStrippedName()
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
	public void sendHelpPage(CommandSender sender, int page) {
		HelpUtils.header(sender, "Healer", 1, 1);
		HelpUtils.format(sender, "healer", "status",
				"view the health and level of a healer");
		HelpUtils.format(sender, "healer", "level-up (levels)",
				"level-up a healer");
		HelpUtils.footer(sender);
	}
}