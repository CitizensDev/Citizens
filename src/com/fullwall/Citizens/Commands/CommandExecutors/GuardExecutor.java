package com.fullwall.Citizens.Commands.CommandExecutors;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
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
			if (npc.getGuard().isBouncer()) {
				if (args.length == 2 && args[0].equalsIgnoreCase("blacklist")) {
					if (Permission.hasPermission(
							"citizens.guard.bouncer.blacklist", sender)) {
						addMobToBlacklist(player, npc, args[1]);
					} else {
						sender.sendMessage(MessageUtils.noPermissionsMessage);
					}
					returnval = true;
				} else if (args.length == 1
						&& args[0].equalsIgnoreCase("blacklist")) {
					if (Permission.hasPermission(
							"citizens.guard.bouncer.blacklist", sender)) {
						displayBlacklist(player, npc);
					} else {
						sender.sendMessage(MessageUtils.noPermissionsMessage);
					}
					returnval = true;
				} else if (args.length == 2
						&& args[0].equalsIgnoreCase("allow")) {
					if (Permission.hasPermission(
							"citizens.guard.bouncer.allow", sender)) {
						addPlayerToWhitelist(player, npc, args[1]);
					} else {
						sender.sendMessage(MessageUtils.noPermissionsMessage);
					}
					returnval = true;
				} else if (args.length == 1
						&& args[0].equalsIgnoreCase("allow")) {
					if (Permission.hasPermission(
							"citizens.guard.bouncer.allow", sender)) {
						displayWhitelist(player, npc);
					} else {
						sender.sendMessage(MessageUtils.noPermissionsMessage);
					}
					returnval = true;
				} else if (args.length == 2
						&& args[0].equalsIgnoreCase("radius")) {
					if (Permission.hasPermission(
							"citizens.guard.bouncer.radius", sender)) {
						setProtectionRadius(player, npc, args[1]);
					} else {
						sender.sendMessage(MessageUtils.noPermissionsMessage);
					}
					returnval = true;
				}
			} else if (npc.getGuard().isBodyguard()) {
				// TODO bodyguard commands here
			} else {
				sender.sendMessage(ChatColor.RED
						+ "That guard isn't the correct type, so you cannot perform this command.");
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
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is now a bodyguard.");
			if (npc.getGuard().isBouncer()) {
				npc.getGuard().setBouncer(false);
			}
		} else {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
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
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is now a bouncer.");
			if (npc.getGuard().isBodyguard()) {
				npc.getGuard().setBodyguard(false);
			}
		} else {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is already a bouncer.");
		}
	}

	/**
	 * Add a mob to a bouncer's blacklist
	 * 
	 * @param player
	 * @param npc
	 * @param mob
	 */
	private void addMobToBlacklist(Player player, HumanNPC npc, String mob) {
		if (npc.getGuard().getMobBlacklist().contains(mob.toLowerCase())) {
			player.sendMessage(ChatColor.RED
					+ "That mob is already blacklisted.");
		} else if (CreatureType.fromName(mob) != null
				|| Bukkit.getServer().getPlayer(mob) != null) {
			npc.getGuard().addMobToBlacklist(mob);
			player.sendMessage(ChatColor.GREEN + "You added the mob type "
					+ StringUtils.wrap(mob) + " to "
					+ StringUtils.wrap(npc.getStrippedName() + "'s")
					+ " blacklist.");
		} else if (mob.equalsIgnoreCase("all")) {
			npc.getGuard().addMobToBlacklist(mob);
			player.sendMessage(ChatColor.GREEN + "You added all mobs to "
					+ StringUtils.wrap(npc.getStrippedName() + "'s")
					+ " blacklist.");
		} else {
			player.sendMessage(ChatColor.RED + "Invalid mob type.");
		}
	}

	/**
	 * Add a player to a bouncer's whitelist
	 * 
	 * @param player
	 * @param npc
	 * @param mob
	 */
	private void addPlayerToWhitelist(Player player, HumanNPC npc,
			String allowed) {
		if (npc.getGuard().getMobBlacklist().contains(allowed.toLowerCase())) {
			player.sendMessage(ChatColor.RED
					+ "That player is already whitelisted.");
		} else {
			npc.getGuard().addPlayerToWhitelist(allowed);
			player.sendMessage(ChatColor.GREEN + "You added "
					+ StringUtils.wrap(allowed) + " to "
					+ StringUtils.wrap(npc.getStrippedName() + "'s")
					+ " whitelist.");
		}
	}

	/**
	 * Set the radius of a bouncer's protection zone
	 * 
	 * @param player
	 * @param npc
	 * @param radius
	 */
	private void setProtectionRadius(Player player, HumanNPC npc, String radius) {
		try {
			npc.getGuard().setProtectionRadius(Double.parseDouble(radius));
			player.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s")
					+ " protection radius has been set to "
					+ StringUtils.wrap(radius) + ".");
		} catch (NumberFormatException ex) {
			player.sendMessage(ChatColor.RED + "That is not a number.");
		}
	}

	// TODO merge display functions
	/**
	 * Display the mobs blacklisted by a bouncer
	 * 
	 * @param player
	 * @param npc
	 */
	private void displayBlacklist(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN
				+ "========== "
				+ StringUtils.wrap(npc.getStrippedName()
						+ "'s Blacklisted Mobs") + " ==========");
		List<String> list = npc.getGuard().getMobBlacklist();
		if (list.isEmpty()) {
			player.sendMessage(ChatColor.RED + "No mobs blacklisted");
		} else {
			for (int x = 0; x < list.size(); x++) {
				player.sendMessage(ChatColor.RED
						+ list.get(x).replace("_", " "));
			}
		}
	}

	/**
	 * Display the players whitelist by a bouncer
	 * 
	 * @param player
	 * @param npc
	 */
	private void displayWhitelist(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN
				+ "========== "
				+ StringUtils.wrap(npc.getStrippedName()
						+ "'s Whitelisted Players") + " ==========");
		List<String> list = npc.getGuard().getWhitelist();
		if (list.isEmpty()) {
			player.sendMessage(ChatColor.RED + "No players whitelisted.");
		} else {
			for (int x = 0; x < list.size(); x++) {
				player.sendMessage(ChatColor.RED + list.get(x));
			}
		}
	}
}