package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderExecutor implements CommandExecutor {

	private Citizens plugin;

	public TraderExecutor(Citizens plugin) {
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
		if (NPCManager.validateSelected((Player) sender))
			npc = NPCManager
					.getNPC(NPCManager.NPCSelected.get(player.getName()));
		else {
			player.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(npc.getUID(), player)) {
			player.sendMessage(MessageUtils.notOwnerMessage);
			return true;

		}
		if (!npc.isTrader()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a trader yet.");
			return true;
		} else {

		}
		return false;
	}
}
