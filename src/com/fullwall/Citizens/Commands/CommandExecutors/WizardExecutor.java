package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCTypes.Wizards.WizardManager.WizardMode;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardExecutor implements CommandExecutor {

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
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			player.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isWizard()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a wizard yet.");
			return true;
		}
		if (args[0].equalsIgnoreCase("help")) {
			if (Permission.canUse(player, npc, "wizard")) {
				HelpUtils.sendWizardHelp(sender);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("mode")) {
			if (Permission.canModify(player, npc, "wizard")) {
				changeMode(player, npc, args[1]);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			returnval = true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("status")) {
			if (Permission.canUse(player, npc, "wizard")) {
				displayStatus(player, npc);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			returnval = true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("locations")) {
			if (Permission.canUse(player, npc, "wizard")) {
				this.displayLocations(player, npc);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			returnval = true;
		} else if (args.length == 2 && args[0].contains("addloc")) {
			if (Permission.canModify(player, npc, "wizard")) {
				if (npc.getWizard().getNumberOfLocations() < Constants.wizardMaxLocations) {
					this.addLocation(player, npc, args[1]);
				} else {
					sender.sendMessage(ChatColor.RED + "Wizard "
							+ StringUtils.wrap(npc.getStrippedName())
							+ " already knows " + Constants.wizardMaxLocations
							+ " locations");
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			returnval = true;
		} else if (args.length == 2 && args[0].contains("removeloc")) {
			if (Permission.canModify(player, npc, "wizard")) {
				int type = -1;
				if (StringUtils.isNumber(args[1])) {
					type = Integer.parseInt(args[1]);
				} else {
					sender.sendMessage(ChatColor.RED
							+ "ID must be a number, see /wizard locations");
					type = -1;
				}
				if (type != -1) {
					if (type <= npc.getWizard().getNumberOfLocations()) {
						this.removeLocation(player, npc, type);
					} else {
						sender.sendMessage(StringUtils.wrap(npc
								.getStrippedName())
								+ " does not have that location.");
					}
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			returnval = true;
		}
		PropertyManager.save(npc);
		return returnval;
	}

	/**
	 * Change the mode of a wizard
	 * 
	 * @param player
	 * @param npc
	 * @param mode
	 */
	private void changeMode(Player player, HumanNPC npc, String mode) {
		WizardMode wizardMode;
		if (WizardMode.parse(mode) != null) {
			wizardMode = WizardMode.parse(mode);
			if (wizardMode != npc.getWizard().getMode()) {
				npc.getWizard().setMode(wizardMode);
				player.sendMessage(StringUtils.wrap(npc.getStrippedName()
						+ "'s")
						+ " mode was set to "
						+ StringUtils.wrap(wizardMode + "") + ".");
			} else {
				player.sendMessage(ChatColor.RED + npc.getStrippedName()
						+ " is already that mode.");
			}
		} else {
			player.sendMessage(ChatColor.RED
					+ "That is not a valid wizard mode.");
		}
	}

	/**
	 * Display the status of a wizard (total mana and current mode)
	 * 
	 * @param player
	 * @param npc
	 */
	private void displayStatus(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.BLUE + "========== " + ChatColor.GOLD
				+ npc.getStrippedName() + "'s Wizard Status" + ChatColor.BLUE
				+ " ==========");
		player.sendMessage(ChatColor.BLUE + "Mode: " + ChatColor.GOLD
				+ npc.getWizard().getMode());
		player.sendMessage(ChatColor.BLUE + "Mana: " + ChatColor.GOLD
				+ npc.getWizard().getMana());
	}

	/**
	 * Adds a teleport location to the wizard.
	 * 
	 * @param player
	 * @param npc
	 * @param locName
	 */
	private void addLocation(Player player, HumanNPC npc, String locName) {
		if (npc.getWizard().getMode() == WizardMode.TELEPORT) {
			player.sendMessage(ChatColor.GREEN + "Added current location to "
					+ StringUtils.wrap(npc.getStrippedName()) + ChatColor.GREEN
					+ " as " + StringUtils.wrap(locName) + ".");
			npc.getWizard().addLocation(player.getLocation(), locName);
		} else {
			player.sendMessage(ChatColor.RED + npc.getStrippedName()
					+ " cannot perform that action in this mode.");
		}
	}

	/**
	 * Removes a teleport location from the wizard.
	 * 
	 * @param player
	 * @param npc
	 * @param parseInt
	 */
	private void removeLocation(Player player, HumanNPC npc, int parseInt) {
		if (npc.getWizard().getMode() == WizardMode.TELEPORT) {
			String locations[] = npc.getWizard().getLocations().split(":");
			String newLoc = "";
			String removedName = "";
			for (int i = 0; i < locations.length; i++) {
				if (i + 1 != parseInt) {
					newLoc = newLoc + locations[i];
				} else {
					removedName = locations[i].split(",")[0].replace("(", "");
				}
			}
			npc.getWizard().cycle(npc, WizardMode.TELEPORT);
			npc.getWizard().setLocations(newLoc);
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has amnesia and forgot about "
					+ StringUtils.wrap(removedName) + ".");
		} else {
			player.sendMessage(ChatColor.RED + npc.getStrippedName()
					+ " cannot perform that action in this mode.");
		}
	}

	/**
	 * Display the list of locations that the wizard has.
	 * 
	 * @param player
	 * @param npc
	 */
	private void displayLocations(Player player, HumanNPC npc) {
		if (npc.getWizard().getMode() == WizardMode.TELEPORT) {
			player.sendMessage(ChatColor.GREEN
					+ "========== "
					+ StringUtils.wrap(npc.getStrippedName()
							+ "'s Wizard Locations") + " ==========");
			String locations[] = npc.getWizard().getLocations().split(":");
			for (int i = 0; i < locations.length; i++) {
				player.sendMessage(ChatColor.YELLOW + "" + (i + 1)
						+ ChatColor.GREEN + ": "
						+ locations[i].split(",")[0].replace("(", ""));
			}
		} else {
			player.sendMessage(ChatColor.RED + npc.getStrippedName()
					+ " cannot perform that action in this mode.");
		}
	}
}