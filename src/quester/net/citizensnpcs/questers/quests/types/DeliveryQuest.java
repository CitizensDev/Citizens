package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.events.NPCTargetEvent;
import net.citizensnpcs.questers.quests.QuestIncrementer;
import net.citizensnpcs.questers.quests.Objectives.ObjectiveCycler;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityTargetEvent;

public class DeliveryQuest extends QuestIncrementer {
	public DeliveryQuest(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		super(npc, player, questName, objectives);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof EntityTargetEvent
				&& event instanceof NPCTargetEvent) {
			NPCTargetEvent e = (NPCTargetEvent) event;
			if (e.getTarget().getEntityId() == this.player.getEntityId()) {
				if (((HumanNPC) e.getEntity()).getUID() == this.objective
						.getDestinationNPCID()) {
					Player player = (Player) e.getTarget();
					if (player.getItemInHand().getType() == this.objective
							.getMaterial()) {
						this.getProgress().setLastItem(player.getItemInHand());
					}
				}
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.getProgress().getAmount() >= this.objective.getAmount();
	}

	@Override
	public Type[] getEventTypes() {
		return new Type[] { Type.ENTITY_TARGET };
	}
}