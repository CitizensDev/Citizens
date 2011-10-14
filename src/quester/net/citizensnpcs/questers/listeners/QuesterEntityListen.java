package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.questers.QuestManager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class QuesterEntityListen extends EntityListener {
	@Override
	public void onEntityDeath(EntityDeathEvent ev) {
		if (ev.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ev
					.getEntity().getLastDamageCause();
			if (event.getDamager() instanceof Projectile) {
				Projectile shot = ((Projectile) event.getDamager());
				if (shot.getShooter() instanceof Player) {
					event = new EntityDamageByEntityEvent(shot.getShooter(),
							event.getEntity(), event.getCause(),
							event.getDamage());
				}
			}
			if (event.getDamager() instanceof Player
					&& event.getEntity() instanceof LivingEntity) {
				QuestManager.incrementQuest((Player) event.getDamager(), ev);
			}
		}
	}
}
