package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.NPCTypes.Blacksmiths.BlacksmithNPC;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;

	public BlacksmithExecutor(Citizens plugin) {
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
			npc = NPCManager.get(NPCManager.selectedNPCs.get(player.getName()));
		} else {
			sender.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			sender.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isBlacksmith()) {
			sender.sendMessage(ChatColor.RED
					+ "Your NPC isn't a blacksmith yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				if (Permission.hasPermission("citizens.blacksmith.create",
						sender)) {
					HelpUtils.sendBlacksmithHelp(player);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				return true;
			} else if (args.length == 2 && args[0].contains("repair")) {
				if (Permission.hasPermission("citizens.blacksmith.repair",
						sender)) {
					repairArmor(player, npc, args[1]);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
				if (Permission.hasPermission("citizens.blacksmith.repair",
						sender)) {
					listValidArmorNames(player);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("uses")) {
				if (Permission
						.hasPermission("citizens.blacksmith.uses", sender)) {
					showUsesRemaining(player, npc.getBlacksmith(),
							Material.getMaterial(player.getItemInHand()
									.getTypeId()));
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			PropertyManager.save(npc);
		}
		return returnval;
	}

	/**
	 * Repair a player's armor
	 * 
	 * @param player
	 * @param npc
	 * @param armor
	 */
	private void repairArmor(Player player, HumanNPC npc, String armor) {
		PlayerInventory inv = player.getInventory();
		if (armor.equalsIgnoreCase("helmet") || armor.equalsIgnoreCase("cap")) {
			buyArmorRepair(player, npc, inv.getHelmet(), armor, false);
		} else if (armor.equalsIgnoreCase("chestplate")
				|| armor.equalsIgnoreCase("torso")
				|| armor.equalsIgnoreCase("tunic")) {
			buyArmorRepair(player, npc, inv.getChestplate(), armor, false);
		} else if (armor.equalsIgnoreCase("leggings")
				|| armor.equalsIgnoreCase("pants")) {
			buyArmorRepair(player, npc, inv.getLeggings(), armor, true);
		} else if (armor.equalsIgnoreCase("boots")
				|| armor.equalsIgnoreCase("shoes")) {
			buyArmorRepair(player, npc, inv.getBoots(), armor, true);
		} else {
			player.sendMessage(ChatColor.RED + "Invalid armor type.");
		}
	}

	/**
	 * Buy an armor repair
	 * 
	 * @param player
	 * @param npc
	 * @param armor
	 * @param armorName
	 * @param plural
	 */
	private void buyArmorRepair(Player player, HumanNPC npc, ItemStack armor,
			String armorName, boolean plural) {
		String msg = "";
		if (!EconomyHandler.useEconomy()
				|| EconomyHandler.canBuy(Operation.BLACKSMITH_ARMORREPAIR,
						player)) {
			if (EconomyHandler.useEconomy()) {
				if (npc.getBlacksmith().validateArmor(armor)) {
					if (armor.getDurability() > 0) {
						double paid = EconomyHandler.pay(
								Operation.BLACKSMITH_ARMORREPAIR, player);
						if (paid > 0) {
							armor.setDurability((short) 0);
							msg = ChatColor.GREEN + ("Your ")
									+ StringUtils.wrap(armorName);
							if (plural) {
								msg += " have been repaired by ";
							} else {
								msg += " has been repaired by ";
							}
							msg += StringUtils.wrap(npc.getStrippedName())
									+ " for "
									+ StringUtils
											.wrap(EconomyHandler
													.getPaymentType(
															Operation.BLACKSMITH_ARMORREPAIR,
															"" + paid,
															ChatColor.YELLOW))
									+ ".";
							player.sendMessage(msg);
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "Your armor is already fully repaired.");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "You are not wearing that type of armor.");
				}
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(
					Operation.BLACKSMITH_ARMORREPAIR, player));
			return;
		}
	}

	/**
	 * Lists the available armor names to use with the repair armor command
	 * 
	 * @param player
	 */
	private void listValidArmorNames(Player player) {
		player.sendMessage(ChatColor.RED + "=====[ " + ChatColor.WHITE
				+ "Valid Armor Names" + ChatColor.RED + " ]=====");
		player.sendMessage(ChatColor.RED + "Helmet: " + ChatColor.WHITE
				+ "helmet, cap");
		player.sendMessage(ChatColor.RED + "Chestplate: " + ChatColor.WHITE
				+ "chestplate, torso, tunic");
		player.sendMessage(ChatColor.RED + "Leggings: " + ChatColor.WHITE
				+ "leggings, pants");
		player.sendMessage(ChatColor.RED + "Boots: " + ChatColor.WHITE
				+ "boots, shoes");
	}

	private void showUsesRemaining(Player player, BlacksmithNPC blacksmith,
			Material material) {
		ItemStack item = player.getItemInHand();
		String itemName = item.getType().name().toLowerCase().replace("_", " ");
		if (blacksmith.validateTool(item) || blacksmith.validateArmor(item)) {
			player.sendMessage(ChatColor.GREEN
					+ "Your "
					+ StringUtils.wrap(itemName)
					+ " has "
					+ StringUtils.wrap(material.getMaxDurability()
							- item.getDurability()) + " uses remaining.");
		} else {
			player.sendMessage(ChatColor.RED + itemName
					+ " does not have a durability.");
		}
	}
}