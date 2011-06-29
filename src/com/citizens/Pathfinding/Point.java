package com.citizens.Pathfinding;

public final class Point {
	public final int x;
	public final int y;
	public final int z;

	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		return x == other.x && y == other.y && z == other.z;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	public int distanceSquared(Point second) {
		int diffX = second.x - this.x;
		int diffY = second.y - this.y;
		int diffZ = second.z - this.z;
		return diffX * diffX + diffY * diffY + diffZ * diffZ;
	}
}