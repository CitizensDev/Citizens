package com.citizens.Pathfinding.Citizens;

import org.bukkit.World;

import com.citizens.Pathfinding.PathFinder.PathWorld;

public final class MinecraftPathWorld implements PathWorld {
	private final World world;

	public MinecraftPathWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}
}
