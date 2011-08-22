package net.citizensnpcs.api.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

public class NPCRemoveEvent extends NPCEvent {
	private static final long serialVersionUID = 1L;
	private NPCRemoveReason reason;

	public NPCRemoveEvent(HumanNPC npc, NPCRemoveReason reason) {
		super("NPCRemoveEvent", npc);
		this.reason = reason;
	}

	/**
	 * Get the reason why an NPC despawn
	 * 
	 * @return reason Reason why NPC despawned
	 */
	public NPCRemoveReason getReason() {
		return this.reason;
	}

	public enum NPCRemoveReason {
		/**
		 * NPC was killed
		 */
		DEATH,
		/**
		 * NPC was removed by a command
		 */
		COMMAND,
		/**
		 * NPC was removed, possibly to be respawned later
		 */
		UNLOAD,
		/**
		 * NPC was removed by other means
		 */
		OTHER;
	}
}