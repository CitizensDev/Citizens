package com.fullwall.Citizens.Traders;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderInterface {

	public static void showInventory(HumanNPC NPC, Player player) {
		((CraftPlayer) player).getHandle()
				.a(NPC.getMinecraftEntity().inventory);
	}
}
