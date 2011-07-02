package com.citizens.Pathfinding.Citizens;

import org.bukkit.ChunkSnapshot;
import org.bukkit.World;

public class ChunkCache {
	private final int chunkX;
	private final int chunkZ;
	private final ChunkSnapshot[][] chunks;

	public ChunkCache() {
		this.chunkX = this.chunkZ = 0;
		chunks = null;
	}

	public ChunkCache(World world, net.minecraft.server.Entity entity, int x,
			int y, int z, float range) {
		this(world, getConstructor(entity, x, y, z, range));
	}

	public ChunkCache(World world, int[] values) {
		this(world, values[0], values[1], values[2], values[3], values[4]);
	}

	public ChunkCache(World world, int x, int y, int z, int offsetX, int offsetZ) {
		this.chunkX = x >> 4;
		this.chunkZ = z >> 4;
		int offsetChunkX = offsetX >> 4;
		int offsetChunkZ = offsetZ >> 4;

		this.chunks = new ChunkSnapshot[offsetChunkX - this.chunkX + 1][offsetChunkZ
				- this.chunkZ + 1];

		for (int newX = this.chunkX; newX <= offsetChunkX; ++newX) {
			for (int newY = this.chunkZ; newY <= offsetChunkZ; ++newY) {
				this.chunks[newX - this.chunkX][newY - this.chunkZ] = world
						.getChunkAt(newX, newY).getChunkSnapshot();
			}
		}
	}

	public ChunkSnapshot getChunk(int x, int z) {
		int offsetX = (x >> 4) - this.chunkX;
		int offsetZ = (z >> 4) - this.chunkZ;

		if (offsetX >= 0 && offsetX < this.chunks.length && offsetZ >= 0
				&& offsetZ < this.chunks[offsetX].length) {
			ChunkSnapshot chunk = this.chunks[offsetX][offsetZ];
			return chunk;
		}
		return null;
	}

	public int getBlockId(int x, int y, int z) {
		return this.getChunk(x, z).getBlockTypeId(toChunkCoord(x),
				toChunkCoord(y), toChunkCoord(z));
	}

	public int getLightLevel(int x, int y, int z) {
		return this.getChunk(x, z).getBlockSkyLight(toChunkCoord(x),
				toChunkCoord(y), toChunkCoord(z));
	}

	public static int[] getConstructor(net.minecraft.server.Entity entity,
			int x, int y, int z, float range) {
		int xFloor = floor(entity.locX);
		int yFloor = floor(entity.locY);
		int zFloor = floor(entity.locZ);
		int offset = (int) (range + 8);
		return new int[] { xFloor - offset, yFloor - offset, zFloor - offset,
				xFloor + offset, zFloor + offset };
	}

	public static int toChunkCoord(int coord) {
		return coord & 15;
	}

	public static int floor(double value) {
		int i = (int) value;
		return value < i ? i - 1 : i;
	}

	public boolean isNull() {
		return chunks == null;
	}
}
