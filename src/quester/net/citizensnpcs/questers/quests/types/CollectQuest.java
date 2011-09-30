package net.citizensnpcs.questers.quests.types;

import java.util.Set;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.google.common.collect.Sets;

public class CollectQuest implements QuestUpdater {
	private static final Type[] EVENTS = { Type.PLAYER_DROP_ITEM,
			Type.PLAYER_PICKUP_ITEM };
	private static final Set<Item> dropped = Sets.newHashSet();

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof PlayerDropItemEvent) {
			PlayerDropItemEvent ev = (PlayerDropItemEvent) event;
			dropped.add(ev.getItemDrop());
			new ItemCheck(ev.getItemDrop()).run();
		} else if (event instanceof PlayerPickupItemEvent) {
			PlayerPickupItemEvent ev = (PlayerPickupItemEvent) event;
			if (dropped.contains(ev.getItem())) {
				return progress.getAmount() >= progress.getObjective()
						.getAmount();
			}
			if (ev.getItem().getItemStack().getType() == progress
					.getObjective().getMaterial()) {
				progress.addAmount(ev.getItem().getItemStack().getAmount());
			}
		}
		return progress.getAmount() >= progress.getObjective().getAmount();
	}

	@Override
	public Type[] getEventTypes() {
		return EVENTS;
	}

	@Override
	public String getStatus(ObjectiveProgress progress) {
		return QuestUtils.defaultAmountProgress(progress, StringUtils
				.formatter(progress.getObjective().getMaterial()).wrap()
				.plural(progress.getAmount())
				+ " collected");
	}

	private static class ItemCheck implements Runnable {
		private final Item item;

		ItemCheck(Item item) {
			this.item = item;
		}

		@Override
		public void run() {
			if (item == null || item.isDead()) {
				dropped.remove(item);
			} else {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
						this, SettingsManager.getInt("ItemExploitCheckDelay"));
			}
		}

	}
}