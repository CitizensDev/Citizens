package net.citizensnpcs.npctypes;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public abstract class CitizensNPC {

	/**
	 * Get the CitizensNPCType subclass that corresponds with the CitizensNPC
	 * subclass
	 * 
	 * @return CitizensNPCType subclass
	 */
	public abstract CitizensNPCType getType();

	public abstract void save(Storage profiles, int UID);

	public abstract void load(Storage profiles, int UID);

	/**
	 * Called when a player left clicks the NPC - this can cause a damage event
	 * as well.
	 * 
	 * @param player
	 *            Player doing the clicking
	 * @param npc
	 *            NPC being clicked
	 */
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	/**
	 * Called when a player right-clicks an NPC.
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