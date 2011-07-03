package com.citizens.Pathfinding.Citizens;

import java.util.HashMap;
import java.util.Map;

import com.citizens.Pathfinding.Path;
import com.citizens.Pathfinding.PathFinder;
import com.citizens.Pathfinding.PathNode;
import com.citizens.Pathfinding.Point;
import com.citizens.Resources.sk89q.BlockType;
import com.citizens.Utils.Messaging;

public class BinaryPathFinder extends PathFinder {
	private final PriorityBuffer<PathNode> paths;
	private final Map<Point, Integer> mindists = new HashMap<Point, Integer>();
	private Path lastPath;
	private Point start, end;
	private final byte SIZE_INCREMENT = 20;
	private ChunkCache cache;

	public BinaryPathFinder(CitizensPathHeuristic heuristic,
			NPCPathPlayer player, CachedMinecraftPathWorld pathWorld) {
		super(heuristic, player, pathWorld);
		this.cache = pathWorld.getCache();
		paths = new PriorityBuffer<PathNode>(8000);
	}

	@Override
	public boolean find() {
		try {
			PathNode root = new PathNode();
			root.point = start;
			calculateTotalCost(root, start, start, false);
			expand(root);
			int iterations = 0;
			while (true) {
				PathNode p = paths.remove();
				if (p == null) {
					clear();
					return false;
				}
				Point last = p.point;
				if (isGoal(last)) {
					calculatePath(p); // Iterate back.
					clear();
					return true;
				}
				expand(p);
				++iterations;
				if (iterations > 20000) {
					Messaging.log("took too long");
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		clear();
		return false;
	}

	@Override
	public Path path() {
		return this.lastPath;
	}

	@Override
	public boolean valid(Point point) {
		return !isSolid(cache.getBlockId(point.x, point.y, point.z));
	}

	@Override
	public void recalculate(Point start, Point end) {
		this.start = start;
		this.end = end;
		this.lastPath = null;
	}

	@Override
	public void recalculate(PathWorld world, Point start, Point end) {
		this.cache = ((CachedMinecraftPathWorld) world).getCache();
		this.start = start;
		this.end = end;
		this.lastPath = null;
	}

	private void clear() {
		this.mindists.clear();
		this.paths.clear();
	}

	private void expand(PathNode path) {
		Point p = path.point;
		Integer min = mindists.get(p);
		if (min == null || min > path.totalCost) {
			mindists.put(p, path.totalCost);
		} else {
			return;
		}
		Point[] successors = generateSuccessors(path);
		for (Point t : successors) {
			if (t == null) {
				continue;
			}
			PathNode newPath = new PathNode(path);
			newPath.point = t;
			calculateTotalCost(newPath, p, t, false);
			paths.add(newPath);
		}
	}

	private void calculatePath(PathNode p) {
		Point[] retPath = new Point[20];
		Point[] copy = null;
		short added = 0;
		for (PathNode i = p; i != null; i = i.parent) {
			if (added >= retPath.length) {
				copy = new Point[retPath.length + SIZE_INCREMENT];
				System.arraycopy(retPath, 0, copy, 0, retPath.length);
				retPath = copy;
			}
			retPath[added++] = i.point;
		}
		this.lastPath = new Path(retPath);
	}

	private int calculateHeuristic(Point start, Point end, boolean endPoint) {
		return this.heuristic.calculate(start, end, this.pathWorld,
				this.player, endPoint);
	}

	private int calculateTotalCost(PathNode p, Point from, Point to,
			boolean endPoint) {
		int g = (calculateHeuristic(from, to, endPoint) + ((p.parent != null) ? p.parent.cost
				: 0));
		int h = calculateHeuristic(from, to, endPoint);
		p.cost = g;
		p.totalCost = (g + h);
		return p.totalCost;
	}

	private Point[] generateSuccessors(PathNode path) {
		Point[] points = new Point[27];
		Point point = path.point, temp = null;
		// boolean notNull = path.parent != null;
		byte counter = -1;
		for (int x = point.x - 1; x <= point.x + 1; ++x) {
			for (int y = point.y + 1; y >= point.y - 1; --y) {
				for (int z = point.z - 1; z <= point.z + 1; ++z) {
					++counter;
					temp = new Point(x, y, z);
					// if (notNull && path.parent.point.equals(temp))
					// continue;
					if (valid(temp)) {
						points[counter] = temp;
					}
				}
			}
		}
		return points;
	}

	private boolean isSolid(int id) {
		return !BlockType.canPassThrough(id);
	}

	private boolean isGoal(Point last) {
		return last.equals(this.end);
	}
}