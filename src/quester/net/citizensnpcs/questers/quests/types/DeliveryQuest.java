package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.npc.NPCTargetEvent;
import net.citizensnpcs.questers.QuestCancelException;
import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestUpdater;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityTargetEvent;

public class DeliveryQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.ENTITY_TARGET };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof EntityTargetEvent
				&& event instanceof NPCTargetEvent) {
			NPCTargetEvent e = (NPCTargetEvent) event;
			if (e.getTarget().getEntityId() == progress.getPlayer()
					.getEntityId()) {
				if (((HumanNPC) e.getEntity()).getUID() == progress
						.getObjective().getDestNPCID()) {
					Player player = (Player) e.getTarget();
					if (player.getItemInHand().getType() == progress
							.getObjective().getMaterial()) {
						progress.setLastItem(player.getItemInHand());
					}
				}
			}
		}
		return isCompleted(progress);
	}

	@Override
	public Type[] getEventTypes() {
		return EVENTS;
	}

	@Override
	public boolean isCompleted(ObjectiveProgress progress) {
		if (progress.getLastItem() == null)
			return false;
		return progress.getLastItem().getType() == progress.getObjective()
				.getMaterial()
				&& progress.getLastItem().getAmount() >= progress
						.getObjective().getAmount();
	}

	@Override
	public String getStatus(ObjectiveProgress progress)
			throws QuestCancelException {
		if (CitizensManager.getNPC(progress.getObjective().getDestNPCID()) == null) {
			throw new QuestCancelException(ChatColor.GRAY
					+ "Cancelling quest due to missing destination NPC.");
		}
		int amount = progress.getAmount();
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