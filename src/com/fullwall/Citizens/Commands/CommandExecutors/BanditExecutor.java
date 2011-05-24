package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;

	public BanditExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player player = (Player) sender;
		HumanNPC npc = null;
		boolean returnval = false;
		if (NPCManager.validateSelected((Player) sender)) {
			npc = NPCManager.get(NPCManager.selectedNPCs.get(player.getName()));
		} else {
			sender.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			sender.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isBandit()) {
			sender.sendMessage(ChatColor.RED + "Your NPC isn't a bandit yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.hasPermission("citizens.bandit.help", sender)) {
					HelpUtils.sendBanditHelp(sender);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			} else if (args.length == 2 && args[0].equalsIgnoreCase("steal")) {
				if (Permission.hasPermission("citizens.bandit.steal", sender)) {
					addStealableItem(player, npc, args[1]);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
		}
		return returnval;
	}

	/**
	 * Set an item as stealable by a bandit NPC
	 * 
	 * @param id
	 */
	private void addStealableItem(Player player, HumanNPC npc, String id) {
		try {
			if (!npc.getBandit().getStealables().contains(Integer.parseInt(id))) {
				if (validateItem(player, id)) {
					npc.getBandit().addStealable(Integer.parseInt(id));
					player.sendMessage(ChatColor.GREEN
							+ "You added "
							+ StringUtils.wrap(Material.getMaterial(
									Integer.parseInt(id)).name()) + " to "
							+ StringUtils.wrap(npc.getStrippedName() + "'s")
							+ " list of stealable items.");
				}
			} else {
				player.sendMessage(ChatColor.RED
						+ "That item is already on the list.");
			}
		} catch (NumberFormatException ex) {
			player.sendMessage(ChatColor.RED + "That is not a valid item ID.");
		}
	}

	/**
	 * Verify that the passed string id is a valid item ID
	 * 
	 * @param player
	 * @param id
	 * @return
	 */
	private boolean validateItem(Player player, String passed) {
		if (Material.getMaterial(Integer.parseInt(passed)) == null) {
			player.sendMessage(ChatColor.RED + "That is not a valid item ID.");
			return false;
		}
		return true;
	}
}