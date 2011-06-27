package com.citizens.Pathfinding.Citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.bukkit.Material;
import org.bukkit.World;

import com.citizens.Pathfinding.Path;
import com.citizens.Pathfinding.PathFinder;
import com.citizens.Pathfinding.PathNode;
import com.citizens.Pathfinding.Point;

public class CitizensPathFinder extends PathFinder {
	private PriorityQueue<PathNode> paths = new PriorityQueue<PathNode>();
	private HashMap<Point, Integer> mindists = new HashMap<Point, Integer>();
	private int lastCost = 0;
	private Path lastPath;
	private int expandedCounter = 0;

	public CitizensPathFinder(Point start, Point end,
			CitizensPathHeuristic heuristic, NPCPathPlayer player,
			MinecraftPathWorld pathWorld) {
		super(start, end, heuristic, player, pathWorld);
	}

	@Override
	public boolean find() {
		try {
			PathNode root = new PathNode();
			root.point = start; /* Needed if the initial point has a cost.  */
			f(root, start, start);
			expand(root);
			while (true) {
				PathNode p = paths.poll();
				if (p == null) {
					this.lastCost = Integer.MAX_VALUE;
					return false;
				}
				Point last = p.point;
				this.lastCost = p.cost;
				if (isGoal(last)) {
					LinkedList<Point> retPath = new LinkedList<Point>();
					for (PathNode i = p; i != null; i = i.parent) {
						retPath.addFirst(i.point);
					}
					this.lastPath = new Path(retPath);
					return true;
				}
				expand(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Path path() {
		return this.lastPath;
	}

	@Override
	public boolean valid(Point point) {
		// TODO
		return true;
	}

	@Override
	public void recalculate(Point start, Point finish) {
		this.start = start;
		this.end = finish;
		this.mindists = new HashMap<Point, Integer>();
		this.lastCost = this.expandedCounter = 0;
		this.lastPath = null;
		this.paths = new PriorityQueue<PathNode>();
	}

	public int getLastCost() {
		return lastCost;
	}

	/** * Expand a path. * * @param path The path to expand. */
	private void expand(PathNode path) {
		Point p = path.point;
		Integer min = mindists.get(path.point); /*                                   * If a better path passing for this point already exists then                                   * don't expand it.                                   */
		if (min == null || min.doubleValue() > path.totalCost)
			mindists.put(path.point, path.totalCost);
		else
			return;
		List<Point> successors = generateSuccessors(p);
		for (Point t : successors) {
			PathNode newPath = new PathNode(path);
			newPath.point = t;
			f(newPath, path.point, t);
			paths.offer(newPath);
		}
		expandedCounter++;
	}

	private int calculate(Point start, Point end) {
		return this.heuristic.calculate(start, end, this.world, this.player);
	}

	private List<Point> generateSuccessors(Point point) {
		List<Point> points = new ArrayList<Point>();
		Point temp = null;
		for (int x = point.x - 1; x <= point.x + 1; ++x) {
			for (int y = point.y + 1; y >= point.y - 1; --y) {
				for (int z = point.z - 1; z <= point.z + 1; ++z) {
					temp = new Point(x, y, z);
					if (valid(temp)) {
						points.add(temp);
					}
				}
			}
		}
		return points;
	}

	/**
	 * * Total cost function to reach the node <code>to</code> from *
	 * <code>from</code>. * * The total cost is defined as: f(x) = g(x) + h(x).
	 * * @param from The node we are leaving. * @param to The node we are
	 * reaching. * @return The total cost.
	 */
	private int f(PathNode p, Point from, Point to) {
		int g = (calculate(from, to) + ((p.parent != null) ? p.parent.cost : 0));
		int h = calculate(from, to);
		p.cost = g;
		p.totalCost = (g + h);
		return p.totalCost;
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

	private World getWorld() {
		return ((MinecraftPathWorld) this.world).getWorld();
	}

	private boolean isGoal(Point last) {
		return last.equals(this.end);
	}
}
