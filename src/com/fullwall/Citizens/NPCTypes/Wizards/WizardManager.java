package com.fullwall.Citizens.NPCTypes.Wizards;

import org.bukkit.entity.Player;

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
		// TODO decide proper way to set time....ask player to enter command
		// specifying the time first?
		// player.getWorld().setTime();
		decreaseMana(player, npc, 5);
	}

	/**
	 * Spawn mob(s) at the specified location
	 * 
	 * @param player
	 * @param npc
	 */
	public static void spawnMob(Player player, HumanNPC npc) {
		// TODO decide proper way to spawn mob....ask player to enter command
		// specifying mob to spawn and location first?
		// player.getWorld().spawnCreature();
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
}
