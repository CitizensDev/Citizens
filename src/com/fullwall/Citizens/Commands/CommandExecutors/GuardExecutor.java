package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;

	public GuardExecutor(Citizens plugin) {
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
			npc = NPCManager.get(NPCManager.NPCSelected.get(player.getName()));
		} else {
			sender.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			sender.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isGuard()) {
			sender.sendMessage(ChatColor.RED + "Your NPC isn't a guard yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.hasPermission("citizens.guard.help", sender)) {
					HelpUtils.sendGuardHelp(sender);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 1
					&& args[0].equalsIgnoreCase("bodyguard")) {
				if (Permission.hasPermission("citizens.guard.bodyguard.create",
						sender)) {
					toggleBodyguard(player, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("bouncer")) {
				if (Permission.hasPermission("citizens.guard.bouncer.create",
						sender)) {
					toggleBouncer(player, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			PropertyManager.save(npc);
		}
		return returnval;
	}

	/**
	 * Toggle the bodyguard state of a bodyguard
	 * 
	 * @param player
	 * @param npc
	 */
	private void toggleBodyguard(Player player, HumanNPC npc) {
		if (!npc.getGuard().isBodyguard()) {
			npc.getGuard().setBodyguard(true);
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " is now a bodyguard.");
			if (npc.getGuard().isBouncer()) {
				npc.getGuard().setBouncer(false);
			}
		} else {
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " is already a bodyguard.");
		}
	}

	/**
	 * Toggle the bouncer state of a guard NPC
	 * 
	 * @param player
	 * @param npc
	 */
	private void toggleBouncer(Player player, HumanNPC npc) {
		if (!npc.getGuard().isBouncer()) {
			npc.getGuard().setBouncer(true);
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " is now a bouncer.");
			if (npc.getGuard().isBodyguard()) {
				npc.getGuard().setBodyguard(false);
			}
		} else {
			player.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " is already a bouncer.");
		}
	}
}