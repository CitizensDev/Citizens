package net.citizensnpcs.api.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class NPCCreateEvent extends NPCEvent implements Cancellable {
	private static final long serialVersionUID = -6321822806485360689L;

	private NPCCreateReason reason;
	private Location loc;
	boolean cancelled = false;

	public NPCCreateEvent(HumanNPC npc, NPCCreateReason reason, Location loc) {
		super("NPCSpawnEvent", npc);
		this.reason = reason;
		this.loc = loc;
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
	 * Get the reason why an NPC was created
	 * 
	 * @return reason for an NPC being created
	 */
	public NPCCreateReason getReason() {
		return this.reason;
	}

	/**
	 * Get the location where the NPC was created
	 * 
	 * @return location where NPC was created
	 */
	public Location getLocation() {
		return this.loc;
	}

	public enum NPCCreateReason {
		/**
		 * NPC naturally spawned into the world
		 */
		SPAWN,
		/**
		 * NPC was created via a command
		 */
		COMMAND,
		/**
		 * NPC was reloaded/respawned after being unloaded/despawned
		 */
		RESPAWN;
	}
}