package com.fullwall.Citizens.CommandExecutors;

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
import com.fullwall.Citizens.NPCs.NPCData;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.PropertyPool;
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
		if (NPCManager.validateSelected(player))
			npc = NPCManager
					.getNPC(NPCManager.NPCSelected.get(player.getName()));

		if (args.length >= 2 && args[0].equals("create")) {
			if (hasPermission("citizens.basic.create", sender)) {
				if (!EconomyHandler.useEconomy()
						|| EconomyHandler.canBuy(Operation.BASIC_NPC_CREATE,
								player)) {
					createNPC(args, player);
				} else if (EconomyHandler.useEconomy()) {
					sender.sendMessage(MessageUtils.getNoMoneyMessage(
							Operation.BASIC_NPC_CREATE, player));
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 1 && (args[0].equals("move"))) {
			if (hasPermission("citizens.general.move", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.move")) {
						moveNPC(sender, npc.getName(), npc);
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
				&& args[0].equals("remove")) {
			if (hasPermission("citizens.general.remove.singular", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.remove.singular")) {
						removeNPC(args, sender, npc);
					} else {
						sender.sendMessage(MessageUtils.notOwnerMessage);
					}
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else if (hasPermission("citizens.general.remove.all", sender)) {
				if (args.length == 2 && args[1].equals("all")) {
					removeNPC(args, sender, npc);
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equals("name")) {
			if (hasPermission("citizens.general.setname", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.setname")) {
						setName(args[1], sender, npc);
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
				&& (args[0].equals("colour") || args[0].equals("color"))) {
			if (hasPermission("citizens.general.colour", sender)
					|| hasPermission("citizens.general.color", sender)) {
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

		} else if (args.length >= 2 && args[0].equals("add")) {
			if (hasPermission("citizens.basic.settext", sender)) {
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

		} else if (args.length >= 2 && args[0].equals("set")) {
			if (hasPermission("citizens.basic.settext", sender)) {
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

		} else if (args.length == 1 && args[0].equals("reset")) {
			if (hasPermission("citizens.basic.settext", sender)) {
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

		} else if (args.length == 2 && args[0].equals("item")) {
			if (hasPermission("citizens.general.setitem", sender)) {
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
				&& (args[0].equals("torso") || args[0].equals("legs")
						|| args[0].equals("helmet") || args[0].equals("boots"))) {
			if (hasPermission("citizens.general.setitem", sender)) {
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

		} else if (args.length >= 1 && args[0].equals("tp")) {
			if (hasPermission("citizens.general.tp", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.tp")) {
						player.teleport((PropertyPool.getLocationFromID(npc
								.getUID())));
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

		} else if (args.length == 1 && args[0].equals("copy")) {
			if (hasPermission("citizens.general.copy", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.copy")) {
						copyNPC(npc.getUID(), npc.getName(), player);
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
		} else if (args.length == 1 && args[0].equals("id")) {
			if (hasPermission("citizens.general.getid", sender)) {
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

		} else if (args.length == 2 && args[0].equals("select")) {
			if (hasPermission("citizens.general.select", sender)) {
				// BUILD CHECK
				if (!Character.isDigit(args[1].charAt(0))) {
					player.sendMessage(ChatColor.RED
							+ "The ID must be a number.");
					return true;
				}
				npc = NPCManager.getNPC(Integer.valueOf(args[1]));
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

		} else if (args.length == 1 && args[0].equals("owner")) {
			if (hasPermission("citizens.general.getowner", sender)) {
				if (npc != null) {
					player.sendMessage(ChatColor.GREEN
							+ "The owner of this NPC is "
							+ StringUtils.yellowify(PropertyPool.getOwner(npc
									.getUID())) + ".");
				} else {
					sender.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
				}
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;

		} else if (args.length == 2 && args[0].equals("setowner")) {
			if (hasPermission("citizens.general.setowner", sender)) {
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
		} else if (args.length == 2 && args[0].equals("addowner")) {
			if (hasPermission("citizens.general.addowner", sender)) {
				if (npc != null) {
					if (NPCManager.validateOwnership(player, npc.getUID(),
							"citizens.general.addowner")) {
						PropertyPool.addOwner(npc.getUID(), args[1], player);
						npc.getNPCData().setOwner(player.getName());
						player.sendMessage(ChatColor.GREEN + "Added "
								+ StringUtils.yellowify(args[1])
								+ " to the owner list of "
								+ StringUtils.yellowify(npc.getStrippedName())
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

		} else if (args.length == 2 && args[0].equals("talkwhenclose")) {
			if (hasPermission("citizens.general.talkwhenclose", sender)) {
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

		} else if (args.length == 2 && args[0].equals("lookatplayers")) {
			if (hasPermission("citizens.general.lookatplayers", sender)) {
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

		} else if ((command.getName().equals("citizens") || command.getName()
				.equals("npc")) && args.length == 1 && (args[0].equals("help"))) {
			if (hasPermission("citizens.help", sender)) {
				sendHelp(sender);
			} else {
				sender.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return true;
			// TODO replace all sendHelp() methods with ones from HelpUtils
		} else if ((command.getName().equals("citizens") || command.getName()
				.equals("npc")) && args.length >= 2) {
			if (args[1].equals("help")) {
				int page = 1;
				if (args.length == 3)
					page = Integer.parseInt(args[2]);
				if (args[0].equals("basic")) {
					if (hasPermission("citizens.basic.help", sender)) {
						sendBasicHelpPage(sender, page);
						// HelpUtils.sendBasicHelpPage(sender, page);
					} else {
						sender.sendMessage(MessageUtils.noPermissionsMessage);
					}
				} else if (args[0].equals("trader")) {
					if (hasPermission("citizens.trader.help", sender)) {
						sendTraderHelpPage(sender, page);
					} else {
						sender.sendMessage(MessageUtils.noPermissionsMessage);
					}
				}
			}
			return true;

		} else {
			if (args.length >= 2 && args[0].equals("move"))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc move");
			else if (args.length >= 3 && args[0].equals("remove"))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc remove OR /npc remove all");
			else if (args.length >= 3 && args[0].equals("name"))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc name [name]");
			else if (args.length >= 3
					&& (args[0].equals("colour") || args[0].equals("color")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc color [color]");
			else if (args.length >= 2 && (args[0].equals("reset")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc reset");
			else if (args.length >= 3 && (args[0].equals("item")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc item [id|item name]");
			else if (args.length >= 3
					&& (args[0].equals("torso") || args[0].equals("legs")
							|| args[0].equals("helmet") || args[0]
							.equals("boots")))
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc "
						+ args[0] + " [id|item name]");
			else if (args.length >= 2 && args[0].equals("tp"))
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /npc tp");
			else if ((command.getName().equals("citizens") || command.getName()
					.equals("npc"))
					&& args.length >= 3
					&& (args[0].equals("help")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc help [page number]");
			else if (args.length >= 2 && (args[0].equals("copy")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc copy");
			else if (args.length >= 2 && (args[0].equals("getid")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc getid");
			else if (args.length >= 3 && (args[0].equals("select")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc select [id]");
			else if (args.length >= 2 && (args[0].equals("getowner")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc getowner");
			else if (args.length >= 3 && (args[0].equals("setowner")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc setowner [player name]");
			else if (args.length >= 3 && (args[0].equals("addowner")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc addowner [player name]");
			else if (args.length >= 3 && (args[0].equals("talkwhenclose")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc talkwhenclose [true|false]");
			else if (args.length >= 3 && (args[0].equals("lookatplayers")))
				sender.sendMessage(ChatColor.RED
						+ "Incorrect Syntax: /npc lookatplayers [true|false]");
			else
				return false;

			return true;
		}
	}

	/**
	 * Creates an NPC given a string array, containing the name of the NPC and
	 * optional text to be added to it.
	 * 
	 * @param args
	 * @param player
	 */
	private void createNPC(String[] args, Player player) {
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
		if (PropertyPool.getNPCAmountPerPlayer(player.getName()) < PropertyPool
				.getMaxNPCsPerPlayer()
				|| PropertyPool.settings.getInt("max-NPCs-per-player") == 0) {
			int UID = plugin.handler.spawnNPC(args[1], player.getLocation(),
					player.getName());
			PropertyPool.saveNPCAmountPerPlayer(player.getName(),
					PropertyPool.getNPCAmountPerPlayer(player.getName()) + 1);
			plugin.handler.setNPCText(UID, texts);
			plugin.handler.setOwner(NPCManager.getNPC(UID), player.getName());
			player.sendMessage(ChatColor.GREEN + "The NPC "
					+ StringUtils.yellowify(args[1]) + " was born!");
			if (EconomyHandler.useEconomy()) {
				int paid = EconomyHandler.pay(Operation.BASIC_NPC_CREATE,
						player);
				if (paid > 0)
					player.sendMessage(MessageUtils.getPaidMessage(
							Operation.BASIC_NPC_CREATE, paid, args[1], "",
							false));
			}
			NPCManager.NPCSelected.put(player.getName(), UID);
			player.sendMessage(ChatColor.GREEN + "You selected NPC ["
					+ StringUtils.yellowify(args[1]) + "], ID ["
					+ StringUtils.yellowify(UID) + "]");
		} else {
			player.sendMessage(ChatColor.GREEN
					+ "You have reached the NPC-creation limit of "
					+ StringUtils.yellowify(""
							+ PropertyPool.getMaxNPCsPerPlayer()) + ".");
		}
	}

	/**
	 * Moves the selected NPC to the current location of a player.
	 * 
	 * @param sender
	 * @param name
	 * @param npc
	 */
	private void moveNPC(CommandSender sender, String name, HumanNPC npc) {
		Location loc = PropertyPool.getLocationFromID(npc.getUID());
		if (loc != null) {
			PropertyPool.saveLocation(name, loc, npc.getUID());
		}
		plugin.handler.moveNPC(npc, ((Player) sender).getLocation());
		sender.sendMessage(StringUtils.yellowify(name)
				+ " is enroute to your location!");
	}

	/**
	 * Removes the selected NPC (can optionally be all NPCs).
	 * 
	 * @param args
	 * @param sender
	 */
	private void removeNPC(String[] args, CommandSender sender, HumanNPC npc) {
		Player p = (Player) sender;
		if (args.length == 2 && args[1].equals("all")) {
			plugin.handler.removeAllNPCs();
			sender.sendMessage(ChatColor.GRAY + "The NPC(s) disappeared.");
			PropertyPool.locations.setInt("currentID", 0);
			PropertyPool.locations.removeKey("list");
		} else {
			plugin.handler.removeNPC(npc.getUID());
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
	private void setName(String name, CommandSender sender, HumanNPC npc) {
		if (name.length() > 16) {
			sender.sendMessage(ChatColor.RED
					+ "NPCs can't have names longer than 16 characters.");
			return;
		}
		plugin.handler.setName(npc.getUID(), name, npc.getOwner());
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
			plugin.handler.setColour(npc.getUID(), args[1], npc.getOwner());
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
				if (i == 1 && !s.isEmpty() && !s.equals(";"))
					text += s;
				if (i > 1 && !s.isEmpty() && !s.equals(";"))
					text += " " + s;
				i += 1;
			}
		}
		ArrayList<String> texts = new ArrayList<String>();
		texts.add(text);
		plugin.handler.setNPCText(npc.getUID(), texts);
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
		plugin.handler.addNPCText(npc.getUID(), text);
		sender.sendMessage(StringUtils.yellowify(text) + " was added to "
				+ StringUtils.yellowify(npc.getStrippedName() + "'s")
				+ " text.");
	}

	private void setOwner(Player player, HumanNPC npc, String name) {
		PropertyPool.setOwner(npc.getUID(), name);
		player.sendMessage(ChatColor.GREEN + "The owner of "
				+ StringUtils.yellowify(npc.getStrippedName()) + " is now "
				+ StringUtils.yellowify(name) + ".");
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
		plugin.handler.resetText(npc.getUID());
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
		plugin.handler.setItemInHand((Player) sender, npc, name);
	}

	/**
	 * Sets the armor of a given type of the npc.
	 * 
	 * @param args
	 * @param sender
	 * @param npc
	 */
	private void setArmor(String[] args, CommandSender sender, HumanNPC npc) {
		plugin.handler.setItemInSlot(args, (Player) sender, npc);
	}

	// Ugh, ugly. Maybe we should palm the PropertyPool functions off to
	// somewhere else.
	/**
	 * Copies an npc's data and position to another npc.
	 * 
	 * @param npc
	 * @param p
	 */
	private void copyNPC(int UID, String name, Player p) {
		ArrayList<String> texts = PropertyPool.getText(UID);
		String colour = PropertyPool.getColour(UID);
		String owner = PropertyPool.getOwner(UID);
		ArrayList<Integer> items = PropertyPool.getItems(UID);
		boolean lookatplayers = PropertyPool.getLookWhenClose(UID);
		boolean talkwhenclose = PropertyPool.getTalkWhenClose(UID);
		int newUID = plugin.handler
				.spawnNPC(name, p.getLocation(), p.getName());

		HumanNPC newNPC = NPCManager.getNPC(newUID);
		newNPC.setNPCData(new NPCData(newNPC.getName(), newNPC.getUID(), newNPC
				.getLocation(), colour, items, texts, lookatplayers,
				talkwhenclose, owner, newNPC.getBalance()));
		PropertyPool.saveState(newUID, newNPC.getNPCData());

		// NPCDataManager.addItems(newNPC, items);

		String newName = newNPC.getName();
		NPCManager.removeNPCForRespawn(newUID);
		plugin.handler.spawnExistingNPC(newName, newUID, newNPC.getOwner());
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
		PropertyPool.setTalkWhenClose(npc.getUID(), talk);
		if (talk)
			p.sendMessage(ChatColor.GREEN + "The NPC: "
					+ StringUtils.yellowify(npc.getStrippedName())
					+ " will now talk to nearby players.");
		else if (!talk)
			p.sendMessage(ChatColor.GREEN + "The NPC: "
					+ StringUtils.yellowify(npc.getStrippedName())
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
		PropertyPool.setLookWhenClose(npc.getUID(), look);
		if (look)
			p.sendMessage(ChatColor.GREEN + "The NPC: "
					+ StringUtils.yellowify(npc.getStrippedName())
					+ " will now look at players.");
		else if (!look)
			p.sendMessage(ChatColor.GREEN + "The NPC: "
					+ StringUtils.yellowify(npc.getStrippedName())
					+ " will stop looking at players.");
	}

	/**
	 * Sends the help page for /citizens help.
	 * 
	 * @param sender
	 */
	private void sendHelp(CommandSender sender) {
		sender.sendMessage("§fCitizens " + Citizens.getVersion() + " Help");
		sender.sendMessage("§b-------------------------------");
		sender.sendMessage("§8/§ctoggle [type] §e- §atoggles the state of an NPC.");
		sender.sendMessage("§8/§ctoggle all [on/off] §e- toggles all types that the NPC is.");
		sender.sendMessage("§8/§ccitizens §b[basic|trader|healer] help [page] §e- §aview help pages for each type of NPC.");
	}

	/**
	 * Sends the help page for the basic npc type.
	 * 
	 * @param sender
	 * @param page
	 */
	private void sendBasicHelpPage(CommandSender sender, int page) {
		switch (page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Citizens "
					+ Citizens.getVersion() + " Basic Help");
			sender.sendMessage("§b-------------------------------");
			sender.sendMessage("§8/§cnpc §bcreate (text) §e- §acreates an NPC at your location.");
			sender.sendMessage("§8/§cnpc §bset [text] §e- §asets the text of an NPC.");
			sender.sendMessage("§8/§cnpc §badd [text] §e- §aadds text to an NPC.");
			sender.sendMessage("§fUse the command /citizens [basic|trader] help [page] for more info.");
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
			sender.sendMessage(ChatColor.GRAY
					+ "Plugin made by fullwall, NeonMaster, TheMPC, and aPunch");
			break;
		default:
			sender.sendMessage(ChatColor.GRAY
					+ "The total number of pages is 4, page: " + page
					+ " is not available.");
			break;
		}
	}

	/**
	 * Sends the help page for the trader npc type.
	 * 
	 * @param sender
	 * @param page
	 */
	private void sendTraderHelpPage(CommandSender sender, int page) {
		switch (page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Citizens "
					+ Citizens.getVersion() + " Trader Help");
			sender.sendMessage("§b-------------------------------");
			sender.sendMessage("§8/§ctrader §blist [buy/sell] (page) §e- §alists the selected trader's buy/selling list (paged).");
			sender.sendMessage("§8/§ctrader §b[buy/sell] [itemID(:amount:data)] [itemID(:amount:data)]§e- §astarts an npc stocking a given item. Put no : in the second argument for iConomy.");
			sender.sendMessage("§8/§ctrader §b[buy/sell] [remove] [item ID] §e- §astops the given item ID from being stocked.");
			sender.sendMessage("§fUse the command /citizens [basic|trader] help [page] for more info.");
			break;
		case 2:
			sender.sendMessage(ChatColor.GOLD + "Citizens "
					+ Citizens.getVersion() + " Trader Help");
			sender.sendMessage("§b-------------------------------");
			sender.sendMessage("§8/§ctrader §bbalance [give/take] [amount] §e- §aif using iConomy, controls a trader's money.");
			sender.sendMessage("§8/§ctrader §bunlimited [true/false] §e- §asets whether a trader has unlimited stock.");
			sender.sendMessage("§b-------------------------------");
			sender.sendMessage(ChatColor.GRAY
					+ "Plugin made by fullwall, NeonMaster, TheMPC, and aPunch");
			break;
		default:
			sender.sendMessage(ChatColor.GRAY
					+ "The total number of pages is 2, page: " + page
					+ " is not available.");
			break;
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

	/**
	 * Checks for permission given a permission string.
	 * 
	 * @param permission
	 * @param sender
	 * @return
	 */
	public static boolean hasPermission(String permission, CommandSender sender) {
		return (!(sender instanceof Player) || Permission.generic(
				(Player) sender, permission));
	}
}
