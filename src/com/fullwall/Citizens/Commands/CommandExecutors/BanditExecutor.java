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
	private final Citizens plugin;

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
			sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
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
				if (Permission.canHelp(player, npc, "bandit")) {
					HelpUtils.sendBanditHelp(sender);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			} else if (args.length == 3
					&& args[0].equalsIgnoreCase("stealable")) {
				if (Permission.canModify(player, npc, "bandit")) {
					if (args[1].equalsIgnoreCase("add")) {
						changeStealable(player, npc, args[2], true);
					} else if (args[1].equalsIgnoreCase("remove")) {
						changeStealable(player, npc, args[2], false);
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
		}
		return returnval;
	}

	/**
	 * Add/remove an item from a bandit NPC's list of stealable items
	 * 
	 * @param player
	 * @param npc
	 * @param passed
	 * @return
	 */
	private void changeStealable(Player player, HumanNPC npc, String passed,
			boolean add) {
		String action = " added ";
		int id = 0;
		if (StringUtils.isNumber(passed)
				&& Material.getMaterial(Integer.parseInt(passed)) != null) {
			id = Integer.parseInt(passed);
		} else {
			player.sendMessage(MessageUtils.invalidItemIDMessage);
			return;
		}
		String material = StringUtils.wrap(Material.getMaterial(id).name());
		String keyword = " to ";
		boolean successful = false;
		if (add) {
			if (!npc.getBandit().getStealables().contains(id)) {
				npc.getBandit().addStealable(id);
				successful = true;
			} else {
				player.sendMessage(ChatColor.RED + "That item is already on "
						+ npc.getStrippedName() + "'s list of stealable items.");
			}
		} else {
			if (npc.getBandit().getStealables().contains(id)) {
				npc.getBandit().removeStealable(id);
				action = " removed ";
				keyword = " from ";
				successful = true;
			} else {
				player.sendMessage(ChatColor.RED + "That item is not on "
						+ npc.getStrippedName() + "'s list of stealable items.");
			}
		}
		if (successful) {
			player.sendMessage(ChatColor.GREEN + "You" + action + material
					+ keyword + StringUtils.wrap(npc.getStrippedName() + "'s")
					+ " list of stealable items.");
		}
	}
}