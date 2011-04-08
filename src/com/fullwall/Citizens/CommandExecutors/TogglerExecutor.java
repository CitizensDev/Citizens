package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TogglerExecutor implements CommandExecutor {

	private Citizens plugin;

	public TogglerExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	// perhaps can be removed. EG: buy a feature, now the
	// chat syntax gets unlocked, and you can do things like
	// "Hi, I want to trade" etc. without having to toggle functionality.
	// this is good for now I guess.
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player p = (Player) sender;
		HumanNPC npc = null;
		if (NPCManager.validateSelected((Player) sender))
			npc = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		else {
			p.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(npc.getUID(), p)) {
			p.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED
					+ "You didn't specify an NPC type to toggle.");
			return true;
		} else {
			if (args[0].equals("trader")) {
				if (BasicExecutor.hasPermission("citizens.trader.create",
						sender)) {
					npc.setTrader(!npc.isTrader());
					if (npc.isTrader())
						p.sendMessage(ChatColor.YELLOW + ""
								+ npc.getSpacedName() + ChatColor.GREEN
								+ " is now a trader!");
					else
						p.sendMessage(ChatColor.YELLOW + ""
								+ npc.getSpacedName() + ChatColor.GREEN
								+ " has stopped being a trader.");
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			}
		}
		return false;
	}
}
