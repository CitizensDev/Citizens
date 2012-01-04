package net.citizensnpcs.api.event;

import net.citizensnpcs.lib.HumanNPC;

import org.bukkit.Location;

public class NPCCreateEvent extends NPCEvent {
	private static final long serialVersionUID = -6321822806485360689L;

	private final NPCCreateReason reason;
	private final Location loc;

	public NPCCreateEvent(HumanNPC npc, NPCCreateReason reason, Location loc) {
		super("NPCSpawnEvent", npc);
		this.reason = reason;
		this.loc = loc;
	}

	/**
	 * Get the location where the NPC was created
	 * 
	 * @return location where NPC was created
	 */
	public Location getLocation() {
		return this.loc;
	}

	/**
	 * Get the reason why an NPC was created
	 * 
	 * @return reason for an NPC being created
	 */
	public NPCCreateReason getReason() {
		return this.reason;
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
		 * NPC was reloaded/respawned after being unloaded/despawned/killed
		 */
		RESPAWN;
	}
}