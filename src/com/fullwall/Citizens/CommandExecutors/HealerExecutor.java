package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
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
			if (args.length == 1 && args[0].equals("status")) {
				if (BasicExecutor.hasPermission("citizens.healer.status",
						sender)) {
					displayStatus(player, npc);
				} else {
					player.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 1 && args[0].equals("level-up")) {
				if (BasicExecutor
						.hasPermission("citizens.healer.level", sender)) {
					levelUp(player, npc);
				} else {
					player.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			HealerPropertyPool.saveState(npc);
		}
		return returnval;
	}

	private void displayHealerStrength(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.YELLOW + "Health: " + ChatColor.GREEN
				+ HealerPropertyPool.getStrength(npc.getUID()) + ChatColor.RED
				+ "/" + HealerPropertyPool.getMaxStrength(npc.getUID()));
	}

	private void displayHealerLevel(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.GREEN
				+ HealerPropertyPool.getLevel(npc.getUID()) + ChatColor.RED
				+ "/10");
	}

	private void displayStatus(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN
				+ "========== "
				+ StringUtils.yellowify(npc.getStrippedName()
						+ "'s Healer Status") + " ==========");
		displayHealerStrength(player, npc);
		displayHealerLevel(player, npc);
		player.sendMessage(ChatColor.GREEN + "========================================");
	}

	private void levelUp(Player player, HumanNPC npc) {
		if (EconomyHandler.useEconomy()) {
			int level = HealerPropertyPool.getLevel(npc.getUID());
			int paid = EconomyHandler.pay(Operation.HEALER_LEVEL_UP, player);
			if (paid > 0) {
				if (level < 10) {
					HealerPropertyPool.saveLevel(npc.getUID(), level + 1);
					player.sendMessage(getLevelUpPaidMessage(
							Operation.HEALER_LEVEL_UP, npc, paid, level + 1));
				} else {
					player.sendMessage(StringUtils.yellowify(npc
							.getStrippedName())
							+ " has reached the maximum level.");
				}
			}
		} else {
			player.sendMessage(ChatColor.GRAY
					+ "Your server has not turned economy on for Citizens.");
		}
	}

	private String getLevelUpPaidMessage(Operation op, HumanNPC npc, int paid,
			int level) {
		String message = ChatColor.GREEN
				+ "You have leveled up the healer "
				+ StringUtils.yellowify(npc.getStrippedName())
				+ " to "
				+ StringUtils.yellowify("Level " + level)
				+ " for "
				+ StringUtils.yellowify(EconomyHandler.getPaymentType(op, ""
						+ paid, ChatColor.GREEN)
						+ ".");
		return message;
	}
}