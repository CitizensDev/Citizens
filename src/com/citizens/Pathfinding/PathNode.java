package com.citizens.Pathfinding;

public class PathNode implements Comparable<PathNode> {
	public Point point;
	public final PathNode parent;
	public int cost;
	public int totalCost;

	public PathNode(Point point, PathNode parent, short cost, short totalCost) {
		this.point = point;
		this.parent = parent;
		this.cost = cost;
		this.totalCost = totalCost;
	}

	public PathNode() {
		this.point = null;
		this.parent = null;
		this.cost = this.totalCost = 0;
	}

	public PathNode(PathNode path) {
		this.parent = path;
		this.cost = path.cost;
		this.totalCost = path.totalCost;
	}

	@Override
	public int compareTo(PathNode node) {
		int result = node.totalCost - node.cost;
		if (result > node.totalCost)
			return 1;
		else if (result == 0)
			return 0;
		else
			return -1;
	}
}