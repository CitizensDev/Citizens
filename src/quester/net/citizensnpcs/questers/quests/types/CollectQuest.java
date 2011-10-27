package net.citizensnpcs.questers.quests.types;

import java.util.EnumMap;
import java.util.Map;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

public class CollectQuest implements QuestUpdater {
	private static final Type[] EVENTS = { Type.PLAYER_PICKUP_ITEM,
			Type.ENTITY_DEATH, Type.BLOCK_BREAK };
	private static final Map<Player, Map<Material, Integer>> allowed = Maps
			.newHashMap();

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof PlayerEvent)
			verifyMap(((PlayerEvent) event).getPlayer());
		if (event instanceof BlockBreakEvent) {
			BlockBreakEvent ev = (BlockBreakEvent) event;
			Material type = ev.getBlock().getType();
			switch (type) {
			case STONE:
				type = Material.COBBLESTONE;
				break;
			case REDSTONE_ORE:
				type = Material.REDSTONE;
				break;
			default:
				break;
			}
			incrementMap(progress.getPlayer(), ev.getBlock().getType());
		} else if (event instanceof EntityDeathEvent) {
			EntityDeathEvent ev = (EntityDeathEvent) event;
			for (ItemStack drop : ev.getDrops()) {
				incrementMap(progress.getPlayer(), drop.getType(),
						drop.getAmount());
			}
		} else if (event instanceof PlayerPickupItemEvent) {
			PlayerPickupItemEvent ev = (PlayerPickupItemEvent) event;
			Map<Material, Integer> previous = allowed.get(ev.getPlayer());
			ItemStack stack = ev.getItem().getItemStack();
			if (previous.containsKey(stack.getType())) {
				int amount = previous.get(stack.getType()) - stack.getAmount();
				if (amount <= 0) {
					previous.remove(stack.getType());
				} else
					previous.put(stack.getType(), amount);
				progress.addAmount(amount <= 0 ? stack.getAmount() + amount
						: stack.getAmount());
			}
		}
		boolean completed = progress.getAmount() >= progress.getObjective()
				.getAmount();
		if (completed) {
			allowed.remove(progress.getPlayer());
		}
		return completed;
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

	private void verifyMap(Player player) {
		if (!allowed.containsKey(player))
			allowed.put(player, new EnumMap<Material, Integer>(Material.class));
	}

	private void incrementMap(Player player, Material material) {
		incrementMap(player, material, 1);
	}

	private void incrementMap(Player player, Material material, int existing) {
		Map<Material, Integer> inner = allowed.get(player);
		int amount = inner.containsKey(material) ? inner.get(material) : 0;
		inner.put(material, amount + existing);
	}
}