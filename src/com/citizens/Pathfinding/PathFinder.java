package com.citizens.Pathfinding;

public abstract class PathFinder {
	protected final PathHeuristic heuristic;
	protected Point start;
	protected Point end;
	protected final PathWorld world;
	protected final PathPlayer player;

	public PathFinder(Point start, Point end, PathHeuristic heuristic,
			PathPlayer player, PathWorld pathWorld) {
		this.start = start;
		this.end = end;
		this.heuristic = heuristic;
		this.player = player;
		this.world = pathWorld;
	}

	public abstract boolean find();

	public abstract Path path();

	public abstract boolean valid(Point point);

	public abstract void recalculate(Point start, Point finish);

	public interface PathHeuristic {
		public int calculate(Point first, Point second, PathWorld pathWorld,
				PathPlayer player);
	}

	public interface PathPlayer {
	}

	public interface PathWorld {
	}
}
