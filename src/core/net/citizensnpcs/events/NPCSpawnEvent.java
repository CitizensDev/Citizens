package net.citizensnpcs.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.event.Cancellable;

public class NPCSpawnEvent extends NPCEvent implements Cancellable {
	private static final long serialVersionUID = -6321822806485360689L;

	boolean isCancelled = false;

	public NPCSpawnEvent(HumanNPC npc) {
		super("NPCSpawnEvent", npc);
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}
}