package net.citizensnpcs.wizards;

import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WizardManager {
	public enum WizardMode {
		/**
		 * Executes commands
		 */
		COMMAND,
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

	// Teleport a player to one of a wizard's locations
	private static boolean teleportPlayer(Player player, HumanNPC npc) {
		Wizard wizard = npc.getType("wizard");
		if (wizard.getNumberOfLocations() > 0) {
			if (decreaseMana(player, npc, 5)) {
				player.teleport(wizard.getCurrentLocation());
				return true;
			}
			return false;
		}
		Messaging.sendError(player, npc.getStrippedName()
				+ " has no locations.");
		return false;
	}

	// Change the time in the player's world
	private static boolean changeTime(Player player, HumanNPC npc) {
		long time = 0;
		Wizard wizard = npc.getType("wizard");
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

	// Spawn mob(s) at the specified location
	private static boolean spawnMob(Player player, HumanNPC npc) {
		if (decreaseMana(player, npc, 5)) {
			player.getWorld().spawnCreature(player.getLocation(),
					((Wizard) npc.getType("wizard")).getMob());
			return true;
		}
		return false;
	}

	// Toggle a storm in the player's world
	private static boolean toggleStorm(Player player, HumanNPC npc) {
		if (decreaseMana(player, npc, 5)) {
			player.getWorld().setStorm(!player.getWorld().hasStorm());
			return true;
		}
		return false;
	}

	// Executes a command
	private static boolean executeCommand(Player player, HumanNPC npc) {
		if (player
				.performCommand(((Wizard) npc.getType("wizard")).getCommand())) {
			return decreaseMana(player, npc, 5);
		}
		return false;
	}

	// Decrease the mana of a wizard
	private static boolean decreaseMana(Player player, HumanNPC npc, int mana) {
		Wizard wizard = npc.getType("wizard");
		if (wizard.hasUnlimitedMana()) {
			return true;
		}
		if (wizard.getMana() - mana >= 0) {
			wizard.setMana(wizard.getMana() - mana);
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " has lost " + mana + " mana.");
			return true;
		}
		player.sendMessage(StringUtils.wrap(npc.getStrippedName())
				+ " does not have enough mana to do that.");
		return false;
	}

	// Handle the right-clicking of a wizard
	public static void handleRightClick(Player player, HumanNPC npc, String op) {
		Wizard wizard = npc.getType("wizard");
		String econMsg = "";
		if (EconomyManager.useEconPlugin()) {
			if (EconomyManager
					.hasEnough(player, UtilityProperties.getPrice(op))) {
				double paid = EconomyManager.pay(player,
						UtilityProperties.getPrice(op));
				if (paid > 0) {
					econMsg = ChatColor.GREEN + "Paid "
							+ StringUtils.wrap(EconomyManager.format(paid))
							+ ":";
				}
			} else {
				player.sendMessage(MessageUtils.getNoMoneyMessage(player, op));
				return;
			}
		}
		String msg = StringUtils.wrap(npc.getStrippedName());
		if (op.equals("wizard.teleport")) {
			msg += " teleported you to "
					+ StringUtils.wrap(wizard.getCurrentLocationName()) + ".";
			if (!teleportPlayer(player, npc)) {
				return;
			}
		} else if (op.equals("wizard.spawnmob")) {
			msg += " spawned a "
					+ StringUtils.wrap(wizard.getMob().name().toLowerCase()
							.replace("_", " ")) + ".";
			if (!spawnMob(player, npc)) {
				return;
			}
		} else if (op.equals("wizard.changetime")) {
			msg += " changed the time to " + StringUtils.wrap(wizard.getTime())
					+ ".";
			if (!changeTime(player, npc)) {
				return;
			}
		} else if (op.equals("wizard.togglestorm")) {
			msg += " toggled a thunderstorm in the world "
					+ StringUtils.wrap(player.getWorld().getName()) + ".";
			if (!toggleStorm(player, npc)) {
				return;
			}
		} else if (op.equals("wizard.executecommand")) {
			msg += " executed the command "
					+ StringUtils.wrap("/" + wizard.getCommand()) + ".";
			if (!executeCommand(player, npc)) {
				return;
			}
		}
		if(EconomyManager.useEconPlugin()) {
			player.sendMessage(econMsg);
		}
		player.sendMessage(msg);
	}
}