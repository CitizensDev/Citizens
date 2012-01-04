package net.citizensnpcs.lib.pathfinding;

import net.citizensnpcs.Citizens;

import org.bukkit.Bukkit;

public class Path {
	private final PathingStrategy strategy;
	private boolean paused = false;

	private Path(PathingStrategy strategy) {
		this.strategy = strategy;
	}

	public boolean isPaused() {
		return paused;
	}

	public void pause() {
		paused = true;
	}

	public void pause(int ticks) {
		if (paused == true)
			return;
		pause();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
				new Runnable() {
					@Override
					public void run() {
						resume();
					}
				}, ticks);
	}

	public void resume() {
		paused = false;
	}

	public boolean update() {
		if (paused)
			return false;
		return strategy.update();
	}

	protected static Path create(PathingStrategy withStrategy) {
		if (withStrategy == null)
			throw new IllegalArgumentException("strategy cannot be null");
		return new Path(withStrategy);
	}
}
