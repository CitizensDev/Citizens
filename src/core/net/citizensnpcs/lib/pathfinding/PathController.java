package net.citizensnpcs.lib.pathfinding;

import net.citizensnpcs.lib.HumanNPC;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class PathController {
	private Path executing;
	private final HumanNPC parent;

	public PathController(HumanNPC parent) {
		this.parent = parent;
	}

	public void cancel() {
	}

	public void disableDefaultCallback(DefaultPathingCallback callback) {
	}

	public void doTick() {
		if (executing == null)
			return;
		boolean finished = executing.update();
		if (!finished)
			return;
	}

	public void enableDefaultCallback(DefaultPathingCallback callback) {
	}

	public Path getCurrentPath() {
		return executing;
	}

	public void newPathWithStrategy(PathingStrategy strategy) {
		executing = Path.create(strategy);
	}

	public Path pathTo(Location destination) {
		executing = Path.create(new MinecraftPathingStrategy(parent,
				destination));
		return executing;
	}

	public Path target(Entity target) {
		executing = Path.create(new TargetPathingStrategy(parent, target));
		return executing;
	}

	public enum DefaultPathingCallback {

	}
}
