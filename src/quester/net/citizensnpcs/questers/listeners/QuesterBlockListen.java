package net.citizensnpcs.questers.listeners;

import java.util.Set;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.questers.QuestManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.google.common.collect.Sets;

public class QuesterBlockListen extends BlockListener {
	private static final Set<Location> placed = Sets.newHashSet();

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (placed.contains(event.getBlock().getLocation())) {
			return;
		}
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		placed.add(event.getBlock().getLocation());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
				new RemoveBlockTask(event.getBlock()),
				Settings.getInt("BlockTrackingRemoveDelay"));
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	private static class RemoveBlockTask implements Runnable {
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