package com.citizens.pathfinding.citizens;

import com.citizens.pathfinding.PathFinder.PathWorld;

public final class CachedMinecraftPathWorld implements PathWorld {
	private final ChunkCache cache;

	public CachedMinecraftPathWorld(ChunkCache cache) {
		this.cache = cache;
	}

	public ChunkCache getCache() {
		return cache;
	}

	public boolean isNull() {
		return this.cache == null || this.cache.isNull();
	}
}