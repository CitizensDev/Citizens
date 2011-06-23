package com.fullwall.Citizens.Commands.Commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class ToggleCommands {

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
			npc = NPCManager.get(NPCManager.selectedNPCs.get(player.getName()));
		else {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			player.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED
					+ "You didn't specify an NPC type to toggle.");
			return true;
		} else {
			if (args[0].equalsIgnoreCase("trader")) {
				if (Permission.canCreate(player, "trader")) {
					if (!PropertyManager.typeExists(npc, "trader")) {
						buyState(player, npc.getTrader(),
								Operation.TRADER_CREATION);
					} else {
						toggleState(player, npc.getTrader());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args[0].equalsIgnoreCase("quester")) {
				if (Permission.canCreate(player, "quester")) {
					if (!PropertyManager.typeExists(npc, "quester")) {
						buyState(player, npc.getQuester(),
								Operation.QUESTER_CREATION);
					} else {
						toggleState(player, npc.getQuester());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args[0].equalsIgnoreCase("healer")) {
				if (Permission.canCreate(player, "healer")) {
					if (!PropertyManager.typeExists(npc, "healer")) {
						buyState(player, npc.getHealer(),
								Operation.HEALER_CREATION);
					} else {
						toggleState(player, npc.getHealer());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args[0].equalsIgnoreCase("guard")) {
				if (Permission.canCreate(player, "guard")) {
					if (!PropertyManager.typeExists(npc, "guard")) {
						buyState(player, npc.getGuard(),
								Operation.GUARD_CREATION);
					} else {
						toggleState(player, npc.getGuard());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args[0].equalsIgnoreCase("wizard")) {
				if (Permission.canCreate(player, "wizard")) {
					if (!PropertyManager.typeExists(npc, "wizard")) {
						buyState(player, npc.getWizard(),
								Operation.WIZARD_CREATION);
					} else {
						toggleState(player, npc.getWizard());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args[0].equalsIgnoreCase("blacksmith")) {
				if (Permission.canCreate(player, "blacksmith")) {
					if (!PropertyManager.typeExists(npc, "blacksmith")) {
						buyState(player, npc.getBlacksmith(),
								Operation.BLACKSMITH_CREATION);
					} else {
						toggleState(player, npc.getBlacksmith());
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else if (args.length == 2 && args[0].equalsIgnoreCase("all")) {
				if (Permission.isAdmin(player)) {
					if (args[1].equals("on")) {
						toggleAll(npc, player, true);
					} else if (args[1].equalsIgnoreCase("off")) {
						toggleAll(npc, player, false);
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			} else {
				player.sendMessage(ChatColor.RED
						+ "Entered NPC type was not recognized.");
				returnval = true;
			}
		}
		PropertyManager.save(npc);
		return returnval;
	}

	/**
	 * Toggles an NPC state.
	 * 
	 * @param player
	 * @param toggleable
	 */
	private void toggleState(Player player, Toggleable toggleable) {
		if (toggleable == null) {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return;
		}
		toggleable.toggle();
		toggleable.saveState();
		if (toggleable.getToggle()) {
			player.sendMessage(StringUtils.wrap(toggleable.getName())
					+ " is now a " + toggleable.getType() + "!");
		} else {
			player.sendMessage(StringUtils.wrap(toggleable.getName())
					+ " has stopped being a " + toggleable.getType() + ".");
		}
	}

	/**
	 * Buys an NPC state.
	 * 
	 * @param player
	 * @param toggleable
	 * @param op
	 */
	private void buyState(Player player, Toggleable toggleable, Operation op) {
		if (toggleable == null) {
			player.sendMessage(MessageUtils.mustHaveNPCSelectedMessage);
			return;
		}
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(op, player);
				if (paid > 0) {
					player.sendMessage(MessageUtils.getPaidMessage(op, paid,
							toggleable.getName(), toggleable.getType(), true));
				}
				toggleable.register();
				toggleState(player, toggleable);
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
		}
	}

	/**
	 * Toggles all types of NPCs
	 * 
	 * @param npc
	 * @param player
	 * @param on
	 */
	private void toggleAll(HumanNPC npc, Player player, boolean on) {
		Map<String, Toggleable> toggle = new HashMap<String, Toggleable>();
		toggle.put("blacksmith", npc.getBlacksmith());
		toggle.put("guard", npc.getGuard());
		toggle.put("healer", npc.getHealer());
		toggle.put("quester", npc.getQuester());
		toggle.put("trader", npc.getTrader());
		toggle.put("wizard", npc.getWizard());
		if (on) {
			for (Toggleable t : toggle.values()) {
				if (!t.getToggle()) {
					toggleState(player, toggle.get(t.getType()));
				}
			}
		} else {
			for (Toggleable t : toggle.values()) {
				if (t.getToggle()) {
					toggleState(player, toggle.get(t.getType()));
				}
			}
		}
	}
}