package net.citizensnpcs.questers.listeners;

import java.util.Map;

import net.citizensnpcs.questers.QuestManager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import com.google.common.collect.Maps;

public class QuesterEntityListen extends EntityListener {
	private final Map<Integer, Player> tagged = Maps.newHashMap();

	@Override
	public void onEntityDamage(EntityDamageEvent ev) {
		if (ev instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ev;
			if (ev.getEntity() instanceof LivingEntity
					&& ((LivingEntity) ev.getEntity()).getHealth()
							- ev.getDamage() <= 0
					&& event.getDamager() instanceof Player) {
				tagged.put(ev.getEntity().getEntityId(),
						(Player) event.getDamager());
			}
		}
	}

	@Override
	public void onEntityDeath(EntityDeathEvent ev) {
		if (tagged.containsKey(ev.getEntity().getEntityId())) {
			QuestManager.incrementQuest(
					tagged.remove(ev.getEntity().getEntityId()), ev);
		}
	}
}
