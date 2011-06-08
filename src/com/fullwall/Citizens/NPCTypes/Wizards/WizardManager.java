package com.fullwall.Citizens.NPCTypes.Wizards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardManager {

	/**
	 * Teleport a player to one of a wizard's locations
	 * 
	 * @param player
	 * @param npc
	 */
	public static boolean teleportPlayer(Player player, HumanNPC npc) {
		if (decreaseMana(player, npc, 5)) {
			player.teleport(npc.getWizard().getCurrentLocation());
			return true;
		}
		return false;
	}

	/**
	 * Change the time in the player's world
	 * 
	 * @param player
	 * @param npc
	 */
	public static boolean changeTime(Player player, HumanNPC npc) {
		long time = 0;
		if (npc.getWizard().getTime().equals("day")) {
			time = 5000;
		} else if (npc.getWizard().getTime().equals("night")) {
			time = 13000;
		} else if (npc.getWizard().getTime().equals("morning")) {
			time = 0;
		} else if (npc.getWizard().getTime().equals("afternoon")) {
			time = 10000;
		}
		if (decreaseMana(player, npc, 5)) {
			player.getWorld().setTime(time);
			return true;
		}
		return false;
	}

	/**
	 * Spawn mob(s) at the specified location
	 * 
	 * @param player
	 * @param npc
	 */
	public static boolean spawnMob(Player player, HumanNPC npc) {
		if (decreaseMana(player, npc, 5)) {
			player.getWorld().spawnCreature(player.getLocation(),
					npc.getWizard().getMob());
			return true;
		}
		return false;
	}

	/**
	 * Toggle a storm in the player's world
	 * 
	 * @param player
	 * @param npc
	 */
	public static boolean toggleStorm(Player player, HumanNPC npc) {
		if (decreaseMana(player, npc, 5)) {
			player.getWorld().setStorm(!player.getWorld().hasStorm());
			return true;
		}
		return false;
	}

	/**
	 * Increase the mana of a wizard
	 * 
	 * @param mana
	 */
	public static void increaseMana(HumanNPC npc, int mana) {
		if (npc.getWizard().getMana() + mana < Constants.maxWizardMana) {
			npc.getWizard().setMana(npc.getWizard().getMana() + mana);
		}
	}

	/**
	 * Decrease the mana of a wizard
	 * 
	 * @param mana
	 */
	public static boolean decreaseMana(Player player, HumanNPC npc, int mana) {
		if (npc.getWizard().getMana() - mana >= 0) {
			npc.getWizard().setMana(npc.getWizard().getMana() - mana);
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has lost 5 mana.");
			return true;
		} else {
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " does not have enough mana to do that.");
			return false;
		}
	}

	/**
	 * Purchase a teleport
	 * 
	 * @param player
	 * @param wizard
	 * @param op
	 */
	public static void buy(Player player, HumanNPC npc, Operation op) {
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(op, player);
				if (paid > 0) {
					boolean canSend = false;
					String msg = ChatColor.GREEN
							+ "Paid "
							+ StringUtils.wrap(EconomyHandler.getPaymentType(
									op, "" + paid, ChatColor.YELLOW));
					switch (op) {
					case WIZARD_TELEPORT:
						msg += " for a teleport to "
								+ StringUtils.wrap(npc.getWizard()
										.getCurrentLocationName()) + ".";
						if (teleportPlayer(player, npc)) {
							canSend = true;
						}
						break;
					case WIZARD_SPAWNMOB:
						msg += " to spawn a "
								+ StringUtils
										.wrap(npc.getWizard().getMob().name()
												.toLowerCase()
												.replace("_", " ")) + ".";
						if (spawnMob(player, npc)) {
							canSend = true;
						}
						break;
					case WIZARD_CHANGETIME:
						msg += " to change the time to "
								+ StringUtils.wrap(npc.getWizard().getTime())
								+ ".";
						if (changeTime(player, npc)) {
							canSend = true;
						}
						break;
					case WIZARD_TOGGLESTORM:
						msg += " toggled a thunderstorm in the world "
								+ StringUtils.wrap(player.getWorld().getName())
								+ ".";
						if (toggleStorm(player, npc)) {
							canSend = true;
						}
						break;
					default:
						player.sendMessage(ChatColor.RED
								+ "No valid mode selected.");
						break;
					}
					if (canSend) {
						player.sendMessage(msg);
					}
				}
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
			return;
		}
	}
}