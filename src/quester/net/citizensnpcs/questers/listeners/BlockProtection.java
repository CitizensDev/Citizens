package net.citizensnpcs.questers.listeners;

import java.util.Set;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import com.google.common.collect.Sets;

public class BlockProtection {
	private final Set<Location> placed = Sets.newHashSet();

	public boolean allowsBreak(Block placed) {
		return !this.placed.contains(placed);
	}

	public void onBlockPlaced(Block block) {
		placed.add(block.getLocation());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
				new RemoveBlockTask(block),
				Settings.getInt("BlockTrackingRemoveDelay"));
	}

	private class RemoveBlockTask implements Runnable {
		private final Location location;

		RemoveBlockTask(Block block) {
			this.location = block.getLocation();
		}

		@Override
		public void run() {
			synchronized (placed) {
				placed.remove(location);
			}
		}
	}
}
