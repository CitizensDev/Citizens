package com.citizens.npctypes;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.citizens.commands.CommandHandler;
import com.citizens.interfaces.Saveable;
import com.citizens.resources.npclib.HumanNPC;

public abstract class CitizensNPC {

	/**
	 * Get an NPC's type
	 * 
	 * @return NPC's type, use lowercase
	 */
	public abstract String getType();

	/**
	 * Get an NPC's property handler
	 * 
	 * @return NPC type's properties object
	 */
	public abstract Saveable getProperties();

	/**
	 * Get an NPC's purchaser
	 * 
	 * @return default NPC type purchaser, can be overriden
	 */
	public Purchaser getPurchaser() {
		return new DefaultPurchaser();
	}

	/**
	 * Get the commands for an NPC type
	 * 
	 * @return NPC type's command object
	 */
	public abstract CommandHandler getCommands();

	/**
	 * Left-clicking an NPC.
	 * 
	 * @param player
	 *            Player doing the clicking
	 * @param npc
	 *            NPC being clicked
	 */
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	/**
	 * Right-clicking an NPC.
	 * 
	 * @param player
	 *            Player doing the clicking
	 * @param npc
	 *            NPC being clicked
	 */
	public void onRightClick(Player player, HumanNPC npc) {
	}

	/**
	 * Called when an NPC type targets.
	 * 
	 * @param event
	 *            Bukkit's EntityTargetEvent
	 */
	public void onTarget(EntityTargetEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Called when an NPC type is damaged.
	 * 
	 * @param event
	 *            Bukkit's EntityDamageEvent
	 */
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Called when an NPC type is killed.
	 * 
	 * @param event
	 *            Bukkit's EntityDeathEvent
	 */
	public void onDeath(EntityDeathEvent event) {
	}
}