package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.resources.sk89q.ServerCommand;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandRequirements(requiredType = "blacksmith")
public class BlacksmithCommands extends CommandHandler {
	public static final BlacksmithCommands INSTANCE = new BlacksmithCommands();

	private BlacksmithCommands() {
	}

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
	public static void blacksmithHelp(CommandContext args,
			CommandSender sender, HumanNPC npc) {
		NPCTypeManager.getType("blacksmith").getCommands()
				.sendHelpPage(sender, 1);
	}

	@Command(
			aliases = "blacksmith",
			usage = "status",
			desc = "view the status of your in-hand item",
			modifiers = "status",
			min = 1,
			max = 1)
	@CommandPermissions("blacksmith.use.status")
	public static void status(CommandContext args, Player player, HumanNPC npc) {
		ItemStack item = player.getItemInHand();
		String repairType = "";
		if (InventoryUtils.isArmor(item.getTypeId())) {
			repairType = "armorrepair";
		} else if (InventoryUtils.isTool(item.getTypeId())) {
			repairType = "toolrepair";
		} else {
			Messaging.sendError(player,
					MessageUtils.getMaterialName(item.getTypeId())
							+ " is not a repairable item.");
			return;
		}
		player.sendMessage(ChatColor.GREEN
				+ "Item: "
				+ StringUtils.wrap(MessageUtils.getMaterialName(item
						.getTypeId())));
		if (EconomyManager.useEconPlugin()) {
			player.sendMessage(ChatColor.GREEN
					+ "Cost: "
					+ StringUtils.wrap(EconomyManager.format(BlacksmithManager
							.getBlacksmithPrice(player, repairType))));
		}
		player.sendMessage(ChatColor.GREEN
				+ "Durability Remaining: "
				+ StringUtils.wrap(Material.getMaterial(item.getTypeId())
						.getMaxDurability() - item.getDurability()));
	}

	@Override
	public void addPermissions() {
		CitizensManager.addPermission("blacksmith.use.help");
		CitizensManager.addPermission("blacksmith.use.status");
		CitizensManager.addPermission("blacksmith.use.repair");
	}

	@Override
	public void sendHelpPage(CommandSender sender, int page) {
		HelpUtils.header(sender, "Blacksmith", 1, 1);
		HelpUtils.format(sender, "blacksmith", "status",
				"view the status of your in-hand item");
		HelpUtils.footer(sender);
	}
}