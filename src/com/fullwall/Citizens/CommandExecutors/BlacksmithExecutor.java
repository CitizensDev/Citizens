package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.BlacksmithPropertyPool;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;

	public BlacksmithExecutor(Citizens plugin) {
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
			npc = NPCManager
					.getNPC(NPCManager.NPCSelected.get(player.getName()));
		} else {
			sender.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			sender.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isBlacksmith()) {
			sender.sendMessage(ChatColor.RED
					+ "Your NPC isn't a blacksmith yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equals("help")) {
				if (Permission.hasPermission("citizens.blacksmith.create",
						sender)) {
					HelpUtils.sendBlacksmithHelp(player);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 2 && args[0].equals("repairarmor")) {
				if (Permission.hasPermission("citizens.blacksmith.repair",
						sender)) {
					repairArmor(player, npc, args[1]);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			BlacksmithPropertyPool.saveState(npc);
		}
		return returnval;
	}

	/**
	 * Repair a player's armor
	 * 
	 * @param player
	 * @param npc
	 * @param armor
	 */
	private void repairArmor(Player player, HumanNPC npc, String armor) {
		PlayerInventory inv = player.getInventory();
		if (armor.equals("helmet") || armor.equals("cap")) {
			ItemStack helmet = inv.getHelmet();
			if (helmet.getDurability() > 0) {
				helmet.setDurability((short) 0);
				repairMessage(player, npc, armor, false);
			} else {
				player.sendMessage(ChatColor.RED
						+ "Your helmet is already fully repaired.");
			}
		} else if (armor.equals("chestplate") || armor.equals("torso")
				|| armor.equals("tunic")) {
			ItemStack chestplate = inv.getChestplate();
			if (chestplate.getDurability() > 0) {
				chestplate.setDurability((short) 0);
				repairMessage(player, npc, armor, false);
			} else {
				player.sendMessage(ChatColor.RED
						+ "Your chestplate is already fully repaired.");
			}
		} else if (armor.equals("leggings") || armor.equals("pants")) {
			ItemStack leggings = inv.getLeggings();
			if (leggings.getDurability() > 0) {
				leggings.setDurability((short) 0);
				repairMessage(player, npc, armor, true);
			} else {
				player.sendMessage(ChatColor.RED
						+ "Your leggings are already fully repaired.");
			}
		} else if (armor.equals("boots") || armor.equals("shoes")) {
			ItemStack boots = inv.getBoots();
			if (boots.getDurability() > 0) {
				boots.setDurability((short) 0);
				repairMessage(player, npc, armor, true);
			} else {
				player.sendMessage(ChatColor.RED
						+ "Your boots are already fully repaired.");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Invalid armor type.");
		}
	}

	/**
	 * Message to be sent to a player when their armor is repaired
	 * 
	 * @param player
	 * @param npc
	 * @param armor
	 * @param plural
	 */
	private void repairMessage(Player player, HumanNPC npc, String armor,
			boolean plural) {
		String msg = "";
		msg = ChatColor.GREEN + ("Your ") + StringUtils.yellowify(armor + " ");
		if (plural) {
			msg += "have been repaired by ";
		} else {
			msg += "has been repaired by ";
		}
		msg += StringUtils.yellowify(npc.getStrippedName()) + ".";
		player.sendMessage(msg);
	}
}