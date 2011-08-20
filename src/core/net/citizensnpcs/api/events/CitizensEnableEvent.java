package net.citizensnpcs.api.events;

import org.bukkit.event.Cancellable;

public class CitizensEnableEvent extends CitizensEvent implements Cancellable {
	private static final long serialVersionUID = 1L;
	private boolean cancelled = false;

	public CitizensEnableEvent() {
		super("CitizensEnableEvent");
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