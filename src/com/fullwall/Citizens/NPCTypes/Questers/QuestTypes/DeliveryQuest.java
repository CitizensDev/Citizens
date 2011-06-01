package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.QuestProgress;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent.NpcTargetReason;

public class DeliveryQuest extends QuestProgress {
	public DeliveryQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof NPCEntityTargetEvent) {
			NPCEntityTargetEvent e = (NPCEntityTargetEvent) event;
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED
					&& e.getTarget().getEntityId() == this.player.getEntityId()) {
				if (((HumanNPC) e.getEntity()).getUID() == this.quester
						.getUID()) {
					Player player = (Player) e.getTarget();
					if (player.getItemInHand().getType() == getObjectiveItem()
							.getType()) {
						this.lastItem = player.getItemInHand();
					}
				}
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return this.lastItem.getAmount() >= getObjectiveItem().getAmount();
	}
}