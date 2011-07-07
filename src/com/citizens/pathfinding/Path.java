package com.citizens.pathfinding;

import java.util.Collection;

public class Path {
	private Point[] points;
	private int index = 0;

	public Path(Point[] points) {
		this.points = points;
	}

	public Path(Collection<Point> path) {
		points = new Point[path.size()];
		this.points = path.toArray(points);
	}

	public void increment() {
		++index;
	}

	public boolean finished() {
		return index > points.length;
	}

	public Point current() {
		return points[index];
	}

	public Point last() {
		return points[points.length - 1];
	}
}