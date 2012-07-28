package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestCancelException;
import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPC;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.resources.npclib.creatures.EvilCreatureNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillNPCQuest implements QuestUpdater {
    @Override
    public Class<? extends Event>[] getEventTypes() {
        return EVENTS;
    }

    @Override
    public String getStatus(ObjectiveProgress progress) throws QuestCancelException {
        return QuestUtils.defaultAmountProgress(progress,
                StringUtils.formatter("NPC").wrap().plural(progress.getAmount()) + " killed");
    }

    @Override
    public boolean update(Event event, ObjectiveProgress progress) {
        if (event instanceof EntityDeathEvent) {
            EntityDeathEvent ev = (EntityDeathEvent) event;
            HumanNPC npc = NPCManager.get(ev.getEntity());
            if (npc == null) {
                CreatureNPC cNPC = CreatureTask.getCreature(ev.getEntity());
                if (cNPC != null)
                    npc = cNPC.npc;
            }
            if (npc != null) {
                String search = progress.getObjective().getString().toLowerCase();
                boolean found = false, reversed = !search.isEmpty() && search.charAt(0) == '-';
                if (search.contains("*") || search.contains(npc.getUID() + ",")
                        || search.contains(npc.getName().toLowerCase() + ",")
                        || (search.contains("evil") && npc.getHandle() instanceof EvilCreatureNPC)) {
                    found = true;
                }
                if (reversed ^ found) {
                    progress.addAmount(1);
                }
            }
        }
        return progress.getAmount() >= progress.getObjective().getAmount();
    }

    private static final Class<? extends Event>[] EVENTS = new Class[] { EntityDeathEvent.class };
}
