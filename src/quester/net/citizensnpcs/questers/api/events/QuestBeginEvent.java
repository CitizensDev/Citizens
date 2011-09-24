package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class QuestBeginEvent extends QuestEvent implements Cancellable {
	private static final long serialVersionUID = 1L;
	private boolean cancelled = false;

	public QuestBeginEvent(Quest quest, Player player) {
		super("QuestBeginEvent", quest, player);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
