package com.fullwall.resources.redecouverte.NPClib.Creatures;

import net.minecraft.server.Entity;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.fullwall.resources.redecouverte.NPClib.CraftNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCAnimator.Action;

public abstract class CreatureNPC extends CraftNPC {
	public CreatureNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	protected final double range = 25;

	/**
	 * Called when a creature spawns. Provides a default method, which sets the
	 * creature to randomly path in updateMove().
	 */
	public void onSpawn() {
		this.randomPather = true;
	}

	/**
	 * Called every tick. This method provides a default action, which can be
	 * overridden by subclasses. The default action attempts to target the
	 * closest player based on a predefined range, else takes a random path.
	 */
	public void doTick() {
		updateMove();
		applyGravity();
	}

	/**
	 * Called when the creature dies. Can be used for loot drops or effects to
	 * play.
	 */
	public abstract void onDeath();

	/**
	 * Called when the creature is damaged. Can be used for targeting attacker
	 * etc. Provides a damage event parameter. The type of damage should be
	 * checked using the event's DamageCause. Function calls on the event will
	 * change the underlying instance. Default is to perform the hurt animation.
	 * 
	 * @param event
	 */
	public void onDamage(EntityDamageEvent event) {
		this.animations.performAction(Action.ACT_HURT);
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if (e.getDamager() instanceof Player) {
				Entity entity = ((CraftEntity) e.getDamager()).getHandle();
				double d0 = entity.locX - this.locX;
				double d1;
				for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math
						.random() - Math.random()) * 0.01D) {
					d0 = (Math.random() - Math.random()) * 0.01D;
				}
				this.af = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D)
						- this.yaw;
				this.a(entity, e.getDamage(), d0, d1);
				this.world
						.makeSound(this, this.h(), this.k(),
								(this.random.nextFloat() - this.random
										.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	/**
	 * Returns the type of this creature.
	 * 
	 * @return
	 */
	public abstract CreatureNPCType getType();

	/**
	 * Helper method.
	 * 
	 * @return
	 */
	public Player getEntity() {
		return (Player) this.bukkitEntity;
	}

	/**
	 * Helper method.
	 * 
	 * @return
	 */
	public Location getLocation() {
		return this.getEntity().getLocation();
	}
}