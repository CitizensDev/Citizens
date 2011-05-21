package com.fullwall.Citizens.Utils;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PathUtils {
	public static void createPath(HumanNPC npc, Location loc) {
		npc.createPath(loc);
	}

	public static void targetPlayer(HumanNPC npc, Player player, boolean aggro) {
		npc.targetPlayer((CraftPlayer) player, aggro);
	}

	public static boolean pathFinished(HumanNPC npc) {
		return npc.pathFinished();
	}

}
