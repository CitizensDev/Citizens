package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocationQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.PLAYER_MOVE };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			if (LocationUtils.withinRange(ev.getPlayer().getLocation(),
					progress.getObjective().getLocation(), progress
							.getObjective().getAmount()))
				return true;
		}
		return false;
	}

	@Override
	public Type[] getEventTypes() {
		return EVENTS;
	}

	@Override
	public String getStatus(ObjectiveProgress progress) {
		int amount = progress.getObjective().getAmount();
		return ChatColor.GREEN
				+ "Moving to "
				+ StringUtils.format(progress.getObjective().getLocation())
				+ " "
				+ StringUtils.bracketize(StringUtils.wrap(amount)
						+ StringUtils.formatter(" block").plural(amount)
						+ " leeway", true);
	}
}