package com.citizens.Pathfinding.Citizens;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.citizens.Pathfinding.PathFinder.PathHeuristic;
import com.citizens.Pathfinding.PathFinder.PathPlayer;
import com.citizens.Pathfinding.PathFinder.PathWorld;
import com.citizens.Pathfinding.Point;

public class CitizensPathHeuristic implements PathHeuristic {

	@Override
	public int calculate(Point first, Point second, PathWorld pathWorld,
			PathPlayer player, boolean endPoint) {
		World world = getWorld(pathWorld);
		if (first.equals(second))
			return 1;
		Block one = world.getBlockAt(first.x, first.y, first.z);
		Block two = world.getBlockAt(second.x, second.y, second.z);
		int ret = 1;
		ret += first.distanceSquared(second);

		// dark spaces are more likely to be dangerous.
		ret += (two.getLightLevel() - one.getLightLevel());
		if (first.y != second.y)
			ret += 5; // This favours not jumping. Good thing? Perhaps.
		switch (two.getType()) {
		case AIR:
			if (two.getFace(BlockFace.UP).getTypeId() == 0)
				ret -= 5; // we want to favour open spaces.
			break;
		case LAVA:
		case STATIONARY_LAVA:
		case FIRE:
		case TNT:
			ret += 15; // all can kill us.
			break;
		case WATER:
		case STATIONARY_WATER:
			ret += 10; // still bad, but not immediately life-threatening.
			break;
		}
		return ret;
	}

	private World getWorld(PathWorld world) {
		return ((MinecraftPathWorld) world).getWorld();
	}
}
