package com.fullwall.Citizens.NPCTypes.Wizards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
	public static void teleportPlayer(Player player, HumanNPC npc) {
		player.teleport(npc.getWizard().getCurrentLocation());
		decreaseMana(player, npc, 5);
	}

	/**
	 * Change the time in the player's world
	 * 
	 * @param player
	 * @param npc
	 */
	public static void changeTime(Player player, HumanNPC npc) {
		long time = 0;
		if (npc.getWizard().getTime().equals("day")) {
			time = 12L;
		} else if (npc.getWizard().getTime().equals("night")) {
			time = 0L;
		} else if (npc.getWizard().getTime().equals("morning")) {
			time = 6L;
		} else if (npc.getWizard().getTime().equals("afternoon")) {
			time = 18L;
		} else {
			time = 0L;
		}
		player.getWorld().setTime(time);
		decreaseMana(player, npc, 5);
	}

	/**
	 * Spawn mob(s) at the specified location
	 * 
	 * @param player
	 * @param npc
	 */
	public static void spawnMob(Player player, HumanNPC npc) {
		player.getWorld().spawnCreature(player.getLocation(),
				npc.getWizard().getMob());
		decreaseMana(player, npc, 5);
	}

	/**
	 * Toggle a storm in the player's world
	 * 
	 * @param player
	 * @param npc
	 */
	public static void toggleStorm(Player player, HumanNPC npc) {
		player.getWorld().setStorm(!player.getWorld().hasStorm());
		decreaseMana(player, npc, 5);
	}

	/**
	 * Increase the mana of a wizard
	 * 
	 * @param mana
	 */
	public static void increaseMana(HumanNPC npc, int mana) {
		npc.getWizard().setMana(npc.getWizard().getMana() + mana);
	}

	/**
	 * Decrease the mana of a wizard
	 * 
	 * @param mana
	 */
	public static void decreaseMana(Player player, HumanNPC npc, int mana) {
		if (npc.getWizard().getMana() - mana > 0) {
			npc.getWizard().setMana(npc.getWizard().getMana() - mana);
		} else {
			player.sendMessage(npc.getStrippedName()
					+ " does not have enough mana to do that.");
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
					String msg = ChatColor.GREEN
							+ "Paid "
							+ StringUtils.wrap(EconomyHandler.getPaymentType(
									op, "" + paid, ChatColor.YELLOW));
					switch (op) {
					case WIZARD_TELEPORT:
						msg += " for a teleport to "
								+ StringUtils.wrap(npc.getWizard()
										.getCurrentLocationName()) + ".";
						teleportPlayer(player, npc);
						break;
					case WIZARD_SPAWNMOB:
						msg += " to spawn ";
						spawnMob(player, npc);
						break;
					case WIZARD_CHANGETIME:
						msg += " to change the time to ";
						changeTime(player, npc);
						break;
					case WIZARD_TOGGLESTORM:
						msg += " toggled a thunderstorm in the world "
								+ StringUtils.wrap(player.getWorld().getName());
						toggleStorm(player, npc);
						break;
					default:
						player.sendMessage(ChatColor.RED
								+ "No valid mode selected.");
						break;
					}
					msg += StringUtils.wrap(npc.getStrippedName())
							+ " has lost 5 mana.";
					player.sendMessage(msg);
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