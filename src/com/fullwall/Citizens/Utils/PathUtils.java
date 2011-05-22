package com.fullwall.Citizens.Utils;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Constants;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PathUtils {
	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks,
			int stationaryTicks, float range) {
		return npc.createPath(loc, pathTicks, stationaryTicks, range);
	}

	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks,
			int stationaryTicks) {
		return createPath(npc, loc, pathTicks, stationaryTicks,
				Constants.pathFindingRange);
	}

	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks) {
		return createPath(npc, loc, pathTicks, Constants.maxStationaryTicks);
	}

	public static boolean createPath(HumanNPC npc, Location loc) {
		return createPath(npc, loc, Constants.maxPathingTicks);
	}

	public static void targetPlayer(HumanNPC npc, Player player, boolean aggro,
			int pathTicks, int stationaryTicks, float range) {
		npc.targetPlayer((CraftPlayer) player, aggro, pathTicks,
				stationaryTicks, range);
	}

	public static void targetPlayer(HumanNPC npc, Player player, boolean aggro,
			int pathTicks, int stationaryTicks) {
		targetPlayer(npc, player, aggro, pathTicks, stationaryTicks,
				Constants.pathFindingRange);
	}

	public static void targetPlayer(HumanNPC npc, Player player, boolean aggro,
			int pathTicks) {
		targetPlayer(npc, player, aggro, pathTicks,
				Constants.maxStationaryTicks);
	}

	public static void targetPlayer(HumanNPC npc, Player player, boolean aggro) {
		targetPlayer(npc, player, aggro, Constants.maxPathingTicks);
	}

	public static boolean pathFinished(HumanNPC npc) {
		return npc.pathFinished();
	}

}
