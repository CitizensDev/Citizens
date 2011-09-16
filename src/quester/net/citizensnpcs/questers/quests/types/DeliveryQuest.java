package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.npc.NPCRightClickEvent;
import net.citizensnpcs.questers.QuestCancelException;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;

public class DeliveryQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.CUSTOM_EVENT };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof NPCRightClickEvent) {
			NPCRightClickEvent e = (NPCRightClickEvent) event;
			if (e.getPlayer().getEntityId() == progress.getPlayer()
					.getEntityId()) {
				if (e.getNPC().getUID() == progress.getObjective()
						.getDestNPCID()) {
					Player player = e.getPlayer();
					if (player.getItemInHand().getType() == progress
							.getObjective().getMaterial()) {
						return player.getItemInHand().getAmount() >= progress
								.getObjective().getAmount();
					}
				}
			}
		}
		return false;
	}

	@Override
	public Type[] getEventTypes() {
		return EVENTS;
	}

	@Override
	public String getStatus(ObjectiveProgress progress)
			throws QuestCancelException {
		if (CitizensManager.getNPC(progress.getObjective().getDestNPCID()) == null) {
			throw new QuestCancelException(ChatColor.GRAY
					+ "Cancelling quest due to missing destination NPC.");
		}
		int amount = progress.getObjective().getAmount();
		return ChatColor.GREEN
				+ "Delivering "
				+ StringUtils.wrap(amount)
				+ " "
				+ StringUtils.formatter(progress.getObjective().getMaterial())
						.plural(amount)
				+ " to "
				+ StringUtils.wrap(CitizensManager.getNPC(
						progress.getObjective().getDestNPCID()).getName())
				+ ".";
	}
}