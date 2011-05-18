package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;

	public WizardExecutor(Citizens plugin) {
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
			player.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			player.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isWizard()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a wizard yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.hasPermission("citizens.wizard.help", sender)) {
					HelpUtils.sendWizardHelp(sender);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			} else if (args.length == 1
					&& args[0].equalsIgnoreCase("locations")) {
				if (Permission.hasPermission("citizens.wizard.locations",
						sender)) {
					this.dislayLocations(player, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 2 && args[0].contains("addloc")) {
				if (Permission.hasPermission("citizens.wizard.addloc", sender)) {
					if (npc.getWizard().getNrOfLocations() < Constants.wizardMaxLocations) {
						this.addLocation(player, npc, args[1]);
					} else {
						sender.sendMessage(ChatColor.RED + "Wizard "
								+ StringUtils.yellowify(npc.getStrippedName())
								+ " already knows "
								+ Constants.wizardMaxLocations + " locations");
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 2 && args[0].contains("removeloc")) {
				if (Permission.hasPermission("citizens.wizard.removeloc",
						sender)) {
					int type = -1;
					try {
						type = Integer.parseInt(args[1]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED
								+ "ID must be a number, see /wizard locations");
						type = -1;
					}
					if (type != -1) {
						if (type <= npc.getWizard().getNrOfLocations()) {
							this.removeLocation(player, npc, type);
						} else {
							sender.sendMessage(ChatColor.RED
									+ "Wizard "
									+ StringUtils.yellowify(npc
											.getStrippedName())
									+ "doesnt have that location.");
						}
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			PropertyManager.get("wizard").saveState(npc);
		}
		return returnval;
	}

	/**
	 * Removes a teleport location from the wizard.
	 * 
	 * @param player
	 * @param npc
	 * @param parseInt
	 */
	private void removeLocation(Player player, HumanNPC npc, int parseInt) {
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
		npc.getWizard().nextLocation();
		npc.getWizard().setLocations(newLoc);
		player.sendMessage(ChatColor.GREEN + "Wizard "
				+ StringUtils.yellowify(npc.getStrippedName())
				+ " had amnesia and forgot about "
				+ StringUtils.yellowify(removedName));
	}

	/**
	 * Adds a teleport location to the wizard.
	 * 
	 * @param player
	 * @param npc
	 * @param locName
	 */
	private void addLocation(Player player, HumanNPC npc, String locName) {
		player.sendMessage(ChatColor.GREEN + "Added current location to "
				+ StringUtils.yellowify(npc.getStrippedName())
				+ ChatColor.GREEN + " as " + StringUtils.yellowify(locName));
		npc.getWizard().addLocation(player.getLocation(), locName);
	}

	/**
	 * Function for displaying the list of locations that the wizard has.
	 * 
	 * @param player
	 * @param npc
	 */
	private void dislayLocations(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN
				+ "========== "
				+ StringUtils.yellowify(npc.getStrippedName()
						+ "'s Wizard Locations") + " ==========");
		displayWizardLocations(player, npc);
	}

	/**
	 * Followup function for displaying the list of locations that the wizard
	 * has.
	 * 
	 * @param player
	 * @param npc
	 */
	private void displayWizardLocations(Player player, HumanNPC npc) {
		String locations[] = npc.getWizard().getLocations().split(":");
		for (int i = 0; i < locations.length; i++) {
			player.sendMessage(ChatColor.YELLOW + "" + (i + 1)
					+ ChatColor.GREEN + ": "
					+ locations[i].split(",")[0].replace("(", ""));
		}
	}
}