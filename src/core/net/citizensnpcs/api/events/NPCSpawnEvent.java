package net.citizensnpcs.api.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.event.Cancellable;

public class NPCSpawnEvent extends NPCEvent implements Cancellable {
	private static final long serialVersionUID = -6321822806485360689L;

	boolean cancelled = false;

	public NPCSpawnEvent(HumanNPC npc) {
		super("NPCSpawnEvent", npc);
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
}