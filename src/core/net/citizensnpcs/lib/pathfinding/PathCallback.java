package net.citizensnpcs.lib.pathfinding;

public abstract class PathCallback {
	public boolean onPathBegin(PathController controller) {
		return false;
	}

	public boolean onPathCancel(PathController controller,
			PathCancelReason reason) {
		return false;
	}

	public boolean onPathCompletion(PathController controller) {
		return false;
	}

	public enum PathCancelReason {
		PLUGIN,
		REPLACE;
	}
}
