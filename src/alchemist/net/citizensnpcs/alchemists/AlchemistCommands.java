package net.citizensnpcs.alchemists;

import java.util.HashMap;
import java.util.Map.Entry;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;
import net.citizensnpcs.resources.sk89q.ServerCommand;
import net.citizensnpcs.utils.HelpUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PageUtils;
import net.citizensnpcs.utils.PageUtils.PageInstance;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRequirements(
		requireSelected = true,
		requireOwnership = true,
		requiredType = "alchemist")
public class AlchemistCommands extends CommandHandler {

	@CommandRequirements()
	@ServerCommand()
	@Command(
			aliases = { "alchemist", "alch" },
			usage = "help",
			desc = "view the alchemist help page",
			modifiers = "help",
			min = 1,
			max = 1)
	@CommandPermissions("alchemist.use.help")
	public static void alchemistHelp(CommandContext args, CommandSender sender,
			HumanNPC npc) {
		HelpUtils.sendHelp((Alchemist) npc.getType("alchemist"), sender, 1);
	}

	@CommandRequirements(requireSelected = true, requiredType = "alchemist")
	@Command(
			aliases = { "alchemist", "alch" },
			usage = "recipes (page)",
			desc = "view an alchemist's list of recipes",
			modifiers = "recipes",
			min = 1,
			max = 2)
	@CommandPermissions("alchemist.use.recipes")
	public static void recipes(CommandContext args, Player player, HumanNPC npc) {
		HashMap<Integer, String> recipes = ((Alchemist) npc
				.getType("alchemist")).getRecipes();
		if (recipes.size() == 0) {
			Messaging.sendError(player, npc.getStrippedName()
					+ " has no recipes.");
			return;
		}
		PageInstance instance = PageUtils.newInstance(player);
		int page = 1;
		if (args.argsLength() == 2) {
			if (!StringUtils.isNumber(args.getString(1))) {
				Messaging.sendError(player, "That is not a valid number.");
				return;
			}
			page = args.getInteger(1);
		}
		instance.header(ChatColor.GREEN
				+ StringUtils.listify(StringUtils.wrap(npc.getStrippedName()
						+ "'s Recipes <%x/%y>")));
		for (Entry<Integer, String> entry : recipes.entrySet()) {
			instance.push(ChatColor.GRAY + " - "
					+ MessageUtils.getMaterialName(entry.getKey()) + " (ID: "
					+ entry.getKey() + ")");
		}
		instance.push(ChatColor.GREEN + "Type "
				+ StringUtils.wrap("/alchemist select [itemID]")
				+ " to select a recipe.");
		instance.push(ChatColor.GREEN + "Type "
				+ StringUtils.wrap("/alchemist view")
				+ " to view ingredients for the recipe.");
		instance.process(page);
	}

	@CommandRequirements(requireSelected = true, requiredType = "alchemist")
	@Command(
			aliases = { "alchemist", "alch" },
			usage = "view (page)",
			desc = "view the selected alchemist recipe",
			modifiers = "view",
			min = 1,
			max = 2)
	@CommandPermissions("alchemist.use.recipes")
	public static void view(CommandContext args, Player player, HumanNPC npc) {
		int page = 1;
		if (args.argsLength() == 2) {
			if (!StringUtils.isNumber(args.getString(1))) {
				Messaging.sendError(player, "That is not a valid number.");
				return;
			}
			page = args.getInteger(1);
		}
		AlchemistManager.sendRecipeMessage(player,
				(Alchemist) npc.getType("alchemist"), page);
	}

	@Command(
			aliases = { "alchemist", "alch" },
			usage = "add [itemID] [itemID(:amt),]",
			desc = "add a custom recipe to an alchemist",
			modifiers = "add",
			min = 3,
			max = 3)
	@CommandPermissions("alchemist.modify.recipes")
	public static void add(CommandContext args, Player player, HumanNPC npc) {
		if (!AlchemistManager.checkValidID(player, args.getString(1))) {
			return;
		}
		int itemID = args.getInteger(1);
		Alchemist alchemist = npc.getType("alchemist");
		String recipe = args.getString(2);
		String[] items = recipe.split(",");
		for (String item : items) {
			String[] split = item.split(":");
			if (Material.getMaterial(StringUtils.parse(split[0])) == null
					|| (split[1].isEmpty() && !StringUtils.isNumber(split[1]))) {
				Messaging.sendError(player,
						"Could not read recipe. Did you use proper item IDs?");
				return;
			}
		}
		alchemist.addRecipe(itemID, recipe);
		player.sendMessage(StringUtils.wrap(npc.getStrippedName())
				+ " has changed the recipe for "
				+ StringUtils.wrap(MessageUtils.getMaterialName(itemID)) + ".");
	}

	@CommandRequirements(requireSelected = true, requiredType = "alchemist")
	@Command(
			aliases = { "alchemist", "alch" },
			usage = "select [itemID]",
			desc = "select a recipe",
			modifiers = "select",
			min = 2,
			max = 2)
	@CommandPermissions("alchemist.use.recipes.select")
	public static void select(CommandContext args, Player player, HumanNPC npc) {
		if (!AlchemistManager.checkValidID(player, args.getString(1))) {
			return;
		}
		int itemID = args.getInteger(1);
		Alchemist alchemist = npc.getType("alchemist");
		if (alchemist.getRecipe(itemID) == null) {
			Messaging.sendError(player, npc.getStrippedName()
					+ " does not have that recipe.");
			return;
		}
		alchemist.setCurrentRecipeID(itemID);
		player.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s")
				+ " recipe has been set to "
				+ StringUtils.wrap(MessageUtils.getMaterialName(itemID)) + ".");
	}

	@Override
	public void addPermissions() {
		PermissionManager.addPerm("alchemist.use.help");
		PermissionManager.addPerm("alchemist.use.recipes.select");
		PermissionManager.addPerm("alchemist.use.recipes.view");
		PermissionManager.addPerm("alchemist.modify.recipes");
		PermissionManager.addPerm("alchemist.use.interact");
	}

	@Override
	public void sendHelp(CommandSender sender, int page) {
		HelpUtils.header(sender, "Alchemist", 1, 1);
		HelpUtils.format(sender, "alchemist", "recipes",
				"view all of an alchemist's recipes");
		HelpUtils.format(sender, "alchemist", "select [itemID]",
				"select a recipe");
		HelpUtils.format(sender, "alchemist", "view (page)",
				"view an alchemist's selected recipe");
		HelpUtils.format(sender, "alchemist", "add [itemID] [itemID(:amt),]",
				"add a recipe to an alchemist");
		HelpUtils.footer(sender);
	}
}