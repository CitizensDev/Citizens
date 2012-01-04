package net.citizensnpcs.questers.quests.types;

import java.util.Set;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.listeners.BlockProtection;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.google.common.collect.Sets;

public class DestroyQuest implements QuestUpdater {
	@Override
	public Type[] getEventTypes() {
		return EVENTS;
	}

	@Override
	public String getStatus(ObjectiveProgress progress) {
		return QuestUtils.defaultAmountProgress(progress, StringUtils
				.formatter(progress.getObjective().getMaterial()).wrap()
				.plural(progress.getAmount())
				+ " destroyed");
	}

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof BlockPlaceEvent) {
			BlockPlaceEvent ev = (BlockPlaceEvent) event;
			if (ev.getBlock().getType() == Material.TNT) {
				tnts.add(ev.getBlock().getLocation());
			}
			protection.onBlockPlaced(ev.getBlock());
		}
		if (event instanceof BlockBreakEvent) {
			BlockBreakEvent ev = (BlockBreakEvent) event;
			if (ev.getBlock().getType() == Material.TNT)
				tnts.remove(ev.getBlock().getLocation());
			if (protection.allowsBreak(ev.getBlock())
					&& ev.getBlock().getType() == progress.getObjective()
							.getMaterial()) {
				progress.addAmount(1);
			}
		}
		if (event instanceof EntityExplodeEvent) {
			EntityExplodeEvent ev = (EntityExplodeEvent) event;
			if (ev.getEntity() instanceof TNTPrimed) {
				TNTPrimed tnt = (TNTPrimed) ev.getEntity();
				Location loc = tnt.getLocation();
				// Entity location might not be the same as block location, so
				// fetch block location.
				if (tnts.contains(new Location(loc.getWorld(), loc.getBlockX(),
						loc.getBlockY(), loc.getBlockZ()))) {
					for (Block exploded : ev.blockList()) {
						if (exploded.getType() != progress.getObjective()
								.getMaterial())
							continue;
						progress.addAmount(1);
					}
				}
			}
		}
		return progress.getAmount() >= progress.getObjective().getAmount();
	}

	private static final Set<Location> tnts = Sets.newHashSet();
	private static final BlockProtection protection = new BlockProtection();
	private static final Type[] EVENTS = new Type[] { Type.BLOCK_BREAK,
			Type.BLOCK_PLACE, Type.ENTITY_EXPLODE };
}