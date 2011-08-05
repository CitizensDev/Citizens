package net.citizensnpcs.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class NPCCreatureSpawnEvent extends NPCEvent implements Cancellable {
	private static final long serialVersionUID = 1L;
	private boolean cancelled = false;
	private Location location;

	public NPCCreatureSpawnEvent(HumanNPC npc, Location location) {
		super("NPCCreatureSpawnEvent", npc);
		this.location = location;
	}

	/**
	 * Get the cancellation state of the event.
	 * 
	 * @return true if the event is cancelled
	 */
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Set the cancellation state of an event.
	 * 
	 * @param cancelled
	 *            the cancellation state of the event
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Get the location where a creature NPC is set to spawn
	 * 
	 * @return location where a creature NPC is set to spawn
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * Set the location where a creature NPC is set to spawn
	 * 
	 * @param location
	 *            the location where a creature NPC will spawn
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
}