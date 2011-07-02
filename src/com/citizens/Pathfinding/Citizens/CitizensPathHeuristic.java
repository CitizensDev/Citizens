package com.citizens.Pathfinding.Citizens;

import com.citizens.Pathfinding.PathFinder.PathHeuristic;
import com.citizens.Pathfinding.PathFinder.PathPlayer;
import com.citizens.Pathfinding.PathFinder.PathWorld;
import com.citizens.Pathfinding.Point;

public class CitizensPathHeuristic implements PathHeuristic {

	@Override
	public int calculate(Point first, Point second, PathWorld pathWorld,
			PathPlayer player, boolean endPoint) {
		if (first.equals(second))
			return 1;
		ChunkCache cache = getWorld(pathWorld);
		int two = cache.getBlockId(second.x, second.y, second.z);
		int ret = 1 + first.distanceSquared(second);
		if (endPoint)
			ret *= 2;

		// dark spaces are more likely to be dangerous.
		ret += (cache.getLightLevel(second.x, second.y, second.z) - cache
				.getLightLevel(first.x, first.y, first.z));
		if (first.y != second.y)
			ret += 5; // This favours not jumping. Good thing? Perhaps.
		switch (two) {
		case 0:
			if (cache.getBlockId(second.x, second.y + 1, second.z) == 0)
				ret -= 5; // we want to favour open spaces.
			break;
		case 8:
		case 9: // Water
		case 10:
		case 11: // Lava
		case 46: // TNT
		case 51: // Fire
			ret += 15; // all are dangerous.
			break;
		}
		return ret;
	}

	private ChunkCache getWorld(PathWorld world) {
		return ((CachedMinecraftPathWorld) world).getCache();
	}
}