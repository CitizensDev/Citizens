package com.fullwall.Citizens.CommandExecutors;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCManager;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.StringUtils;

public class BasicNPCCommandExecutor implements CommandExecutor {

	private Citizens plugin;
	private String noPermissionsMessage = ChatColor.RED
			+ "You don't have permission to use that command.";
	private String mustBeIngameMessage = "You must use this command ingame";

	public BasicNPCCommandExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length >= 2 && args[0].equals("create")) {
			if (sender instanceof Player) {
				if (hasPermission("citizens.basic.create", sender)) {
					createNPC(args, (Player) sender);
				} else
					sender.sendMessage(noPermissionsMessage);
				return true;
			} else {
				sender.sendMessage(mustBeIngameMessage);
				return true;
			}
		} else if (args.length == 2 && (args[0].equals("move"))) {
			if (sender instanceof Player) {
				if (hasPermission("citizens.general.move", sender)) {
					if (validateName(args[1], sender)) {
						moveNPC(sender, args[1]);
					}
				} else
					sender.sendMessage(noPermissionsMessage);
			} else
				sender.sendMessage(mustBeIngameMessage);
			return true;
		} else if (args.length == 2 && args[0].equals("remove")) {
			if (hasPermission("citizens.general.remove.singular", sender)
					|| hasPermission("citizens.general.remove.all", sender)) {
				if (validateName(args[1], sender)) {
					removeNPC(args, sender);
				} else
					sender.sendMessage(noPermissionsMessage);
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;

		} else if (args.length == 3 && args[0].equals("name")) {
			if (hasPermission("citizens.general.setname", sender)) {
				if (validateName(args[1], sender)) {
					setName(args, sender);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 3
				&& (args[0].equals("colour") || args[0].equals("color"))) {
			if (hasPermission("citizens.general.colour", sender)
					|| hasPermission("citizens.general.color", sender)) {
				if (validateName(args[1], sender)) {
					setColour(args, sender);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length >= 3 && args[0].equals("add")) {
			if (hasPermission("citizens.basic.settext", sender)) {
				if (validateName(args[1], sender)) {
					addText(args, sender);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length >= 3 && args[0].equals("set")) {
			if (hasPermission("citizens.basic.settext", sender)) {
				if (validateName(args[1], sender)) {
					sender.sendMessage(ChatColor.GREEN + args[1]
							+ "'s text was set to " + setText(args));
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2 && (args[0].equals("reset"))) {
			if (hasPermission("citizens.basic.settext", sender)) {
				if (validateName(args[1], sender)) {
					resetText(args, sender);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 3 && (args[0].equals("item"))) {
			if (hasPermission("citizens..general.setitem", sender)) {
				if (validateName(args[1], sender)) {
					setItemInHand(sender, args);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 3
				&& (args[0].equals("torso") || args[0].equals("legs")
						|| args[0].equals("helmet") || args[0].equals("boots"))) {
			if (hasPermission("citizens..general.setitem", sender)) {
				if (validateName(args[1], sender)) {
					setArmor(sender, args);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length >= 2 && args[0].equals("tp")){
			Player p = null;
			if (sender instanceof Player) {
			if (hasPermission("citizens.tp", sender)){
				if(validateName(args[1], sender)){
					p = (Player)sender;
					p.teleportTo((PropertyPool.getLocationFromName(args[1])));
					sender.sendMessage("Teleported you to the NPC named "+args[1]+" Enjoy!");
				}
				}else{
					sender.sendMessage(noPermissionsMessage);
				}
			}else{
			  sender.sendMessage(mustBeIngameMessage);
			}
			return true;
	}else if (command.getName().equals("citizens") && args.length == 1 && (args[0].equals("help"))) {
			if (hasPermission("citizens.help", sender)) {
				sendHelp(sender);
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		}
		return false;
	}

	private void createNPC(String[] args, Player p) {
		String text = "";
		ArrayList<String> texts = new ArrayList<String>();
		if (args.length >= 3) {
			int i = 0;
			for (String s : args) {
				if (i == 2 && !s.isEmpty() && !s.equals(";"))
					text += s;
				if (i > 2 && !s.isEmpty() && !s.equals(";"))
					text += " " + s;
				i += 1;
			}
			texts.add(text);
		}
		plugin.handler.spawnNPC(args[1], p.getLocation());
		plugin.handler.setNPCText(args[1], texts);
		p.sendMessage(ChatColor.GOLD + "The NPC " + args[1] + " was born!");
	}

	private void moveNPC(CommandSender sender, String name) {
		Location loc = PropertyPool.getLocationFromName(name);
		if (loc != null){
			PropertyPool.saveLocation(name, loc);
		}
		plugin.handler.moveNPC(name, ((Player) sender).getLocation());
		sender.sendMessage(name + " is enroute to your location!");
	}

	private void removeNPC(String[] args, CommandSender sender) {
		if (!args[1].equals("all")) {
			plugin.handler.removeNPC(args[1]);
			sender.sendMessage(ChatColor.GRAY + args[1] + " disappeared...");
		} else {
			plugin.handler.removeAllNPCs();
			sender.sendMessage(ChatColor.GRAY + "The NPC(s) disappeared...");
		}
	}

	private void setName(String[] args, CommandSender sender) {
		plugin.handler.setName(args[1], args[2]);
		sender.sendMessage(ChatColor.GREEN + args[1] + "'s name was set to "
				+ args[2]);
		return;
	}

	private void setColour(String[] args, CommandSender sender) {
		if (args[2].indexOf('&') != 0) {
			sender.sendMessage(ChatColor.GRAY + "Use an & to specify "+ args[0] + ".");
		} else {
		plugin.handler.setColour(args[1], args[2]);
		}
	}

	private void addText(String[] args, CommandSender sender) {
		String text = "";
		int i = 0;
		for (String s : args) {
			if (i == 2 && !s.isEmpty() && !s.equals(";")) {
				text += s;
			}
			if (i > 2 && !s.isEmpty() && !s.equals(";")) {
				text += " " + s;
			}
			i += 1;
		}
		plugin.handler.addNPCText(args[1], text);
		sender.sendMessage(ChatColor.GREEN + text + " was added to " + args[1]
				+ "'s text.");
	}

	private String setText(String[] args) {
		String text = "";
		if (args.length >= 3) {
			int i = 0;
			for (String s : args) {
				if (i == 2 && !s.isEmpty() && !s.equals(";"))
					text += s;
				if (i > 2 && !s.isEmpty() && !s.equals(";"))
					text += " " + s;
				i += 1;
			}
		}
		ArrayList<String> texts = new ArrayList<String>();
		texts.add(text);
		plugin.handler.setNPCText(args[1], texts);
		return text;
	}

	private void resetText(String[] args, CommandSender sender) {
		plugin.handler.resetText(args[0]);
		sender.sendMessage(ChatColor.GREEN + args[1] + "'s text was reset!");
	}

	private void setItemInHand(CommandSender sender, String[] args) {
		plugin.handler.setItemInHand(args[1], args[2]);
	}

	private void setArmor(CommandSender sender, String[] args) {
		plugin.handler.setItemInSlot(args);
	}

	private void sendHelp(CommandSender sender) {
		// remove, reset, add, color
		sender.sendMessage("§fCitizens v1.07 Help");
		sender.sendMessage("§b-------------------------------");
		sender.sendMessage("§8/§cnpc §bcreate [name] (text) §e- §acreates an NPC at your location.");
		sender.sendMessage("§8/§cnpc §bset [name] [text] §e- §asets the text of an NPC.");
		sender.sendMessage("§8/§cnpc §badd [name] [text] §e- §aadds text to an NPC.");
		sender.sendMessage("§8/§cnpc §bname [name] [new name] §e- §achanges the name of an NPC.");
		sender.sendMessage("§8/§cnpc §bremove [name|all] §e- §adeletes and despawns the NPC(s).");
		sender.sendMessage("§8/§cnpc §breset [name] §e- §aresets the messages of an NPC.");
		sender.sendMessage("§8/§cnpc §bcolo(u)r [name] [&(code)] §e- §aedits the color of an NPC's name.");
		sender.sendMessage("§8/§cnpc §bitem [name] [id|item name] §e- §asets the in-hand item of an NPC.");
		sender.sendMessage("§8/§cnpc §bhelmet|torso|legs|boots [name] [id|item name] §e- §asets the item slot of an NPC.");
		sender.sendMessage("§8/§cnpc §bmove [name] §e- §amoves an NPC to your location.");
		sender.sendMessage("§8/§cnpc §btp [name] §e- §aTeleports you to the location of an NPC.");
		
		sender.sendMessage("§b-------------------------------");
		sender.sendMessage("§fPlugin made by fullwall and NeonMaster.");

	}

	public boolean validateName(String name, CommandSender sender) {
		if (!plugin.validateName(name)) {
			sender.sendMessage(ChatColor.GRAY + "Couldn't find the NPC called "
					+ name + ".");
			return false;
		}
		return true;
	}

	public boolean hasPermission(String permission, CommandSender sender) {
		return !(sender instanceof Player)
				|| Permission.generic((Player) sender, permission);
	}

}
