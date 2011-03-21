package com.fullwall.Citizens.CommandExecutors;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCDataManager;
import com.fullwall.Citizens.NPCManager;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicNPCCommandExecutor implements CommandExecutor {

	private Citizens plugin;
	private String noPermissionsMessage = ChatColor.RED
			+ "You don't have permission to use that command.";
	private String notEnoughMoneyMessage = ChatColor.GRAY
			+ "You don't have enough money to do that.";
	private String mustBeIngameMessage = "You must use this command ingame";
	private String mustHaveNPCSelectedMessage = ChatColor.GRAY
			+ "You must have an NPC selected (right click).";

	public BasicNPCCommandExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (args.length >= 2 && args[0].equals("create")) {
			if (sender instanceof Player) {
				if (hasPermission("citizens.basic.create", sender)) {
					if (EconomyHandler.canBuy(Operation.BASIC_NPC_CREATE,
							(Player) sender)) {
						createNPC(args, (Player) sender);
					} else
						sender.sendMessage(notEnoughMoneyMessage);
				} else
					sender.sendMessage(noPermissionsMessage);
				return true;
			} else {
				sender.sendMessage(mustBeIngameMessage);
				return true;
			}
		} else if (args.length == 1 && (args[0].equals("move"))) {
			if (sender instanceof Player) {
				if (hasPermission("citizens.general.move", sender)) {
					if (validateSelected((Player) sender)) {
						Player p = (Player) sender;
						HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected
								.get(p.getName()));
						moveNPC(sender, n.getName(),
								Integer.valueOf(NPCManager.NPCSelected.get(p
										.getName())));
					} else {
						sender.sendMessage(mustHaveNPCSelectedMessage);
					}
				} else
					sender.sendMessage(noPermissionsMessage);
			} else
				sender.sendMessage(mustBeIngameMessage);
			return true;
		} else if ((args.length == 1 || args.length == 2)
				&& args[0].equals("remove")) {
			if (hasPermission("citizens.general.remove.singular", sender)
					|| hasPermission("citizens.general.remove.all", sender)) {
				if (validateSelected((Player) sender)
						|| (args.length == 2 && args[1].equals("all"))) {
					removeNPC(args, sender);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;

		} else if (args.length == 2 && args[0].equals("name")) {
			if (hasPermission("citizens.general.setname", sender)) {
				if (validateSelected((Player) sender)) {
					setName(args, sender);
					Player p = (Player) sender;
					NPCManager.NPCSelected.remove(p.getName());
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2
				&& (args[0].equals("colour") || args[0].equals("color"))) {
			if (hasPermission("citizens.general.colour", sender)
					|| hasPermission("citizens.general.color", sender)) {
				if (validateSelected((Player) sender)) {
					setColour(args, sender);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length >= 2 && args[0].equals("add")) {
			if (hasPermission("citizens.basic.settext", sender)) {
				if (validateSelected((Player) sender)) {
					addText(args, sender);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length >= 2 && args[0].equals("set")) {
			if (hasPermission("citizens.basic.settext", sender)) {
				if (validateSelected((Player) sender)) {
					setText(args, sender);

				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2 && (args[0].equals("reset"))) {
			if (hasPermission("citizens.basic.settext", sender)) {
				if (validateSelected((Player) sender)) {
					resetText(args, sender);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2 && (args[0].equals("item"))) {
			if (hasPermission("citizens.general.setitem", sender)) {
				if (validateSelected((Player) sender)) {
					setItemInHand(sender, args);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2
				&& (args[0].equals("torso") || args[0].equals("legs")
						|| args[0].equals("helmet") || args[0].equals("boots"))) {
			if (sender instanceof Player) {
				if (hasPermission("citizens.general.setitem", sender)) {
					if (validateSelected((Player) sender)) {
						setArmor(sender, args);
					} else {
						sender.sendMessage(mustHaveNPCSelectedMessage);
					}
				} else
					sender.sendMessage(noPermissionsMessage);
			}
			return true;
		} else if (args.length >= 1 && args[0].equals("tp")) {
			Player p = null;
			if (sender instanceof Player) {
				if (hasPermission("citizens.tp", sender)) {
					if (validateSelected((Player) sender)) {
						p = (Player) sender;
						HumanNPC npc = NPCManager.getNPC(NPCManager.NPCSelected
								.get(p.getName()));
						p.teleportTo((PropertyPool.getLocationFromName(npc
								.getUID())));
						sender.sendMessage("Teleported you to the NPC named "
								+ npc.getName() + ". Enjoy!");
					} else {
						sender.sendMessage(mustHaveNPCSelectedMessage);
					}
				} else {
					sender.sendMessage(noPermissionsMessage);
				}
			} else {
				sender.sendMessage(mustBeIngameMessage);
			}
			return true;
		} else if ((command.getName().equals("citizens") || command.getName()
				.equals("npc")) && args.length == 1 && (args[0].equals("help"))) {
			if (hasPermission("citizens.help", sender)) {
				sendHelp(sender);
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if ((command.getName().equals("citizens") || command.getName()
				.equals("npc")) && args.length == 2 && (args[0].equals("help"))) {
			if (hasPermission("citizens.help", sender)) {
				sendHelpPage(sender, args[1]);
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 1 && (args[0].equals("copy"))) {
			if (hasPermission("citizens.general.copy", sender)) {
				if (validateSelected((Player) sender)) {
					Player p = (Player) sender;
					HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p
							.getName()));
					copyNPC(n, p);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 1 && (args[0].equals("getid"))) {
			if (hasPermission("citizens.general.getid", sender)) {
				if (validateSelected((Player) sender)) {
					Player p = (Player) sender;
					HumanNPC npc = NPCManager.getNPC(NPCManager.NPCSelected
							.get(p.getName()));
					p.sendMessage("The ID of this NPC is: " + npc.getUID());
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2 && (args[0].equals("select"))) {
			if (hasPermission("citizens.general.select", sender)) {
				Player p = (Player) sender;
				// BUILD CHECK
				if (!Character.isDigit(args[1].charAt(0))) {
					p.sendMessage(ChatColor.RED + "The ID must be a number.");
					return true;
				}
				HumanNPC npc = NPCManager.getNPC(Integer.valueOf(args[1]));
				if (npc == null) {
					sender.sendMessage(ChatColor.RED + "No NPC with the ID "
							+ args[1] + ".");
				} else {
					NPCManager.NPCSelected.put(p.getName(), npc.getUID());
					p.sendMessage("Selected NPC with ID: " + npc.getUID()
							+ " Name: " + npc.getName() + ".");
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 1 && (args[0].equals("getowner"))) {
			if (hasPermission("citizens.general.getowner", sender)) {
				if (validateSelected((Player) sender)) {
					Player p = (Player) sender;
					HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p
							.getName()));
					p.sendMessage("The owner of this NPC is: §c"
							+ PropertyPool.getNPCOwner(n.getUID()) + ".");
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2 && (args[0].equals("setowner"))) {
			if (hasPermission("citizens.general.setowner", sender)) {
				if (validateSelected((Player) sender)) {
					Player p = (Player) sender;
					HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p
							.getName()));
					PropertyPool.setNPCOwner(n.getUID(), args[1]);
					p.sendMessage("The owner of NPC: §c" + n.getName()
							+ "§f is now: §c" + args[1] + ".");
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2 && (args[0].equals("talkwhenclose"))) {
			if (hasPermission("citizens.general.talkwhenclose", sender)) {
				if (validateSelected((Player) sender)) {
					Player p = (Player) sender;
					changeTalkWhenClose(p, args[1]);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
			} else
				sender.sendMessage(noPermissionsMessage);
			return true;
		} else if (args.length == 2 && (args[0].equals("lookatplayers"))) {
			if (hasPermission("citizens.general.lookatplayers", sender)) {
				if (validateSelected((Player) sender)) {
					Player p = (Player) sender;
					changeLookWhenClose(p, args[1]);
				} else {
					sender.sendMessage(mustHaveNPCSelectedMessage);
				}
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
		int UID = plugin.handler.spawnNPC(args[1], p.getLocation());
		plugin.handler.setNPCText(UID, texts);
		plugin.handler.setOwner(UID, p.getName());
		p.sendMessage(ChatColor.GOLD + "The NPC " + args[1] + " was born!");
		int paid = EconomyHandler.pay(Operation.BASIC_NPC_CREATE, p);
		if (paid > 0)
			p.sendMessage(ChatColor.GREEN + "Paid " + paid + " "
					+ EconomyHandler.getPaymentType(Operation.BASIC_NPC_CREATE)
					+ " for " + args[1] + ".");
	}

	private void moveNPC(CommandSender sender, String name, int UID) {
		Location loc = PropertyPool.getLocationFromID(UID);
		if (loc != null) {
			PropertyPool.saveLocation(name, loc, UID);
		}
		plugin.handler.moveNPC(UID, ((Player) sender).getLocation());
		sender.sendMessage(ChatColor.GREEN + name
				+ " is enroute to your location!");
	}

	private void removeNPC(String[] args, CommandSender sender) {
		Player p = (Player) sender;
		if (args.length == 2 && args[1].equals("all")) {
			plugin.handler.removeAllNPCs();
			sender.sendMessage(ChatColor.GRAY + "The NPC(s) disappeared.");
			PropertyPool.locations.setInt("currentID", 0);
			PropertyPool.locations.removeKey("list");
		} else {
			HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p
					.getName()));
			plugin.handler.removeNPC(Integer.valueOf(NPCManager.NPCSelected
					.get(p.getName())));
			sender.sendMessage(ChatColor.GRAY + n.getName() + " disappeared.");
		}
		NPCManager.NPCSelected.remove(p.getName());
	}

	private void setName(String[] args, CommandSender sender) {
		Player p = (Player) sender;
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		plugin.handler.setName(n.getUID(), args[1]);
		sender.sendMessage(ChatColor.GREEN + n.getName()
				+ "'s name was set to " + args[1] + ".");
		return;
	}

	private void setColour(String[] args, CommandSender sender) {
		if (args[1].indexOf('&') != 0) {
			sender.sendMessage(ChatColor.GRAY + "Use an & to specify "
					+ args[0] + ".");
		} else {
			Player p = (Player) sender;
			HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p
					.getName()));
			plugin.handler.setColour(n.getUID(), args[1]);
		}
	}

	private void addText(String[] args, CommandSender sender) {
		Player p = (Player) sender;
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		String text = "";
		int i = 0;
		for (String s : args) {
			if (i == 1 && !s.isEmpty() && !s.equals(";")) {
				text += s;
			}
			if (i > 1 && !s.isEmpty() && !s.equals(";")) {
				text += " " + s;
			}
			i += 1;
		}
		plugin.handler.addNPCText(n.getUID(), text);
		sender.sendMessage(ChatColor.GREEN + text + " was added to "
				+ n.getName() + "'s text.");
	}

	private void setText(String[] args, CommandSender sender) {
		Player p = (Player) sender;
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		String text = "";
		if (args.length >= 2) {
			int i = 0;
			for (String s : args) {
				if (i == 1 && !s.isEmpty() && !s.equals(";"))
					text += s;
				if (i > 1 && !s.isEmpty() && !s.equals(";"))
					text += " " + s;
				i += 1;
			}
		}
		ArrayList<String> texts = new ArrayList<String>();
		texts.add(text);
		plugin.handler.setNPCText(n.getUID(), texts);
		sender.sendMessage(ChatColor.GREEN + n.getName()
				+ "'s text was set to " + text);

	}

	private void resetText(String[] args, CommandSender sender) {
		Player p = (Player) sender;
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		plugin.handler.resetText(n.getUID());
		sender.sendMessage(ChatColor.GREEN + n.getName() + "'s text was reset!");
	}

	private void setItemInHand(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		plugin.handler.setItemInHand(n.getUID(), args[1]);
	}

	private void setArmor(CommandSender sender, String[] args) {
		plugin.handler.setItemInSlot((Player) sender, args);
	}

	private void copyNPC(HumanNPC NPC, Player p) {
		ArrayList<String> texts = PropertyPool.getText(NPC.getUID());
		String colour = PropertyPool.getColour(NPC.getUID());
		ArrayList<Integer> items = PropertyPool.getItemsFromFile(NPC.getUID());
		int newUID = plugin.handler.spawnNPC(NPC.getName(), p.getLocation());
		HumanNPC newNPC = NPCManager.getNPC(newUID);
		PropertyPool.saveColour(newUID, colour);
		PropertyPool.saveText(newUID, texts);
		PropertyPool.saveItems(newUID, items);
		NPCDataManager.addItems(newNPC, items);
	}

	private void changeTalkWhenClose(Player p, String bool) {
		boolean talk = false;
		if (bool.equals("true"))
			talk = true;
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		PropertyPool.setNPCTalkWhenClose(n.getUID(), talk);
		if (talk == true)
			p.sendMessage(ChatColor.GREEN + "The NPC: " + n.getName()
					+ " will now talk to nearby players.");
		else if (talk == false)
			p.sendMessage(ChatColor.GREEN + "The NPC: " + n.getName()
					+ " will not talk to nearby players.");
	}

	private void changeLookWhenClose(Player p, String bool) {
		boolean look = false;
		if (bool.equals("true"))
			look = true;
		HumanNPC n = NPCManager.getNPC(NPCManager.NPCSelected.get(p.getName()));
		PropertyPool.setNPCLookWhenClose(n.getUID(), look);
		if (look == true)
			p.sendMessage(ChatColor.GREEN + "The NPC: " + n.getName()
					+ " will now look at players.");
		else if (look == false)
			p.sendMessage(ChatColor.GREEN + "The NPC: " + n.getName()
					+ " will not look at players.");
	}

	private void sendHelp(CommandSender sender) {
		// remove, reset, add, color
		sender.sendMessage("§fCitizens v1.07 Help");
		sender.sendMessage("§b-------------------------------");
		sender.sendMessage("§8/§cnpc §bcreate (text) §e- §acreates an NPC at your location.");
		sender.sendMessage("§8/§cnpc §bset [text] §e- §asets the text of an NPC.");
		sender.sendMessage("§8/§cnpc §badd [text] §e- §aadds text to an NPC.");
		sender.sendMessage("§fUse the command /citizens help [page] for more info.");

	}

	private void sendHelpPage(CommandSender sender, String page) {
		int pageNum = Integer.valueOf(page);
		switch (pageNum) {
		case 1:
			sender.sendMessage("§fCitizens v1.07 Help");
			sender.sendMessage("§b-------------------------------");
			sender.sendMessage("§8/§cnpc §bcreate (text) §e- §acreates an NPC at your location.");
			sender.sendMessage("§8/§cnpc §bset [text] §e- §asets the text of an NPC.");
			sender.sendMessage("§8/§cnpc §badd [text] §e- §aadds text to an NPC.");
			sender.sendMessage("§fUse the command /citizens help [page] for more info.");
			break;
		case 2:
			sender.sendMessage("§8/§cnpc §bname [new name] §e- §achanges the name of an NPC.");
			sender.sendMessage("§8/§cnpc §bremove [all] §e- §adeletes and despawns the NPC(s).");
			sender.sendMessage("§8/§cnpc §breset §e- §aresets the messages of an NPC.");
			sender.sendMessage("§8/§cnpc §bcolo(u)r [&(code)] §e- §aedits the color of an NPC's name.");
			sender.sendMessage("§8/§cnpc §bitem [id|item name] §e- §asets the in-hand item of an NPC.");
			sender.sendMessage("§8/§cnpc §bhelmet|torso|legs|boots [id|item name] §e- §asets the item slot of an NPC.");
			break;
		case 3:
			sender.sendMessage("§8/§cnpc §bmove §e- §amoves an NPC to your location.");
			sender.sendMessage("§8/§cnpc §btp §e- §ateleports you to the location of an NPC.");
			sender.sendMessage("§8/§cnpc §bcopy §e- §amakes of copy of the NPC on your location.");
			sender.sendMessage("§8/§cnpc §bgetid §e- §agets the ID of the selected NPC.");
			sender.sendMessage("§8/§cnpc §bselect [id] §e- §aselects an NPC with the given ID.");
			sender.sendMessage("§8/§cnpc §bgetowner §e- §agets the owner of the selected NPC.");
			break;
		case 4:
			sender.sendMessage("§8/§cnpc §bsetowner [name] §e- §asets the owner of the selected NPC.");
			sender.sendMessage("§8/§cnpc §btalkwhenclose [true|false] §e- §amake a NPC talk to players.");
			sender.sendMessage("§8/§cnpc §blookatplayers [true|false] §e- §amake a NPC look at players.");
			sender.sendMessage("§b-------------------------------");
			sender.sendMessage("§fPlugin made by fullwall, NeonMaster and TheMPC.");
			break;
		default:
			sender.sendMessage("§fThe number of pages is 4, page: " + pageNum
					+ " is not available.");
			break;
		}
	}

	public boolean validateUID(int UID, CommandSender sender) {
		if (!plugin.validateUID(UID)) {
			sender.sendMessage(ChatColor.GRAY
					+ "Couldn't find the NPC with id " + UID + ".");
			return false;
		}
		return true;
	}

	public boolean validateSelected(Player p) {
		if (NPCManager.NPCSelected.get(p.getName()) != null
				&& !NPCManager.NPCSelected.get(p.getName()).toString()
						.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean hasPermission(String permission, CommandSender sender) {
		return !(sender instanceof Player)
				|| Permission.generic((Player) sender, permission);
	}
}
