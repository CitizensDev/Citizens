package com.citizens.npctypes.interfaces;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public interface Damageable {
	public void onDamage(EntityDamageEvent event);

	public void onDeath(EntityDeathEvent event);
}
