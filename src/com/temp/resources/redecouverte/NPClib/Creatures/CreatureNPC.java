package com.temp.resources.redecouverte.NPClib.Creatures;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.temp.resources.redecouverte.NPClib.CraftNPC;

public abstract class CreatureNPC extends CraftNPC {
	protected final double range = 25;
	protected final Integer[] weapons = { 267, 268, 272, 276, 283 };

	public CreatureNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

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
	 * change the underlying instance.
	 * 
	 * @param event
	 */
	public abstract void onDamage(EntityDamageEvent event);

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

	/**
	 * Called when a player right clicks the entity.
	 * 
	 * @param player
	 */
	public abstract void onRightClick(Player player);

	/**
	 * Called when a player left clicks the entity - this can cause a damage
	 * event as well.
	 * 
	 * @param target
	 */
	public abstract void onLeftClick(Player player);
}