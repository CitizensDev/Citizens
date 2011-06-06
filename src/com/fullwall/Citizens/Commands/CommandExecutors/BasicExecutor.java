package com.fullwall.Citizens.Commands.CommandExecutors;

import java.util.ArrayDeque;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicExecutor implements CommandExecutor {

	private final Citizens plugin;

	public BasicExecutor(Citizens plugin) {
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
		if (NPCManager.validateSelected(player)) {
			npc = NPCManager.get(NPCManager.selectedNPCs.get(player.getName()));
		}
		if (args.length >= 2 && args[0].equalsIgnoreCase("create")) {
			if (Permission.canCreate(player, "basic")) {
				if (!EconomyHandler.useEconomy()
						|| EconomyHandler.canBuy(Operation.BASIC_CREATION,
								player)) {
					create(args, player);
				} else if (EconomyHandler.useEconomy()) {
					sender.sendMessage(MessageUtils.getNoMoneyMessage(
							Operation.BASIC_CREATION, player));
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && (args[0].equalsIgnoreCase("move"))) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					move(player, npc.getName(), npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if ((args.length == 1 || args.length == 2)
				&& args[0].equalsIgnoreCase("remove")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					remove(args, sender, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("name")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					rename(args[1], sender, npc);
					NPCManager.selectedNPCs.remove(player.getName());
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2
				&& (args[0].equalsIgnoreCase("colour") || args[0]
						.equalsIgnoreCase("color"))) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					setColour(args, player, npc);
				} else {
					sender.sendMessage(MessageUtils.notOwnerMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					addText(args, sender, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length >= 2 && args[0].equalsIgnoreCase("set")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					setText(args, sender, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					resetText(args, sender, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("item")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					setItemInHand(args[1], sender, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2
				&& (args[0].equalsIgnoreCase("torso")
						|| args[0].startsWith("leg")
						|| args[0].startsWith("helm") || args[0]
						.startsWith("boot"))) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					setArmor(args, sender, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length >= 1 && args[0].equalsIgnoreCase("tp")) {
			if (npc != null) {
				if (Permission.canUse(player, npc, "basic")) {
					player.teleport(npc.getNPCData().getLocation());
					sender.sendMessage(ChatColor.GREEN
							+ "Teleported you to the NPC named "
							+ StringUtils.wrap(npc.getStrippedName())
							+ ". Enjoy!");
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("copy")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					copy(npc.getUID(), npc.getName(), player);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("id")) {
			if (npc != null) {
				if (Permission.canUse(player, npc, "basic")) {
					player.sendMessage(ChatColor.GREEN
							+ "The ID of this NPC is "
							+ StringUtils.wrap("" + npc.getUID()) + ".");
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("select")) {
			if (Permission.canUse(player, npc, "basic")) {
				// BUILD CHECK
				if (!Character.isDigit(args[1].charAt(0))) {
					player.sendMessage(ChatColor.RED
							+ "The ID must be a number.");
					return true;
				}
				npc = NPCManager.get(Integer.valueOf(args[1]));
				if (npc == null) {
					sender.sendMessage(ChatColor.RED + "No NPC with the ID "
							+ args[1] + ".");
				} else {
					NPCManager.selectedNPCs.put(player.getName(), npc.getUID());
					player.sendMessage(ChatColor.GREEN
							+ "Selected NPC with ID "
							+ StringUtils.wrap("" + npc.getUID()) + ", name "
							+ StringUtils.wrap(npc.getStrippedName()) + ".");
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("owner")) {
			if (npc != null) {
				if (Permission.canUse(player, npc, "basic")) {
					player.sendMessage(ChatColor.GREEN
							+ "The owner of this NPC is "
							+ StringUtils.wrap(npc.getOwner()) + ".");
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("setowner")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					setOwner(player, npc, args[1]);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2
				&& args[0].equalsIgnoreCase("talkwhenclose")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					changeTalkWhenClose(args[1], player, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2
				&& args[0].equalsIgnoreCase("lookatplayers")) {
			if (npc != null) {
				if (Permission.canModify(player, npc, "basic")) {
					changeLookWhenClose(args[1], player, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if ((command.getName().equalsIgnoreCase("citizens") || command
				.getName().equalsIgnoreCase("npc"))) {
			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.canUse(player, npc, "basic")) {
					HelpUtils.sendHelp(sender, 1);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
				if (Permission.canUse(player, npc, "basic")) {
					int page = Integer.parseInt(args[1]);
					HelpUtils.sendHelp(sender, page);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			}
			return true;

		} else if (command.getName().equalsIgnoreCase("basic")) {
			if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
				if (Permission.canUse(player, npc, "basic")) {
					int page = Integer.parseInt(args[1]);
					HelpUtils.sendBasicHelpPage(sender, page);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.canUse(player, npc, "basic")) {
					HelpUtils.sendBasicHelpPage(sender, 1);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			}

		} else {
			if (args.length >= 2 && args[0].equalsIgnoreCase("move")) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc move");
			} else if (args.length >= 3 && args[0].equalsIgnoreCase("remove")) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc remove OR /npc remove all");
			} else if (args.length >= 3 && args[0].equalsIgnoreCase("name")) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc name [name]");
			} else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("colour") || args[0]
							.equalsIgnoreCase("color"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc color [color]");
			} else if (args.length >= 2 && (args[0].equalsIgnoreCase("reset"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc reset");
			} else if (args.length >= 3 && (args[0].equalsIgnoreCase("item"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc item [id|item name]");
			} else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("torso")
							|| args[0].equalsIgnoreCase("legs")
							|| args[0].equalsIgnoreCase("helmet") || args[0]
							.equalsIgnoreCase("boots"))) {
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc "
						+ args[0] + " [id|item name]");
			} else if (args.length >= 2 && args[0].equalsIgnoreCase("tp")) {
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc tp");
			} else if (args.length >= 2 && (args[0].equalsIgnoreCase("copy"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc copy");
			} else if (args.length >= 2 && (args[0].equalsIgnoreCase("id"))) {
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc id");
			} else if (args.length >= 3 && (args[0].equalsIgnoreCase("select"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc select [id]");
			} else if (args.length >= 2 && (args[0].equalsIgnoreCase("owner"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc owner");
			} else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("setowner"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc setowner [player name]");
			} else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("addowner"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc addowner [player name]");
			} else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("talkwhenclose"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc talkwhenclose [true|false]");
			} else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("lookatplayers"))) {
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc lookatplayers [true|false]");
			} else {
				return false;
			}
		}
		PropertyManager.get("basic").saveState(npc);
		return false;
	}

	/**
	 * Creates an NPC given a string array, containing the name of the NPC and
	 * optional text to be added to it.
	 * 
	 * @param args
	 * @param player
	 */
	private void create(String[] args, Player player) {
		String text = "";
		ArrayDeque<String> texts = new ArrayDeque<String>();
		if (args.length >= 3) {
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
			texts.add(text);
		}
		if (args[1].length() > 15) {
			player.sendMessage(ChatColor.RED
					+ "The name of this NPC will be truncated - max name length is 15.");
		}
		if ((PropertyManager.getBasic().getNPCAmountPerPlayer(player.getName()) < Constants.maxNPCsPerPlayer)
				|| (Constants.maxNPCsPerPlayer == 0)
				|| (Permission.isAdmin(player))) {
			int UID = NPCManager.register(args[1], player.getLocation(),
					player.getName());
			PropertyManager.getBasic().saveNPCAmountPerPlayer(
					player.getName(),
					PropertyManager.getBasic().getNPCAmountPerPlayer(
							player.getName()) + 1);
			NPCManager.setText(UID, texts);

			NPCManager.get(UID).getNPCData().setOwner(player.getName());

			player.sendMessage(ChatColor.GREEN + "The NPC "
					+ StringUtils.wrap(args[1]) + " was born!");
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(Operation.BASIC_CREATION,
						player);
				if (paid > 0) {
					player.sendMessage(MessageUtils.getPaidMessage(
							Operation.BASIC_CREATION, paid, args[1], "", false));
				}
			}
			NPCManager.selectedNPCs.put(player.getName(), UID);
			player.sendMessage(ChatColor.GREEN + "You selected NPC "
					+ StringUtils.wrap(args[1]) + ", ID "
					+ StringUtils.wrap(UID) + ".");
		} else {
			player.sendMessage(ChatColor.GREEN
					+ "You have reached the NPC-creation limit of "
					+ StringUtils.wrap("" + Constants.maxNPCsPerPlayer) + ".");
		}
	}

	/**
	 * Moves the selected NPC to the current location of a player.
	 * 
	 * @param sender
	 * @param name
	 * @param npc
	 */
	private void move(Player player, String name, HumanNPC npc) {
		Location loc = npc.getNPCData().getLocation();
		player.sendMessage(StringUtils.wrap(name)
				+ " is enroute to your location!");
		npc.getNPCData().setLocation(loc);
		npc.teleport(player.getLocation());
	}

	/**
	 * Copies an npc's data and position to another npc.
	 * 
	 * @param npc
	 * @param p
	 */
	private void copy(int UID, String name, Player p) {
		int newUID = NPCManager.register(name, p.getLocation(), p.getName());
		HumanNPC newNPC = NPCManager.get(newUID);
		newNPC.teleport(p.getLocation());

		newNPC.getNPCData().setLocation(p.getLocation());
		PropertyManager.copy(UID, newUID);
		NPCManager.removeForRespawn(newUID);
		NPCManager.register(name, newUID, newNPC.getOwner());
	}

	/**
	 * Removes the selected NPC (can optionally be all NPCs).
	 * 
	 * @param args
	 * @param sender
	 */
	private void remove(String[] args, CommandSender sender, HumanNPC npc) {
		Player p = (Player) sender;
		if (args.length == 2 && args[1].equalsIgnoreCase("all")) {
			plugin.basicNPCHandler.removeAll();
			sender.sendMessage(ChatColor.GRAY + "The NPC(s) disappeared.");
			PropertyManager.getBasic().locations.setInt("currentID", 0);
			PropertyManager.getBasic().locations.removeKey("list");
		} else {
			plugin.basicNPCHandler.remove(npc.getUID());
			sender.sendMessage(ChatColor.GRAY + npc.getName() + " disappeared.");
		}
		NPCManager.selectedNPCs.remove(p.getName());
	}

	/**
	 * Renames the selected NPC.
	 * 
	 * @param name
	 * @param sender
	 */
	private void rename(String name, CommandSender sender, HumanNPC npc) {
		if (name.length() > 16) {
			sender.sendMessage(ChatColor.RED
					+ "Max name length is 16 - NPC name length will be truncated.");
		}
		plugin.basicNPCHandler.rename(npc.getUID(), name, npc.getOwner());
		sender.sendMessage(ChatColor.GREEN + StringUtils.wrap(npc.getName())
				+ "'s name was set to " + StringUtils.wrap(name) + ".");
		return;
	}

	/**
	 * Sets the colour of the selected NPC's name.
	 * 
	 * @param args
	 * @param player
	 * @param npc
	 */
	private void setColour(String[] args, Player player, HumanNPC npc) {
		if (!args[1].substring(0, 1).equals("&")) {
			player.sendMessage(ChatColor.RED + "Use an & to specify " + args[0]
					+ ".");
		} else if (args[1].length() != 2) {
			player.sendMessage(ChatColor.GRAY
					+ "Use the format &(code). Example - &f = white.");
		} else {
			int colour = 0xf;
			try {
				colour = Integer.parseInt(args[1].substring(1, 2));
			} catch (NumberFormatException ex) {
				try {
					colour = Integer.parseInt(args[1].substring(1, 2), 16);
				} catch (NumberFormatException e) {
				}
			}
			npc.getNPCData().setColour(colour);
			plugin.basicNPCHandler.setColour(npc.getUID(), npc.getOwner());
			player.sendMessage(StringUtils.wrapFull("{" + npc.getName()
					+ "}'s name " + args[0] + " is now "
					+ args[1].replace("&", "§") + "this}."));
		}
	}

	/**
	 * Resets the selected NPC's text to a given text.
	 * 
	 * @param args
	 * @param sender
	 * @param npc
	 */
	private void setText(String[] args, CommandSender sender, HumanNPC npc) {
		String text = "";
		if (args.length >= 2) {
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
		}
		ArrayDeque<String> texts = new ArrayDeque<String>();
		texts.add(text);
		NPCManager.setText(npc.getUID(), texts);
		sender.sendMessage(StringUtils.wrapFull("{" + npc.getName()
				+ "}'s text was set to {" + text + "}."));

	}

	/**
	 * Adds text to the selected NPC's text, picked randomly on right click.
	 * 
	 * @param args
	 * @param sender
	 * @param npc
	 */
	private void addText(String[] args, CommandSender sender, HumanNPC npc) {
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
		plugin.basicNPCHandler.addText(npc.getUID(), text);
		sender.sendMessage(StringUtils.wrap(text) + " was added to "
				+ StringUtils.wrap(npc.getStrippedName() + "'s") + " text.");
	}

	private void setOwner(Player player, HumanNPC npc, String name) {
		player.sendMessage(ChatColor.GREEN + "The owner of "
				+ StringUtils.wrap(npc.getStrippedName()) + " is now "
				+ StringUtils.wrap(name) + ".");
		npc.getNPCData().setOwner(name);
	}

	/**
	 * Clears all of an NPC's text.
	 * 
	 * @param args
	 * @param sender
	 * @param npc
	 */
	private void resetText(String[] args, CommandSender sender, HumanNPC npc) {
		NPCManager.resetText(npc.getUID());
		sender.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s")
				+ " text was reset!");
	}

	/**
	 * Sets the selected npc's item in hand.
	 * 
	 * @param name
	 * @param sender
	 * @param npc
	 */
	private void setItemInHand(String name, CommandSender sender, HumanNPC npc) {
		plugin.basicNPCHandler.setItemInHand((Player) sender, npc, name);
	}

	/**
	 * Sets the armor of a given type of the npc.
	 * 
	 * @param args
	 * @param sender
	 * @param npc
	 */
	private void setArmor(String[] args, CommandSender sender, HumanNPC npc) {
		plugin.basicNPCHandler.setItemInSlot(args, (Player) sender, npc);
	}

	/**
	 * Changes whether the selected npc will talk when a player gets near.
	 * 
	 * @param bool
	 * @param p
	 * @param npc
	 */
	private void changeTalkWhenClose(String bool, Player p, HumanNPC npc) {
		boolean talk = false;
		if (bool.equals("true")) {
			talk = true;
		}
		npc.getNPCData().setTalkClose(talk);
		if (talk) {
			p.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will now talk to nearby players.");
		} else if (!talk) {
			p.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will stop talking to nearby players.");
		}
	}

	/**
	 * Changes whether the selected npc will look at nearby players.
	 * 
	 * @param bool
	 * @param p
	 * @param npc
	 */
	private void changeLookWhenClose(String bool, Player p, HumanNPC npc) {
		boolean look = false;
		if (bool.equals("true")) {
			look = true;
		}
		npc.getNPCData().setLookClose(look);
		if (look) {
			p.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will now look at players.");
		} else if (!look) {
			p.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will stop looking at players.");
		}
	}

	/**
	 * Checks whether a UID has an npc attached to it.
	 * 
	 * @param UID
	 * @param sender
	 * @return
	 */
	public boolean validateUID(int UID, CommandSender sender) {
		if (!plugin.validateUID(UID)) {
			sender.sendMessage(ChatColor.GRAY
					+ "Couldn't find the NPC with id " + UID + ".");
			return false;
		}
		return true;
	}
}