package com.fullwall.Citizens.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.NPCTypes.Blacksmiths.BlacksmithManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandRequirements;

public class BlacksmithCommands {
	
	@CommandRequirements(
			requiredType = "blacksmith")
	@Command(
			aliases = "blacksmith",
			usage = "help",
			desc = "view the blacksmith help page",
			modifier = "help",
			min = 1,
			max = 1)
	@CommandPermissions("use.blacksmith")
	public static void sendBlacksmithHelp(CommandContext args, Player player, HumanNPC npc) {
		HelpUtils.sendBlacksmithHelp(player);
	}
	
	@CommandRequirements(
			requiredType = "blacksmith")
	@Command(
			aliases = "blacksmith",
			usage = "uses",
			desc = "show the uses remaining for your tool",
			modifier = "uses",
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