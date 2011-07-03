package com.citizens.Pathfinding;

public abstract class PathFinder {
	protected final PathHeuristic heuristic;
	protected final PathPlayer player;
	protected final PathWorld pathWorld;

	public PathFinder(PathHeuristic heuristic, PathPlayer player,
			PathWorld pathWorld) {
		this.heuristic = heuristic;
		this.player = player;
		this.pathWorld = pathWorld;
	}

	public abstract boolean find();

	public abstract Path path();

	public abstract boolean valid(Point point);

	public abstract void recalculate(Point start, Point finish);

	public abstract void recalculate(PathWorld world, Point start, Point end);

	public interface PathHeuristic {
		public int calculate(Point first, Point second, PathWorld pathWorld,
				PathPlayer player, boolean endPoint);
	}

	public interface PathPlayer {
	}

	public interface PathWorld {
	}
}