package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent.NpcTargetReason;

public class DeliveryQuest extends Quest {
	private HumanNPC destination;
	private ItemStack item;

	public DeliveryQuest(HumanNPC quester, Player player) {
		super(quester, player);
	}

	public DeliveryQuest(HumanNPC quester, Player player, HumanNPC destination,
			ItemStack item) {
		super(quester, player);
		this.destination = destination;
		this.item = item;
	}

	@Override
	public QuestType getType() {
		return QuestType.DELIVERY;
	}

	@Override
	public void updateProgress(Event event) {
		if (event instanceof EntityTargetEvent) {
			EntityTargetEvent ev = (EntityTargetEvent) event;
			if (ev.getTarget() == destination) {
				NPCEntityTargetEvent e = (NPCEntityTargetEvent) ev;
				if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
					if (ev.getEntity() instanceof Player) {
						Player player = (Player) ev.getEntity();
						if (player.getItemInHand() == item) {
							completed = true;
							super.updateProgress(event);
						}
					}
				}
			}
		}
	}

	@Override
	public Quest parse(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createString() {
		// TODO Auto-generated method stub
		return null;
	}
}