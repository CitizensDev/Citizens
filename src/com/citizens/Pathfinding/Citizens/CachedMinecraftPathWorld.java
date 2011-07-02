package com.citizens.Pathfinding.Citizens;

import com.citizens.Pathfinding.PathFinder.PathWorld;

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