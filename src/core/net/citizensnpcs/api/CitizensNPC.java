package net.citizensnpcs.api;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

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
	public abstract Properties getProperties();

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

	/**
	 * Register event listeners for an NPC type. Use
	 * CitizensNPCManager.registerEvent
	 */
	public void registerEvents() {
	}
}