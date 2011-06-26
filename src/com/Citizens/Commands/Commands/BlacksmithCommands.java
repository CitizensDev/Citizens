package com.Citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Citizens.NPCTypes.Blacksmiths.BlacksmithManager;
import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.resources.sk89q.commands.Command;
import com.Citizens.resources.sk89q.commands.CommandContext;
import com.Citizens.resources.sk89q.commands.CommandPermissions;
import com.Citizens.resources.sk89q.commands.CommandRequirements;
import com.Citizens.Utils.HelpUtils;
import com.Citizens.Utils.StringUtils;

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
	@CommandPermissions("use.blacksmith")
	public static void sendBlacksmithHelp(CommandContext args, Player player,
			HumanNPC npc) {
		HelpUtils.sendBlacksmithHelp(player);
	}

	@Command(
			aliases = "blacksmith",
			usage = "uses",
			desc = "show the uses remaining for your tool",
			modifiers = "uses",
			min = 1,
			max = 1)
	@CommandPermissions("use.blacksmith")
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