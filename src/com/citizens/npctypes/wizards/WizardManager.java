package com.citizens.npctypes.wizards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.SettingsManager.Constant;
import com.citizens.economy.EconomyHandler;
import com.citizens.economy.EconomyHandler.Operation;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.StringUtils;

public class WizardManager {
	public enum WizardMode {
		/**
		 * Teleports players
		 */
		TELEPORT,
		/**
		 * Changes the time of the world
		 */
		TIME,
		/**
		 * Spawns mobs into the world
		 */
		SPAWN,
		/**
		 * Strikes lightning/makes it rain
		 */
		WEATHER;

		public static WizardMode parse(String string) {
			try {
				return WizardMode.valueOf(string.toUpperCase());
			} catch (Exception ex) {
				return null;
			}
		}
	}

	/**
	 * Teleport a player to one of a wizard's locations
	 * 
	 * @param player
	 * @param npc
	 */
	public static boolean teleportPlayer(Player player, HumanNPC npc) {
		if (decreaseMana(player, npc, 5)) {
			player.teleport(((WizardNPC) npc.getToggleable("wizard"))
					.getCurrentLocation());
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
		WizardNPC wizard = npc.getToggleable("wizard");
		if (wizard.getTime().equals("day")) {
			time = 5000;
		} else if (wizard.getTime().equals("night")) {
			time = 13000;
		} else if (wizard.getTime().equals("morning")) {
			time = 0;
		} else if (wizard.getTime().equals("afternoon")) {
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
					((WizardNPC) npc.getToggleable("wizard")).getMob());
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
	 * @param npc
	 * @param mana
	 */
	public static void increaseMana(HumanNPC npc, int mana) {
		WizardNPC wizard = npc.getToggleable("wizard");
		if (wizard != null
				&& wizard.getMana() + mana < Constant.MaxWizardMana.toInt()) {
			wizard.setMana(wizard.getMana() + mana);
		}
	}

	/**
	 * Decrease the mana of a wizard
	 * 
	 * @param player
	 * @param npc
	 * @param mana
	 */
	public static boolean decreaseMana(Player player, HumanNPC npc, int mana) {
		WizardNPC wizard = npc.getToggleable("wizard");
		if (wizard.hasUnlimitedMana()) {
			return true;
		}
		if (wizard.getMana() - mana >= 0) {
			wizard.setMana(wizard.getMana() - mana);
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has lost 5 mana.");
			return true;
		}
		player.sendMessage(StringUtils.wrap(npc.getStrippedName())
				+ " does not have enough mana to do that.");
		return false;
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
				boolean canSend = false;
				WizardNPC wizard = npc.getToggleable("wizard");
				double paid = EconomyHandler.pay(op, player);
				if (paid > 0 || EconomyHandler.isFree(player, op)) {
					String msg = ChatColor.GREEN
							+ "Paid "
							+ StringUtils.wrap(EconomyHandler.getPaymentType(
									op, "" + paid));
					switch (op) {
					case WIZARD_TELEPORT:
						msg += " for a teleport to "
								+ StringUtils.wrap(wizard
										.getCurrentLocationName()) + ".";
						if (teleportPlayer(player, npc)) {
							canSend = true;
						}
						break;
					case WIZARD_SPAWNMOB:
						msg += " to spawn a "
								+ StringUtils.wrap(wizard.getMob().name()
										.toLowerCase().replace("_", " ")) + ".";
						if (spawnMob(player, npc)) {
							canSend = true;
						}
						break;
					case WIZARD_CHANGETIME:
						msg += " to change the time to "
								+ StringUtils.wrap(wizard.getTime()) + ".";
						if (changeTime(player, npc)) {
							canSend = true;
						}
						break;
					case WIZARD_TOGGLESTORM:
						msg += " to toggle a thunderstorm in the world "
								+ StringUtils.wrap(player.getWorld().getName())
								+ ".";
						if (toggleStorm(player, npc)) {
							canSend = true;
						}
						break;
					default:
						msg = ChatColor.RED + "No valid mode selected.";
						canSend = true;
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
		}
	}
}