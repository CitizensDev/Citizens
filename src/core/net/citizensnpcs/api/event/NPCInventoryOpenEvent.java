package net.citizensnpcs.api.event;

import net.citizensnpcs.lib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class NPCInventoryOpenEvent extends NPCPlayerEvent implements
		Cancellable {
	private static final long serialVersionUID = 1L;
	private boolean cancelled = false;

	public NPCInventoryOpenEvent(HumanNPC npc, Player player) {
		super("NPCInventoryOpenEvent", npc, player);
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