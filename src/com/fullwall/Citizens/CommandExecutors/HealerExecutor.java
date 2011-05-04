package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerExecutor implements CommandExecutor {
	private Citizens plugin;

	public HealerExecutor(Citizens plugin) {
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
		if (NPCManager.validateSelected((Player) sender))
			npc = NPCManager
					.getNPC(NPCManager.NPCSelected.get(player.getName()));
		else {
			player.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			player.sendMessage(MessageUtils.notOwnerMessage);
			return true;

		}
		if (!npc.isHealer()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a healer yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equals("strength")) {
				if (BasicExecutor.hasPermission("citizens.healer.strength",
						sender)) {
					displayHealerStrength(player, npc, args);
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			} else if (args.length == 3
					&& (args[0].contains("b") || args[0].contains("s"))) {
				if (BasicExecutor
						.hasPermission("citizens.trader.stock", sender)) {
					changeTraderStock(player, npc, args[1], args[2],
							args[0].contains("s"));
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			} else if (args.length == 2 && (args[0].contains("unl"))) {
				if (BasicExecutor.hasPermission(
						"citizens.admin.unlimitedtrader", sender)) {
					changeUnlimited(npc, sender, args[1]);
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			} else if (args.length == 3 && args[0].contains("list")
					&& (args[1].contains("s") || args[1].contains("b"))) {
				if (BasicExecutor
						.hasPermission("citizens.trader.stock", sender)) {
					displayList(player, npc, args, args[1].contains("s"));
				} else
					player.sendMessage(MessageUtils.noPermissionsMessage);
				returnval = true;
			}
			HealerPropertyPool.saveHealerState(npc);
		}
		return returnval;
	}

	private void displayHealerStrength(Player player, HumanNPC npc,
			String[] args) {
		player.sendMessage(ChatColor.GREEN
				+ npc.getStrippedName()
				+ "has "
				+ StringUtils.yellowify(
						"" + HealerPropertyPool.getStrength(npc.getUID()),
						ChatColor.GREEN) + " strength remaining.");
	}
}
