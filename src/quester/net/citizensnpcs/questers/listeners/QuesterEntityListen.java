package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.questers.QuestManager;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class QuesterEntityListen extends EntityListener {
	@Override
	public void onEntityDamage(EntityDamageEvent ev) {
		if (ev instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ev;
			if (event.getDamager() instanceof Player) {
				QuestManager.incrementQuest((Player) event.getDamager(), event);
			}
		}
	}
}
