package com.citizens.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.npctypes.blacksmiths.BlacksmithManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.resources.sk89q.ServerCommand;
import com.citizens.utils.HelpUtils;
import com.citizens.utils.StringUtils;

@CommandRequirements(requiredType = "blacksmith")
public class BlacksmithCommands {

	@CommandRequirements()
	@Command(
			aliases = "blacksmith",
			usage = "help",
			desc = "view the blacksmith help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("blacksmith.use.help")
	@ServerCommand()
	public static void sendBlacksmithHelp(CommandContext args,
			CommandSender sender, HumanNPC npc) {
		HelpUtils.sendBlacksmithHelp(sender);
	}

	@Command(
			aliases = "blacksmith",
			usage = "uses",
			desc = "show the uses remaining for your tool",
			modifiers = "uses",
			min = 1,
			max = 1)
	@CommandPermissions("blacksmith.use.uses")
	public static void showUses(CommandContext args, Player player, HumanNPC npc) {
		ItemStack item = player.getItemInHand();
		String itemName = item.getType().name().toLowerCase().replace("_", " ");
		if (BlacksmithManager.validateTool(item)
				|| BlacksmithManager.validateArmor(item)) {
			player.sendMessage(ChatColor.GREEN
					+ "Your "
					+ StringUtils.wrap(itemName)
					+ " has "
					+ StringUtils.wrap(Material.getMaterial(item.getTypeId())
							.getMaxDurability() - item.getDurability())
					+ " uses remaining.");
		} else {
			player.sendMessage(ChatColor.RED + itemName
					+ " does not have a durability.");
		}
	}
}