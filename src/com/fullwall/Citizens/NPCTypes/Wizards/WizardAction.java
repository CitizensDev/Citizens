package com.fullwall.Citizens.NPCTypes.Wizards;

import org.bukkit.entity.Player;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardAction {

	public static void teleportPlayer(Player player, HumanNPC npc) {
		player.teleport(npc.getWizard().getCurrentLocation());
	}

	public static void changeTime(Player player, HumanNPC npc) {
		// TODO decide proper way to set time....ask player to enter command
		// specifying the time first?
		// player.getWorld().setTime();
	}

	public static void spawnMob(Player player, HumanNPC npc) {
		// TODO decide proper way to spawn mob....ask player to enter command
		// specifying mob to spawn and location first?
		// player.getWorld().spawnCreature();
	}

	public static void toggleStorm(Player player) {
		player.getWorld().setStorm(!player.getWorld().hasStorm());
	}
}