package com.citizens.Pathfinding.Citizens;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.World;

import com.citizens.Pathfinding.Path;
import com.citizens.Pathfinding.PathFinder;
import com.citizens.Pathfinding.PathNode;
import com.citizens.Pathfinding.Point;

public class BinaryPathFinder extends PathFinder {
	private final PriorityBuffer<PathNode> paths;
	private final HashMap<Point, Integer> mindists = new HashMap<Point, Integer>();
	private final ComparableComparator<PathNode> comparator = new ComparableComparator<PathNode>();
	private Path lastPath;
	private Point start, end;
	private final byte SIZE_INCREMENT = 20;
	private final World world;

	public BinaryPathFinder(CitizensPathHeuristic heuristic,
			NPCPathPlayer player, MinecraftPathWorld pathWorld) {
		super(heuristic, player, pathWorld);
		this.world = pathWorld.getWorld();
		paths = new PriorityBuffer<PathNode>(8000, comparator);
	}

	@Override
	public boolean find() {
		try {
			PathNode root = new PathNode();
			root.point = start;
			calculateTotalCost(root, start, start, false);
			expand(root);
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
		return !isSolid(world.getBlockTypeIdAt(point.x, point.y, point.z));
	}

	@Override
	public void recalculate(Point start, Point end) {
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
		if (min == null || min > path.totalCost)
			mindists.put(p, path.totalCost);
		else
			return;
		Point[] successors = generateSuccessors(p);
		for (Point t : successors) {
			if (t == null)
				continue;
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

	private Point[] generateSuccessors(Point point) {
		Point[] points = new Point[27];
		Point temp = null;
		byte counter = -1;
		for (int x = point.x - 1; x <= point.x + 1; ++x) {
			for (int y = point.y + 1; y >= point.y - 1; --y) {
				for (int z = point.z - 1; z <= point.z + 1; ++z) {
					++counter;
					if (x == 0 && y == 0 && z == 0)
						continue;
					temp = new Point(x, y, z);
					if (valid(temp))
						points[counter] = temp;
				}
			}
		}
		return points;
	}

	private boolean isSolid(int id) {
		if (id < 1) {
			return false;
		}
		if ((Material.getMaterial(id) != null)
				&& (net.minecraft.server.Block.byId[id].a())) {
			return true;
		}
		return id == Material.GLASS.getId() || id == Material.LEAVES.getId();
	}

	private boolean isGoal(Point last) {
		return last.equals(this.end);
	}
}