package net.citizensnpcs.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class NPCInventoryOpenEvent extends NPCEvent implements Cancellable {
	private static final long serialVersionUID = 1L;
	private boolean cancelled = false;
	private final Player player;

	public NPCInventoryOpenEvent(HumanNPC npc, Player player) {
		super("NPCInventoryOpenEvent", npc);
		this.player = player;
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
	 * Get the player involved in the event.
	 * 
	 * @return player involved in the event
	 */
	public Player getPlayer() {
		return this.player;
	}
}