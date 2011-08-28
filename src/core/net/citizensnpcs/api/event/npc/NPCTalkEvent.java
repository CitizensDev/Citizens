package net.citizensnpcs.api.event.npc;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class NPCTalkEvent extends NPCEvent implements Cancellable {
	private static final long serialVersionUID = 1L;
	private boolean cancelled = false;
	private String text;
	private final Player player;

	public NPCTalkEvent(HumanNPC npc, Player player, String text) {
		super("NPCDisplayTextEvent", npc);
		this.text = text;
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
	 * Get the text involved in the event.
	 * 
	 * @return text involved in the event.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Set the text involved in the event.
	 * 
	 * @param text
	 *            the text you want displayed.
	 */
	public void setText(String text) {
		this.text = text;
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