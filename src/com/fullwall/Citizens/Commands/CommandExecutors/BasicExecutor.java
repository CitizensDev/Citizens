package com.fullwall.Citizens.Commands.CommandExecutors;

import java.util.ArrayList;

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
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicExecutor implements CommandExecutor {

	private Citizens plugin;

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
		// Get the selected NPC, if any (required for most commands)
		if (NPCManager.validateSelected(player)) {
			npc = NPCManager.get(NPCManager.NPCSelected.get(player.getName()));
		}

		if (args.length >= 2 && args[0].equalsIgnoreCase("create")) {
			if (Permission.hasPermission("citizens.basic.create", sender)) {
				if (!EconomyHandler.useEconomy()
						|| EconomyHandler.canBuy(Operation.BASIC_NPC_CREATE,
								player)) {
					create(args, player);
				} else if (EconomyHandler.useEconomy()) {
					sender.sendMessage(MessageUtils.getNoMoneyMessage(
							Operation.BASIC_NPC_CREATE, player));
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && (args[0].equalsIgnoreCase("move"))) {
			if (Permission.hasPermission("citizens.general.move", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.move")) {
						move(player, npc.getName(), npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if ((args.length == 1 || args.length == 2)
				&& args[0].equalsIgnoreCase("remove")) {
			if (Permission.hasPermission("citizens.general.remove.singular",
					sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.remove.singular")) {
						remove(args, sender, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else if (Permission.hasPermission("citizens.general.remove.all",
					sender)) {
				if (args.length == 2 && args[1].equalsIgnoreCase("all")) {
					remove(args, sender, npc);
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("name")) {
			if (Permission.hasPermission("citizens.general.setname", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.setname")) {
						rename(args[1], sender, npc);
						NPCManager.NPCSelected.remove(player.getName());
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2
				&& (args[0].equalsIgnoreCase("colour") || args[0]
						.equalsIgnoreCase("color"))) {
			if (Permission.hasPermission("citizens.general.colour", sender)
					|| Permission.hasPermission("citizens.general.color",
							sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.colour")
							|| NPCManager.validateOwnership(player,
									npc.getUID(), "citizens.general.color")) {
						setColour(args, player, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
			if (Permission.hasPermission("citizens.basic.settext", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.basic.settext")) {
						addText(args, sender, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length >= 2 && args[0].equalsIgnoreCase("set")) {
			if (Permission.hasPermission("citizens.basic.settext", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.basic.settext")) {
						setText(args, sender, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
			if (Permission.hasPermission("citizens.basic.settext", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.basic.settext")) {
						resetText(args, sender, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("item")) {
			if (Permission.hasPermission("citizens.general.setitem", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.setitem")) {
						setItemInHand(args[1], sender, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2
				&& (args[0].equalsIgnoreCase("torso")
						|| args[0].equalsIgnoreCase("legs")
						|| args[0].equalsIgnoreCase("helmet") || args[0]
						.equalsIgnoreCase("boots"))) {
			if (Permission.hasPermission("citizens.general.setitem", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.setitem")) {
						setArmor(args, sender, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length >= 1 && args[0].equalsIgnoreCase("tp")) {
			if (Permission.hasPermission("citizens.general.tp", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.tp")) {
						player.teleport(npc.getNPCData().getLocation());
						sender.sendMessage(ChatColor.GREEN
								+ "Teleported you to the NPC named "
								+ StringUtils.yellowify(npc.getStrippedName())
								+ ". Enjoy!");
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("copy")) {
			if (Permission.hasPermission("citizens.general.copy", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.copy")) {
						copy(npc.getUID(), npc.getName(), player);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("id")) {
			if (Permission.hasPermission("citizens.general.id", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.getid")) {
						player.sendMessage(ChatColor.GREEN
								+ "The ID of this NPC is "
								+ StringUtils.yellowify("" + npc.getUID())
								+ ".");
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("select")) {
			if (Permission.hasPermission("citizens.general.select", sender)) {
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
					NPCManager.NPCSelected.put(player.getName(), npc.getUID());
					player.sendMessage(ChatColor.GREEN
							+ "Selected NPC with ID "
							+ StringUtils.yellowify("" + npc.getUID())
							+ ", name "
							+ StringUtils.yellowify(npc.getStrippedName())
							+ ".");
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && args[0].equalsIgnoreCase("owner")) {
			if (Permission.hasPermission("citizens.general.owner", sender)) {
				if (npc != null) {
					player.sendMessage(ChatColor.GREEN
							+ "The owner of this NPC is "
							+ StringUtils.yellowify(npc.getOwner()) + ".");
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("setowner")) {
			if (Permission.hasPermission("citizens.general.setowner", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.setowner")) {
						setOwner(player, npc, args[1]);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if (args.length == 2
				&& args[0].equalsIgnoreCase("talkwhenclose")) {
			if (Permission.hasPermission("citizens.general.talkwhenclose",
					sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.talkwhenclose")) {
						changeTalkWhenClose(args[1], player, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2
				&& args[0].equalsIgnoreCase("lookatplayers")) {
			if (Permission.hasPermission("citizens.general.lookatplayers",
					sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.lookatplayers")) {
						changeLookWhenClose(args[1], player, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
			if (Permission.hasPermission("citizens.general.list", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.list")) {
						// list stuff goes here
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if ((command.getName().equalsIgnoreCase("citizens") || command
				.getName().equalsIgnoreCase("npc"))
				&& args.length == 1
				&& (args[0].equalsIgnoreCase("help"))) {
			if (Permission.hasPermission("citizens.help", sender)) {
				HelpUtils.sendHelp(sender);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
		} else if (command.getName().equalsIgnoreCase("basic")) {
			if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
				if (Permission.hasPermission("citizens.basic.help", sender)) {
					int page = Integer.parseInt(args[1]);
					HelpUtils.sendBasicHelpPage(sender, page);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.hasPermission("citizens.basic.help", sender)) {
					HelpUtils.sendBasicHelpPage(sender, 1);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			}
		} else {
			if (args.length >= 2 && args[0].equalsIgnoreCase("move"))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc move");
			else if (args.length >= 3 && args[0].equalsIgnoreCase("remove"))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc remove OR /npc remove all");
			else if (args.length >= 3 && args[0].equalsIgnoreCase("name"))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc name [name]");
			else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("colour") || args[0]
							.equalsIgnoreCase("color")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc color [color]");
			else if (args.length >= 2 && (args[0].equalsIgnoreCase("reset")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc reset");
			else if (args.length >= 3 && (args[0].equalsIgnoreCase("item")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc item [id|item name]");
			else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("torso")
							|| args[0].equalsIgnoreCase("legs")
							|| args[0].equalsIgnoreCase("helmet") || args[0]
							.equalsIgnoreCase("boots")))
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc "
						+ args[0] + " [id|item name]");
			else if (args.length >= 2 && args[0].equalsIgnoreCase("tp"))
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc tp");
			else if (args.length >= 2 && (args[0].equalsIgnoreCase("copy")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc copy");
			else if (args.length >= 2 && (args[0].equalsIgnoreCase("id")))
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc id");
			else if (args.length >= 3 && (args[0].equalsIgnoreCase("select")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc select [id]");
			else if (args.length >= 2 && (args[0].equalsIgnoreCase("owner")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc owner");
			else if (args.length >= 3 && (args[0].equalsIgnoreCase("setowner")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc setowner [player name]");
			else if (args.length >= 3 && (args[0].equalsIgnoreCase("addowner")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc addowner [player name]");
			else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("talkwhenclose")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc talkwhenclose [true|false]");
			else if (args.length >= 3
					&& (args[0].equalsIgnoreCase("lookatplayers")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc lookatplayers [true|false]");
			else
				return false;
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
		if (args[1].length() > 16) {
			player.sendMessage(ChatColor.RED
					+ "NPC name length is too long. The limit is 16 characters.");
			return;
		}
		if ((PropertyManager.getBasicProperties().getNPCAmountPerPlayer(
				player.getName()) < UtilityProperties.getMaxNPCsPerPlayer())
				|| (UtilityProperties.settings.getInt("max-NPCs-per-player") == 0)
				|| (Permission.hasPermission("citizens.general.nolimit",
						(CommandSender) player))) {
			int UID = NPCManager.register(args[1], player.getLocation(),
					player.getName());
			PropertyManager.getBasicProperties().saveNPCAmountPerPlayer(
					player.getName(),
					PropertyManager.getBasicProperties().getNPCAmountPerPlayer(
							player.getName()) + 1);
			NPCManager.setText(UID, texts);

			NPCManager.get(UID).getNPCData().setOwner(player.getName());

			player.sendMessage(ChatColor.GREEN + "The NPC "
					+ StringUtils.yellowify(args[1]) + " was born!");
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(Operation.BASIC_NPC_CREATE,
						player);
				if (paid > 0)
					player.sendMessage(MessageUtils.getPaidMessage(
							Operation.BASIC_NPC_CREATE, paid, args[1], "",
							false));
			}
			NPCManager.NPCSelected.put(player.getName(), UID);
			player.sendMessage(ChatColor.GREEN + "You selected NPC "
					+ StringUtils.yellowify(args[1]) + ", ID "
					+ StringUtils.yellowify(UID) + ".");
		} else {
			player.sendMessage(ChatColor.GREEN
					+ "You have reached the NPC-creation limit of "
					+ StringUtils.yellowify(""
							+ UtilityProperties.getMaxNPCsPerPlayer()) + ".");
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
		player.sendMessage(StringUtils.yellowify(name)
				+ " is enroute to your location!");
		npc.getNPCData().setLocation(loc);
		npc.moveTo(player.getLocation());
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
		newNPC.moveTo(p.getLocation());

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
			PropertyManager.getBasicProperties().locations.setInt("currentID",
					0);
			PropertyManager.getBasicProperties().locations.removeKey("list");
		} else {
			plugin.basicNPCHandler.remove(npc.getUID());
			sender.sendMessage(ChatColor.GRAY + npc.getName() + " disappeared.");
		}
		NPCManager.NPCSelected.remove(p.getName());
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
		sender.sendMessage(ChatColor.GREEN
				+ StringUtils.yellowify(npc.getName()) + "'s name was set to "
				+ StringUtils.yellowify(name) + ".");
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
		} else {
			plugin.basicNPCHandler.setColour(npc.getUID(), args[1],
					npc.getOwner());
			npc.getNPCData().setColour(args[1].replace("&", "§"));
			player.sendMessage(StringUtils.yellowify(npc.getName())
					+ "'s name colour is now " + args[1] + "this"
					+ ChatColor.GREEN + ".");
		}
	}

	public @interface yellow {

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
				if (i == 1 && !s.isEmpty() && !s.equals(";"))
					text += s;
				if (i > 1 && !s.isEmpty() && !s.equals(";"))
					text += " " + s;
				i += 1;
			}
		}
		ArrayList<String> texts = new ArrayList<String>();
		texts.add(text);
		NPCManager.setText(npc.getUID(), texts);
		sender.sendMessage(ChatColor.GREEN
				+ StringUtils.yellowify(npc.getName()) + "'s text was set to "
				+ StringUtils.yellowify(text) + ".");

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
		sender.sendMessage(StringUtils.yellowify(text) + " was added to "
				+ StringUtils.yellowify(npc.getStrippedName() + "'s")
				+ " text.");
	}

	private void setOwner(Player player, HumanNPC npc, String name) {
		player.sendMessage(ChatColor.GREEN + "The owner of "
				+ StringUtils.yellowify(npc.getStrippedName()) + " is now "
				+ StringUtils.yellowify(name) + ".");
		npc.getNPCData().setOwner(name);
		npc.crouch();
	}

	/**
	 * Clears all of an NPC's text.
	 * 
	 * @param args
	 * @param sender
	 * @param npc
	 */
	private void resetText(String[] args, CommandSender sender, HumanNPC npc) {
		plugin.basicNPCHandler.resetText(npc.getUID());
		sender.sendMessage(StringUtils.yellowify(npc.getStrippedName() + "'s")
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
		if (bool.equals("true"))
			talk = true;
		npc.getNPCData().setTalkClose(talk);
		if (talk)
			p.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " will now talk to nearby players.");
		else if (!talk)
			p.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " will stop talking to nearby players.");
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
		if (bool.equals("true"))
			look = true;
		npc.getNPCData().setLookClose(look);
		if (look)
			p.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " will now look at players.");
		else if (!look)
			p.sendMessage(StringUtils.yellowify(npc.getStrippedName())
					+ " will stop looking at players.");
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
