package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.Citizens.Utils.WizardPropertyPool;
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
		if (!npc.isWizard()) {
			player.sendMessage(ChatColor.RED + "Your NPC isn't a wizard yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equals("status")) {
				if (BasicExecutor.hasPermission("citizens.wizard.status",
						sender)) {
					this.displayStatus(player, npc);
				} else {
					player.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}  
			
			else if (args.length == 1 && args[0].equals("help")) {
				if (BasicExecutor.hasPermission("citizens.wizard.help", sender)) {
					HelpUtils.sendWizardHelp(sender);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			
			else if (args.length == 1 && args[0].equals("locations")) {
				if (BasicExecutor.hasPermission("citizens.wizard.locations", sender)) {
					this.dislayLocations(player, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			
			else if (args.length == 2 && args[0].equals("addlocation")) {
				if (BasicExecutor.hasPermission("citizens.wizard.addlocation", sender)) {
					if(npc.getWizard().getNrOfLocations() < Citizens.wizardMaxLocations){
						this.addLocation(player, npc, args[1]);
					}else{
						sender.sendMessage(ChatColor.RED + "This wizard already knows " + Citizens.wizardMaxLocations + " locations");
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			WizardPropertyPool.saveState(npc);
		}
		return returnval;
	}
	
	private void addLocation(Player player, HumanNPC npc, String locName) {
		player.sendMessage(ChatColor.GREEN + "Added current location to " + StringUtils.yellowify(npc.getStrippedName()) + ChatColor.GREEN + " as " + StringUtils.yellowify(locName));
		npc.getWizard().addLocation(player.getLocation(),locName);
	}

	private void displayStatus(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN
				+ "========== "
				+ StringUtils.yellowify(npc.getStrippedName()
						+ "'s Wizard Status") + " ==========");
		
	}

	private void dislayLocations(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN
				+ "========== "
				+ StringUtils.yellowify(npc.getStrippedName()
						+ "'s Wizard Locations") + " ==========");
		displayWizardLocations(player, npc);
	}

	@SuppressWarnings("unused")
	private void displayWizardLocations(Player player, HumanNPC npc) {
		String locations = npc.getWizard().getLocations();
	}
}