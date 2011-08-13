package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.events.NPCTargetEvent;
import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestObjective;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityTargetEvent;

public class DeliveryQuest implements QuestObjective {
	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof EntityTargetEvent
				&& event instanceof NPCTargetEvent) {
			NPCTargetEvent e = (NPCTargetEvent) event;
			if (e.getTarget().getEntityId() == progress.getPlayer()
					.getEntityId()) {
				if (((HumanNPC) e.getEntity()).getUID() == progress
						.getObjective().getDestinationNPCID()) {
					Player player = (Player) e.getTarget();
					if (player.getItemInHand().getType() == progress
							.getObjective().getMaterial()) {
						progress.setLastItem(player.getItemInHand());
					}
				}
			}
		}
		return progress.getAmount() >= progress.getObjective().getAmount();
	}

	@Override
	public Type[] getEventTypes() {
		return new Type[] { Type.ENTITY_TARGET };
	}

	@Override
	public boolean isCompleted(ObjectiveProgress progress) {
		return progress.getAmount() >= progress.getObjective().getAmount();
	}
}