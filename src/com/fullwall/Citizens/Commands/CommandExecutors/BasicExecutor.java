package com.fullwall.Citizens.Commands.CommandExecutors;

import java.util.ArrayDeque;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.NPCs.NPCDataManager;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.PageUtils;
import com.fullwall.Citizens.Utils.PageUtils.PageInstance;
import com.fullwall.Citizens.Utils.ServerUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicExecutor implements CommandExecutor {

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
		if (args.length == 0) {
			if (Permission.isAdmin(player)) {
				showInfo(player);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if ((commandLabel.equalsIgnoreCase("npc") || commandLabel
				.equalsIgnoreCase("citizens"))
				&& args.length == 1
				&& args[0].equalsIgnoreCase("help")) {
			HelpUtils.sendHelpPage(sender, 1);
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if (Permission.isAdmin(player)) {
				reload(player);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if ((commandLabel.equalsIgnoreCase("npc") || commandLabel
				.equalsIgnoreCase("citizens"))
				&& args[0].equalsIgnoreCase("help")) {
			if (args.length == 1) {
				HelpUtils.sendHelpPage(sender, 1);
			} else if (args.length == 2) {
				if (StringUtils.isNumber(args[1])) {
					HelpUtils.sendHelpPage(sender, Integer.parseInt(args[1]));
				} else {
					sender.sendMessage(ChatColor.RED + "That's not a number.");
				}
			}
			return true;
		} else if (commandLabel.equalsIgnoreCase("basic")
				&& args[0].equalsIgnoreCase("help")) {
			if (args.length == 1) {
				HelpUtils.sendBasicHelpPage(sender, 1);
			} else if (args.length == 2) {
				if (StringUtils.isNumber(args[1])) {
					HelpUtils.sendBasicHelpPage(sender,
							Integer.parseInt(args[1]));
				} else {
					sender.sendMessage(ChatColor.RED + "That's not a number.");
				}
			}
			return true;
		} else if (args.length >= 2 && args[0].equalsIgnoreCase("create")) {
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

		} else if (args.length == 1 && args[0].equalsIgnoreCase("move")) {
			if (Permission.canModify(player, npc, "basic")) {
				move(player, npc);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args[0].equalsIgnoreCase("moveto")) {
			if (Permission.canModify(player, npc, "basic")) {
				moveTo(player, npc, args);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if ((args.length == 1 || args.length == 2)
				&& args[0].equalsIgnoreCase("remove")) {
			if (Permission.canModify(player, npc, "basic")) {
				remove(player, npc, args);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("name")) {
			if (Permission.canModify(player, npc, "basic")) {
				rename(player, npc, args[1]);
				NPCManager.selectedNPCs.remove(player.getName());
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2
				&& (args[0].equalsIgnoreCase("colour") || args[0]
						.equalsIgnoreCase("color"))) {
			if (Permission.canModify(player, npc, "basic")) {
				setColour(player, npc, args);
			} else {
				sender.sendMessage(MessageUtils.notOwnerMessage);
			}
			return true;

		} else if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
			if (Permission.canModify(player, npc, "basic")) {
				addText(player, npc, args);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length >= 2 && args[0].equalsIgnoreCase("set")) {
			if (Permission.canModify(player, npc, "basic")) {
				setText(player, npc, args);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
			if (Permission.canModify(player, npc, "basic")) {
				resetText(player, npc, args);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("item")) {
			if (Permission.canModify(player, npc, "basic")) {
				setItemInHand(player, npc, args[1]);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2
				&& (args[0].equalsIgnoreCase("torso")
						|| args[0].startsWith("leg")
						|| args[0].startsWith("helm") || args[0]
						.startsWith("boot"))) {
			if (Permission.canModify(player, npc, "basic")) {
				setArmor(player, npc, args);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length >= 1 && args[0].equalsIgnoreCase("tp")) {
			if (npc != null) {
				if (Permission.isAdmin(player)) {
					player.teleport(npc.getNPCData().getLocation());
					sender.sendMessage(ChatColor.GREEN + "Teleported you to "
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
					copy(player, npc.getUID(), npc.getName());
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			}
			return true;

		} else if (args.length == 2
				&& args[0].equalsIgnoreCase("talkwhenclose")) {
			if (Permission.canModify(player, npc, "basic")) {
				changeTalkWhenClose(player, npc, args[1]);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2
				&& args[0].equalsIgnoreCase("lookatplayers")) {
			if (Permission.canModify(player, npc, "basic")) {
				changeLookWhenClose(player, npc, args[1]);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
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
				if (!StringUtils.isNumber(args[1])) {
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
			if (Permission.canModify(player, npc, "basic")) {
				setOwner(player, npc, args[1]);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
			if (Permission.canUse(player, npc, "basic")) {
				switch (args.length) {
				case 1:
					displayList(player, player, npc, "1");
					break;
				case 2:
					if (StringUtils.isNumber(args[1])) {
						displayList(player, player, npc, args[1]);
					} else {
						if (ServerUtils.matchPlayer(player, args[1]) != null) {
							displayList(player,
									ServerUtils.matchPlayer(player, args[1]),
									npc, "1");
						} else {
							player.sendMessage(ChatColor.RED
									+ "Could not match player.");
						}
					}
					break;
				case 3:
					if (ServerUtils.matchPlayer(player, args[1]) != null) {
						displayList(player,
								ServerUtils.matchPlayer(player, args[1]), npc,
								args[2]);
					} else {
						player.sendMessage(ChatColor.RED
								+ "Could not match player.");
					}
					break;
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		}
		if (npc != null) {
			PropertyManager.get("basic").saveState(npc);
		}
		return false;
	}

	/* Example of new command system. Note that these arguments must be used
	 * for *all* commands, although they can be null etc. In this case, args
	 * is unused. Also note that the full package declaration must be used,
	 * but only for the moment (it's conflicting with bukkit's Command class).
	@com.sk89q.minecraft.util.commands.Command(
			aliases = { "move", "mo" }, - the first is the 'main' command, others are aliases.
			usage = "", - what is displayed when the command throws an exception (error etc.)
			flags = "", - optional flags that can be accessed separately (eg -a can specify a free command).
			desc = "Move the npc.", - description of command.
			modifier = "", - allows discrimination of same commands based on the first argument.
			min = 0, - minimum number of arguments.
			max = 0) - max number of arguments.
	@CommandPermissions({ "modify.basic" }) - permission required. 'admin' for admin.
	Automatically fetches the selected npc for permissions operations.
	private void move(Player player, HumanNPC npc, String[] args) {
		if (npc != null) {
			Location loc = npc.getNPCData().getLocation();
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is enroute to your location!");
			npc.getNPCData().setLocation(loc);
			npc.teleport(player.getLocation());
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}
	 */

	/**
	 * Creates an NPC given a string array, containing the name of the NPC and
	 * optional text to be added to it.
	 * 
	 * @param args
	 * @param player
	 */
	private void create(String[] args, Player player) {
		ArrayDeque<String> texts = new ArrayDeque<String>();
		StringBuilder buf = new StringBuilder();
		if (args.length >= 3) {
			int i = 0;
			for (String s : args) {
				if (i == 2 && !s.isEmpty() && !s.equals(";")) {
					buf.append(s);
				}
				if (i > 2 && !s.isEmpty() && !s.equals(";")) {
					buf.append(" ").append(s);
				}
				i += 1;
			}
			texts.add(buf.toString());
		}
		if (args[1].length() > 16) {
			player.sendMessage(ChatColor.RED
					+ "The name of this NPC will be truncated - max name length is 16.");
			args[1] = args[1].substring(0, 16);
		}
		int UID = NPCManager.register(args[1], player.getLocation(),
				player.getName());
		NPCManager.setText(UID, texts);

		NPCManager.get(UID).getNPCData().setOwner(player.getName());

		player.sendMessage(ChatColor.GREEN + "The NPC "
				+ StringUtils.wrap(args[1]) + " was born!");
		if (EconomyHandler.useEconomy()) {
			double paid = EconomyHandler.pay(Operation.BASIC_CREATION, player);
			if (paid > 0) {
				player.sendMessage(MessageUtils.getPaidMessage(
						Operation.BASIC_CREATION, paid, args[1], "", false));
			}
		}
		NPCManager.selectedNPCs.put(player.getName(), UID);
		player.sendMessage(ChatColor.GREEN + "You selected NPC "
				+ StringUtils.wrap(args[1]) + ", ID " + StringUtils.wrap(UID)
				+ ".");
	}

	/**
	 * Moves the selected NPC to the current location of a player.
	 * 
	 * @param player
	 * @param npc
	 * @param name
	 */
	private void move(Player player, HumanNPC npc) {
		/*if (npc != null) {
			Location loc = npc.getNPCData().getLocation();
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is enroute to your location!");
			npc.getNPCData().setLocation(loc);
			npc.teleport(player.getLocation());
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}*/
	}

	private void moveTo(Player player, HumanNPC npc, String[] args) {
		int index = args.length - 1;
		if (index < 4) {
			player.sendMessage(ChatColor.GRAY
					+ "Must at least specify x, y and z.");
			return;
		}
		double x = 0, y = 0, z = 0;
		float yaw = npc.getLocation().getYaw(), pitch = npc.getLocation()
				.getPitch();
		String world = "";
		switch (args.length - 1) {
		case 6:
			pitch = Float.parseFloat(args[index]);
			--index;
		case 5:
			yaw = Float.parseFloat(args[index]);
			--index;
		case 4:
			world = args[index];
			--index;
		case 3:
			z = Double.parseDouble(args[index]);
			--index;
		case 2:
			y = Double.parseDouble(args[index]);
			--index;
		case 1:
			x = Double.parseDouble(args[index]);
		}
		if (Bukkit.getServer().getWorld(world) == null) {
			player.sendMessage("Invalid world.");
			return;
		}
		npc.teleport(new Location(Bukkit.getServer().getWorld(world), x, y, z,
				yaw, pitch));
	}

	/**
	 * Copies an npc's data and position to another npc.
	 * 
	 * @param player
	 * @param UID
	 * @param name
	 */
	private void copy(Player player, int UID, String name) {
		int newUID = NPCManager.register(name, player.getLocation(),
				player.getName());
		HumanNPC newNPC = NPCManager.get(newUID);
		newNPC.teleport(player.getLocation());
		newNPC.getNPCData().setLocation(player.getLocation());
		PropertyManager.copyNPCs(UID, newUID);
	}

	/**
	 * Removes the selected NPC (can optionally be all NPCs).
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 */
	private void remove(Player player, HumanNPC npc, String[] args) {
		if (npc == null) {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return;
		}
		if (Permission.isAdmin(player) && args.length == 2
				&& args[1].equalsIgnoreCase("all")) {
			NPCManager.removeAll();
			player.sendMessage(ChatColor.GRAY + "The NPC(s) disappeared.");
		} else {
			NPCManager.remove(npc.getUID());
			player.sendMessage(ChatColor.GRAY + npc.getName() + " disappeared.");
		}
		NPCManager.selectedNPCs.remove(player.getName());
	}

	/**
	 * Renames the selected NPC.
	 * 
	 * @param player
	 * @param npc
	 * @param name
	 */
	private void rename(Player player, HumanNPC npc, String name) {
		if (npc != null) {
			if (name.length() > 16) {
				player.sendMessage(ChatColor.RED
						+ "Max name length is 16 - NPC name length will be truncated.");
				name = name.substring(0, 16);
			}
			NPCManager.rename(npc.getUID(), name, npc.getOwner());
			player.sendMessage(ChatColor.GREEN
					+ StringUtils.wrap(npc.getName()) + "'s name was set to "
					+ StringUtils.wrap(name) + ".");
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}

	/**
	 * Sets the colour of the selected NPC's name.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 */
	private void setColour(Player player, HumanNPC npc, String[] args) {
		if (npc == null) {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return;
		}
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
					player.sendMessage(ChatColor.RED + "Invalid colour code.");
					return;
				}
			}
			npc.getNPCData().setColour(colour);
			NPCManager.setColour(npc.getUID(), npc.getOwner());
			player.sendMessage(StringUtils.wrapFull("{" + npc.getName()
					+ "}'s name " + args[0] + " is now "
					+ args[1].replace("&", "�") + "this}."));
		}
	}

	/**
	 * Resets the selected NPC's text to a given text.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 */
	private void setText(Player player, HumanNPC npc, String[] args) {
		if (npc == null) {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return;
		}
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
		player.sendMessage(StringUtils.wrapFull("{" + npc.getName()
				+ "}'s text was set to {" + text + "}."));

	}

	/**
	 * Adds text to the selected NPC's text, picked randomly on right click.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 */
	private void addText(Player player, HumanNPC npc, String[] args) {
		if (npc == null) {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return;
		}
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
		NPCManager.addText(npc.getUID(), text);
		player.sendMessage(StringUtils.wrap(text) + " was added to "
				+ StringUtils.wrap(npc.getStrippedName() + "'s") + " text.");
	}

	private void setOwner(Player player, HumanNPC npc, String name) {
		if (npc != null) {
			player.sendMessage(ChatColor.GREEN + "The owner of "
					+ StringUtils.wrap(npc.getStrippedName()) + " is now "
					+ StringUtils.wrap(name) + ".");
			npc.getNPCData().setOwner(name);
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}

	/**
	 * Clears all of an NPC's text.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 */
	private void resetText(Player player, HumanNPC npc, String[] args) {
		if (npc != null) {
			NPCManager.resetText(npc.getUID());
			player.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s")
					+ " text was reset!");
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}

	/**
	 * Sets the selected npc's item in hand.
	 * 
	 * @param player
	 * @param npc
	 * @param name
	 */
	private void setItemInHand(Player player, HumanNPC npc, String name) {
		if (npc != null) {
			NPCDataManager.setItemInHand(player, npc, name);
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}

	/**
	 * Sets the armor of a given type of the npc.
	 * 
	 * @param player
	 * @param npc
	 * @param args
	 */
	private void setArmor(Player player, HumanNPC npc, String[] args) {
		if (npc != null) {
			NPCDataManager.setItemInSlot(args, player, npc);
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}

	/**
	 * Changes whether the selected npc will talk when a player gets near.
	 * 
	 * @param p
	 * @param npc
	 * @param bool
	 */
	private void changeTalkWhenClose(Player player, HumanNPC npc, String bool) {
		boolean talk = false;
		if (bool.equals("true")) {
			talk = true;
		}
		if (npc != null) {
			npc.getNPCData().setTalkClose(talk);
			if (talk) {
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " will now talk to nearby players.");
			} else if (!talk) {
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " will stop talking to nearby players.");
			}
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}

	/**
	 * Changes whether the selected npc will look at nearby players.
	 * 
	 * @param p
	 * @param npc
	 * @param bool
	 */
	private void changeLookWhenClose(Player player, HumanNPC npc, String bool) {
		boolean look = false;
		if (bool.equals("true")) {
			look = true;
		}
		if (npc != null) {
			npc.getNPCData().setLookClose(look);
			if (look) {
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " will now look at players.");
			} else if (!look) {
				player.sendMessage(StringUtils.wrap(npc.getStrippedName())
						+ " will stop looking at players.");
			}
		} else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
		}
	}

	/**
	 * * Display a list of NPCs owned by a player * * @param player * @param npc
	 */
	private void displayList(Player sender, Player toDisplay, HumanNPC npc,
			String passed) {
		PageInstance paginate = PageUtils.newInstance(sender);
		for (HumanNPC hnpc : NPCManager.getList().values()) {
			if (hnpc.getOwner().equals(toDisplay.getName())) {
				paginate.push(ChatColor.GRAY + "" + hnpc.getUID()
						+ ChatColor.YELLOW + " " + hnpc.getStrippedName());
			}
		}
		if (StringUtils.isNumber(passed)) {
			int page = Integer.parseInt(passed);
			if (page == 0) {
				page = 1;
			}
			if (page <= paginate.maxPages()) {
				paginate.header(ChatColor.GREEN + "========== NPC List for "
						+ StringUtils.wrap(toDisplay.getName())
						+ " (%x/%y) ==========");
				paginate.process(page);
			} else {
				sender.sendMessage(MessageUtils.getMaxPagesMessage(page,
						paginate.maxPages()));
			}
		} else {
			sender.sendMessage(ChatColor.RED
					+ "Incorrect syntax. Correct syntax: /npc list (playername) (page)");
		}
	}

	/**
	 * Reload Citizens
	 * 
	 * @param player
	 */
	private void reload(Player player) {
		player.sendMessage(ChatColor.GREEN + "[Citizens] Reloading....");
		Bukkit.getServer().getPluginManager().disablePlugin(Citizens.plugin);
		Bukkit.getServer().getPluginManager().enablePlugin(Citizens.plugin);
		player.sendMessage(ChatColor.GREEN + "[Citizens] Reloaded.");
	}

	/**
	 * Show Citizens plugin information
	 * 
	 * @param player
	 */
	private void showInfo(Player player) {
		player.sendMessage(ChatColor.GREEN + "==========[ "
				+ StringUtils.wrap("Citizens") + " ]==========");
		player.sendMessage(ChatColor.GREEN + "  Version: "
				+ StringUtils.wrap(Citizens.getVersion()));
		player.sendMessage(ChatColor.GREEN + "  Authors: ");
		player.sendMessage(ChatColor.YELLOW + "      - fullwall");
		player.sendMessage(ChatColor.YELLOW + "      - aPunch");
		player.sendMessage(ChatColor.YELLOW + "      - NeonMaster");
		player.sendMessage(ChatColor.YELLOW + "      - TheMPC");
	}
}