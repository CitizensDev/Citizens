package com.citizens.commands.commands;

import java.util.ArrayDeque;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.Citizens;
import com.citizens.Permission;
import com.citizens.commands.CommandHandler;
import com.citizens.economy.EconomyManager;
import com.citizens.npcs.NPCDataManager;
import com.citizens.npcs.NPCManager;
import com.citizens.npctypes.CitizensNPC;
import com.citizens.properties.PropertyManager;
import com.citizens.properties.SettingsManager;
import com.citizens.properties.properties.UtilityProperties;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.sk89q.Command;
import com.citizens.resources.sk89q.CommandContext;
import com.citizens.resources.sk89q.CommandPermissions;
import com.citizens.resources.sk89q.CommandRequirements;
import com.citizens.resources.sk89q.ServerCommand;
import com.citizens.utils.HelpUtils;
import com.citizens.utils.InventoryUtils;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.Messaging;
import com.citizens.utils.ServerUtils;
import com.citizens.utils.StringUtils;

@CommandRequirements(requireSelected = true, requireOwnership = true)
public class BasicCommands implements CommandHandler {

	@Command(aliases = "citizens", desc = "view Citizens info", max = 0)
	@ServerCommand()
	@CommandPermissions("admin.info")
	@CommandRequirements()
	public static void citizens(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		sender.sendMessage(ChatColor.GREEN
				+ StringUtils.listify(StringUtils.wrap("Citizens")));
		sender.sendMessage(ChatColor.GREEN + "  Version: "
				+ StringUtils.wrap(Citizens.getVersion()));
		sender.sendMessage(ChatColor.GREEN + "  Authors: ");
		sender.sendMessage(ChatColor.YELLOW + "      - fullwall");
		sender.sendMessage(ChatColor.YELLOW + "      - aPunch");
		sender.sendMessage(ChatColor.YELLOW + "      - NeonMaster");
	}

	@Command(
			aliases = "citizens",
			usage = "help (page)",
			desc = "view the Citizens help page",
			modifiers = "help",
			min = 1,
			max = 2)
	@CommandPermissions("basic.use.help")
	@CommandRequirements()
	@ServerCommand()
	public static void citizensHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		int page = 1;
		if (args.argsLength() == 2) {
			page = Integer.parseInt(args.getString(1));
		}
		HelpUtils.sendHelpPage(sender, page);
	}

	@CommandRequirements()
	@Command(
			aliases = "citizens",
			usage = "reload",
			desc = "reload Citizens",
			modifiers = "reload",
			min = 1,
			max = 1)
	@CommandPermissions("admin.reload")
	@ServerCommand()
	public static void reload(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		Messaging.log("Reloading configuration settings....");
		sender.sendMessage(ChatColor.GREEN + "[" + StringUtils.wrap("Citizens")
				+ "] Reloading....");

		UtilityProperties.initialize();
		PropertyManager.loadAll();
		SettingsManager.setupVariables();

		Messaging.log("Reloaded.");
		sender.sendMessage(ChatColor.GREEN + "[" + StringUtils.wrap("Citizens")
				+ "] Reloaded.");
	}

	@CommandRequirements()
	@Command(
			aliases = "citizens",
			usage = "save",
			desc = "force a save of Citizens files",
			modifiers = "save",
			min = 1,
			max = 1)
	@ServerCommand()
	@CommandPermissions("admin.save")
	public static void save(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		if (sender instanceof Player) {
			Messaging.log("Saving...");
		}
		sender.sendMessage(ChatColor.GREEN + "[" + StringUtils.wrap("Citizens")
				+ "] Saving...");

		PropertyManager.saveState();

		if (sender instanceof Player) {
			Messaging.log("Saved.");
		}
		sender.sendMessage(ChatColor.GREEN + "[" + StringUtils.wrap("Citizens")
				+ "] Saved.");
	}

	@CommandRequirements()
	@Command(
			aliases = "citizens",
			usage = "debug",
			desc = "toggle debug mode for Citizens",
			modifiers = "debug",
			min = 1,
			max = 1)
	@ServerCommand()
	@CommandPermissions("admin.debug")
	public static void debug(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		boolean debug = SettingsManager.getBoolean("DebugMode");
		UtilityProperties.getSettings()
				.setBoolean("general.debug-mode", !debug);
		debug = !debug;
		if (debug) {
			Messaging.log("Debug mode is now on.");
			if (sender instanceof Player) {
				Messaging.send((Player) sender, npc, "Debug mode is now "
						+ ChatColor.GREEN + "on");
			}
		} else {
			Messaging.log("Debug mode is now off.");
			if (sender instanceof Player) {
				Messaging.send((Player) sender, npc, "Debug mode is now "
						+ ChatColor.RED + "off");
			}
		}
	}

	@CommandRequirements()
	@Command(
			aliases = { "basic", "npc" },
			usage = "help (page)",
			desc = "view the Basic NPC help page",
			modifiers = "help",
			min = 1,
			max = 2)
	@CommandPermissions("basic.use.help")
	@ServerCommand()
	public static void basicHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		int page = 1;
		if (args.argsLength() == 2) {
			page = Integer.parseInt(args.getString(1));
		}
		HelpUtils.sendBasicHelpPage(sender, page);
	}

	@CommandRequirements(requireSelected = true)
	@Command(aliases = "npc", desc = "view information for an NPC", max = 0)
	@CommandPermissions("basic.use.info")
	public static void npc(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		sender.sendMessage(ChatColor.GREEN
				+ StringUtils.listify(StringUtils.wrap(npc.getStrippedName())));
		sender.sendMessage(ChatColor.GREEN + "ID: "
				+ StringUtils.wrap(npc.getUID()));
		sender.sendMessage(ChatColor.GREEN + "Owner: "
				+ StringUtils.wrap(npc.getOwner()));
		sender.sendMessage(ChatColor.GREEN + "Types:");
		if (npc.types() == null) {
			sender.sendMessage(ChatColor.RED + "None");
			return;
		}
		for (CitizensNPC t : npc.types()) {
			sender.sendMessage(ChatColor.GRAY + "    - "
					+ StringUtils.wrap(t.getType()));
		}
	}

	@CommandRequirements()
	@Command(
			aliases = "npc",
			usage = "create [name] (text)",
			desc = "create an NPC",
			modifiers = "create",
			min = 2)
	@CommandPermissions("basic.create")
	public static void create(CommandContext args, Player player, HumanNPC npc) {
		if (!Permission.canCreate(player)) {
			player.sendMessage(MessageUtils.reachedNPCLimitMessage);
			return;
		}
		ArrayDeque<String> texts = new ArrayDeque<String>();
		String firstArg = args.getString(1);
		if (args.argsLength() >= 3) {
			texts.add(args.getJoinedStrings(2));
		}
		if (firstArg.length() > 16) {
			player.sendMessage(ChatColor.RED
					+ "The name of this NPC will be truncated - max name length is 16.");
			firstArg = args.getString(1).substring(0, 16);
		}
		if (EconomyManager.useEconPlugin()) {
			if (EconomyManager.hasEnough(player,
					UtilityProperties.getPrice("basic.creation"))) {
				double paid = EconomyManager.pay(player,
						UtilityProperties.getPrice("basic.creation"));
				if (paid > 0) {
					player.sendMessage(MessageUtils.getPaidMessage(player,
							"basic", "basic.creation",
							firstArg.replace("/", " "), false));
				}
			} else {
				player.sendMessage(MessageUtils.getNoMoneyMessage(player,
						"basic.creation"));
				return;
			}
		}
		int UID = NPCManager.register(firstArg, player.getLocation(),
				player.getName());
		NPCDataManager.setText(UID, texts);

		HumanNPC created = NPCManager.get(UID);
		created.getNPCData().setOwner(player.getName());
		Messaging.send(player, created,
				SettingsManager.getString("CreationMessage"));

		NPCDataManager.selectNPC(player, NPCManager.get(UID));
		Messaging.send(player, created,
				SettingsManager.getString("CreationMessage"));
	}

	@Command(
			aliases = "npc",
			usage = "move",
			desc = "move an NPC",
			modifiers = "move",
			min = 1,
			max = 1)
	@CommandPermissions("basic.modify.move")
	public static void move(CommandContext args, Player player, HumanNPC npc) {
		player.sendMessage(StringUtils.wrap(npc.getStrippedName())
				+ " is enroute to your location!");
		npc.teleport(player.getLocation());
		npc.getNPCData().setLocation(player.getLocation());
	}

	@Command(
			aliases = "npc",
			usage = "moveto [x y z](world pitch yaw)",
			desc = "move an NPC to a location",
			modifiers = "moveto",
			min = 3,
			max = 6)
	@CommandPermissions("basic.modify.moveto")
	public static void moveTo(CommandContext args, Player player, HumanNPC npc) {
		int index = args.argsLength() - 1;
		double x = 0, y = 0, z = 0;
		float yaw = npc.getLocation().getYaw();
		float pitch = npc.getLocation().getPitch();
		String world = npc.getWorld().getName();
		switch (args.argsLength()) {
		case 6:
			yaw = Float.parseFloat(args.getString(index));
			--index;
		case 5:
			pitch = Float.parseFloat(args.getString(index));
			--index;
		case 4:
			world = args.getString(index);
			--index;
		case 3:
			z = Double.parseDouble(args.getString(index));
			--index;
		case 2:
			y = Double.parseDouble(args.getString(index));
			--index;
		case 1:
			x = Double.parseDouble(args.getString(index));
		}
		if (Bukkit.getServer().getWorld(world) == null) {
			player.sendMessage("Invalid world.");
			return;
		}
		npc.teleport(new Location(Bukkit.getServer().getWorld(world), x, y, z,
				pitch, yaw));
	}

	@Command(
			aliases = "npc",
			usage = "copy",
			desc = "copy an NPC",
			modifiers = "copy",
			min = 1,
			max = 1)
	@CommandPermissions("basic.modify.copy")
	public static void copy(CommandContext args, Player player, HumanNPC npc) {
		if (!Permission.canCreate(player)) {
			player.sendMessage(MessageUtils.reachedNPCLimitMessage);
			return;
		}
		PropertyManager.save(npc);
		int newUID = NPCManager.register(npc.getName(), player.getLocation(),
				player.getName());
		HumanNPC newNPC = NPCManager.get(newUID);
		newNPC.teleport(player.getLocation());
		newNPC.getNPCData().setLocation(player.getLocation());
		PropertyManager.copyNPCs(npc.getUID(), newUID);
		PropertyManager.load(newNPC);
	}

	@Command(
			aliases = "npc",
			usage = "remove (all)",
			desc = "remove NPCs",
			modifiers = "remove",
			min = 1,
			max = 2)
	@CommandPermissions("basic.modify.remove")
	public static void remove(CommandContext args, Player player, HumanNPC npc) {
		if (args.argsLength() == 2 && args.getString(1).equalsIgnoreCase("all")) {
			if (Permission.generic(player, "citizens.admin.removeall")) {
				NPCManager.removeAll();
				NPCDataManager.deselectNPC(player);
				player.sendMessage(ChatColor.GRAY + "The NPC(s) disappeared.");
			} else {
				player.sendMessage(MessageUtils.noPermissionsMessage);
			}
			return;
		}
		NPCManager.remove(npc.getUID());
		NPCDataManager.deselectNPC(player);
		player.sendMessage(StringUtils.wrap(npc.getStrippedName(),
				ChatColor.GRAY) + " disappeared.");
	}

	@Command(
			aliases = "npc",
			usage = "rename [name]",
			desc = "rename an NPC",
			modifiers = "rename",
			min = 2,
			max = 2)
	@CommandPermissions("basic.modify.rename")
	public static void rename(CommandContext args, Player player, HumanNPC npc) {
		String name = args.getString(1);
		if (name.length() > 16) {
			player.sendMessage(ChatColor.RED
					+ "Max name length is 16 - NPC name length will be truncated.");
			name = name.substring(0, 16);
		}
		NPCManager.rename(npc.getUID(), name, npc.getOwner());
		player.sendMessage(ChatColor.GREEN + StringUtils.wrap(npc.getName())
				+ "'s name was set to " + StringUtils.wrap(name) + ".");
	}

	@Command(
			aliases = "npc",
			usage = "color [color-code]",
			desc = "set the name color of an NPC",
			modifiers = "color",
			min = 2,
			max = 2)
	@CommandPermissions("basic.modify.color")
	public static void color(CommandContext args, Player player, HumanNPC npc) {
		if (!args.getString(1).substring(0, 1).equals("&")) {
			player.sendMessage(ChatColor.RED + "Use an & to specify color.");
		} else if (args.getString(1).length() != 2) {
			player.sendMessage(ChatColor.GRAY
					+ "Use the format &(code). Example - &f = white.");
		} else {
			int colour = 0xf;
			try {
				colour = Integer.parseInt(args.getString(1).substring(1, 2));
			} catch (NumberFormatException ex) {
				try {
					colour = Integer.parseInt(
							args.getString(1).substring(1, 2), 16);
				} catch (NumberFormatException e) {
					player.sendMessage(ChatColor.RED + "Invalid color code.");
					return;
				}
			}
			if (ChatColor.getByCode(colour) == null) {
				player.sendMessage(ChatColor.RED + "Color code not recognised.");
				return;
			}
			npc.getNPCData().setColour(ChatColor.getByCode(colour));
			NPCManager.setColour(npc.getUID(), npc.getOwner());
			player.sendMessage(StringUtils.wrapFull("{" + npc.getStrippedName()
					+ "}'s name color is now "
					+ args.getString(1).replace("&", "\u00A7") + "this}."));
		}
	}

	@Command(
			aliases = "npc",
			usage = "set [text]",
			desc = "set the text of an NPC",
			modifiers = "set",
			min = 2)
	@CommandPermissions("basic.modify.settext")
	public static void set(CommandContext args, Player player, HumanNPC npc) {
		String text = args.getJoinedStrings(1);
		ArrayDeque<String> texts = new ArrayDeque<String>();
		texts.add(text);
		NPCDataManager.setText(npc.getUID(), texts);
		player.sendMessage(StringUtils.wrapFull("{" + npc.getStrippedName()
				+ "}'s text was set to {" + text + "}."));
	}

	@Command(
			aliases = "npc",
			usage = "add [text]",
			desc = "add text to an NPC",
			modifiers = "add",
			min = 2)
	@CommandPermissions("basic.modify.addtext")
	public static void add(CommandContext args, Player player, HumanNPC npc) {
		String text = args.getJoinedStrings(1);
		NPCDataManager.addText(npc.getUID(), text);
		player.sendMessage(StringUtils.wrap(text) + " was added to "
				+ StringUtils.wrap(npc.getStrippedName() + "'s") + " text.");
	}

	@Command(
			aliases = "npc",
			usage = "reset",
			desc = "reset the text of an NPC",
			modifiers = "reset",
			min = 1,
			max = 1)
	@CommandPermissions("basic.modify.resettext")
	public static void reset(CommandContext args, Player player, HumanNPC npc) {
		NPCDataManager.resetText(npc.getUID());
		player.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s")
				+ " text was reset!");
	}

	@Command(
			aliases = "npc",
			usage = "item [item]",
			desc = "set the item in an NPC's hand",
			modifiers = "item",
			min = 2,
			max = 2)
	@CommandPermissions("basic.modify.item")
	public static void item(CommandContext args, Player player, HumanNPC npc) {
		NPCDataManager.setItemInHand(player, npc, args.getString(1));
	}

	@Command(
			aliases = "npc",
			usage = "armor [armor] [item]",
			desc = "set the armor of an NPC",
			modifiers = "armor",
			min = 3,
			max = 3)
	@CommandPermissions("basic.modify.armor")
	public static void armor(CommandContext args, Player player, HumanNPC npc) {
		Material mat = StringUtils.parseMaterial(args.getString(2));
		if (mat == null) {
			player.sendMessage(ChatColor.RED + "Invalid item.");
			return;
		}
		if (mat != Material.AIR && !player.getInventory().contains(mat)) {
			player.sendMessage(ChatColor.RED
					+ "You need to have the item in your inventory to add it to the NPC.");
			return;
		}
		if ((mat.getId() < 298 || mat.getId() > 317)
				&& (mat.getId() != 86 && mat.getId() != 91)) {
			player.sendMessage(ChatColor.GRAY
					+ "That can't be used as an armor material.");
			return;
		}
		int slot = player.getInventory().first(mat);
		ItemStack item = InventoryUtils.decreaseItemStack(player.getInventory()
				.getItem(slot));
		player.getInventory().setItem(slot, item);
		ArrayList<Integer> items = npc.getNPCData().getItems();

		if (args.getString(1).contains("helm")) {
			items.set(1, mat.getId());
		} else if (args.getString(1).equalsIgnoreCase("torso")) {
			items.set(2, mat.getId());
		} else if (args.getString(1).contains("leg")) {
			items.set(3, mat.getId());
		} else if (args.getString(1).contains("boot")) {
			items.set(4, mat.getId());
		}
		npc.getNPCData().setItems(items);
		NPCDataManager.addItems(npc, items);

		// Despawn the old NPC, register our new one.
		NPCManager.removeForRespawn(npc.getUID());
		NPCManager.register(npc.getUID(), npc.getOwner());

		player.sendMessage(StringUtils.wrap(npc.getStrippedName())
				+ "'s armor was set to "
				+ StringUtils.wrap(MessageUtils.getMaterialName(mat.getId()))
				+ ".");
	}

	@CommandRequirements(requireSelected = true, requireOwnership = true)
	@Command(
			aliases = "npc",
			usage = "tp",
			desc = "teleport to an NPC",
			modifiers = "tp",
			min = 1,
			max = 1)
	@CommandPermissions("basic.use.teleport")
	public static void teleport(CommandContext args, Player player, HumanNPC npc) {
		player.teleport(npc.getNPCData().getLocation());
		player.sendMessage(ChatColor.GREEN + "Teleported you to "
				+ StringUtils.wrap(npc.getStrippedName()) + ". Enjoy!");
	}

	@Command(
			aliases = "npc",
			usage = "talkclose",
			desc = "toggle an NPC's talk-when-close setting",
			modifiers = "talkclose",
			min = 1,
			max = 1)
	@CommandPermissions("basic.modify.talkclose")
	public static void talkClose(CommandContext args, Player player,
			HumanNPC npc) {
		npc.getNPCData().setTalkClose(!npc.getNPCData().isTalkClose());
		if (npc.getNPCData().isTalkClose()) {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will now talk to nearby players.");
		} else {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will stop talking to nearby players.");
		}
	}

	@Command(
			aliases = "npc",
			usage = "lookat",
			desc = "set an NPC's look-when-close setting",
			modifiers = "lookat",
			min = 1,
			max = 1)
	@CommandPermissions("basic.modify.lookat")
	public static void lookAt(CommandContext args, Player player, HumanNPC npc) {
		npc.getNPCData().setLookClose(!npc.getNPCData().isLookClose());
		if (npc.getNPCData().isLookClose()) {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will now look at players.");
		} else {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " will stop looking at players.");
		}
	}

	@Command(
			aliases = "npc",
			usage = "select [id]",
			desc = "select an NPC by its ID",
			modifiers = "select",
			min = 2,
			max = 2)
	@CommandPermissions("basic.use.select")
	@CommandRequirements()
	public static void select(CommandContext args, Player player, HumanNPC npc) {
		npc = NPCManager.get(args.getInteger(1));
		if (npc == null) {
			player.sendMessage(ChatColor.RED + "No NPC with the ID "
					+ StringUtils.wrap(args.getString(1), ChatColor.RED)
					+ " exists.");
		} else {
			NPCDataManager.selectNPC(player, npc);
			Messaging.send(player, npc,
					SettingsManager.getString("SelectionMessage"));
		}
	}

	@CommandRequirements(requireSelected = true, requireOwnership = true)
	@Command(
			aliases = "npc",
			usage = "setowner [name]",
			desc = "set the owner of an NPC",
			modifiers = "setowner",
			min = 2,
			max = 2)
	@CommandPermissions("basic.modify.setowner")
	public static void setOwner(CommandContext args, Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN + "The owner of "
				+ StringUtils.wrap(npc.getStrippedName()) + " is now "
				+ StringUtils.wrap(args.getString(1)) + ".");
		npc.getNPCData().setOwner(args.getString(1));
	}

	@Command(
			aliases = "npc",
			usage = "/npc [path|waypoints] (reset)",
			desc = "toggle waypoint editing",
			modifiers = { "path", "waypoints" },
			min = 1,
			max = 2)
	@CommandPermissions("waypoints.edit")
	public static void waypoints(CommandContext args, Player player,
			HumanNPC npc) {
		if (args.length() == 2) {
			Integer editing = NPCDataManager.pathEditors.get(player.getName());
			int UID = npc.getUID();
			if (editing == null) {
				player.sendMessage(ChatColor.AQUA
						+ StringUtils.listify("Waypoint Editing Controls"));
				player.sendMessage(StringUtils.wrap("Left")
						+ " click adds a waypoint, while "
						+ StringUtils.wrap("right") + " click acts as an undo.");
				player.sendMessage(StringUtils.wrap("Repeat")
						+ " this command to finish.");
				editing = UID;
				npc.setPaused(true);
			} else if (editing == UID) {
				player.sendMessage(StringUtils.wrap("Finished")
						+ " editing waypoints.");
				editing = null;
				npc.setPaused(false);
			} else if (editing != UID) {
				player.sendMessage(ChatColor.GRAY + "Now editing "
						+ StringUtils.wrap(npc.getStrippedName())
						+ "'s waypoints.");
				editing = UID;
			}
			NPCDataManager.pathEditors.put(player.getName(), editing);
		} else if (args.length() >= 3 && args.getString(1).equals("reset")) {
			npc.getWaypoints().resetWaypoints();
			player.sendMessage(ChatColor.GREEN + "Waypoints "
					+ StringUtils.wrap("reset") + ".");
		}
	}

	@CommandRequirements()
	@Command(
			aliases = "npc",
			usage = "list (name) (page)",
			desc = "view a list of NPCs for a player",
			modifiers = "list",
			min = 1,
			max = 3)
	@CommandPermissions("basic.use.list")
	public static void list(CommandContext args, Player player, HumanNPC npc) {
		switch (args.argsLength()) {
		case 1:
			MessageUtils.displayNPCList(player, player, npc, "1");
			break;
		case 2:
			if (StringUtils.isNumber(args.getString(1))) {
				MessageUtils.displayNPCList(player, player, npc,
						args.getString(1));
			} else {
				if (ServerUtils.matchPlayer(args.getString(1)) != null) {
					MessageUtils.displayNPCList(player,
							ServerUtils.matchPlayer(args.getString(1)), npc,
							"1");
				} else {
					player.sendMessage(ChatColor.RED
							+ "Could not match player.");
				}
			}
			break;
		case 3:
			if (ServerUtils.matchPlayer(args.getString(1)) != null) {
				MessageUtils.displayNPCList(player,
						ServerUtils.matchPlayer(args.getString(1)), npc,
						args.getString(2));
			} else {
				player.sendMessage(ChatColor.RED + "Could not match player.");
			}
			break;
		}
	}
}