package net.citizensnpcs.questers.quests.types;

import java.util.Set;

import net.citizensnpcs.questers.QuestCancelException;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class ChatQuest implements QuestUpdater {
    private static final Class<? extends Event>[] EVENTS = new Class[] { PlayerChatEvent.class };

    @Override
    public Class<? extends Event>[] getEventTypes() {
        return EVENTS;
    }

    @Override
    public boolean update(Event event, ObjectiveProgress progress) {
        if (!(event instanceof PlayerChatEvent))
            return false;
        if (progress.getObjective().hasParameter("leeway") && progress.getObjective().hasParameter("npcid")) {
            int leeway = progress.getObjective().getParameter("leeway").getInt();
            for (String string : Splitter.on(",").split(progress.getObjective().getParameter("npcid").getString())) {
                HumanNPC npc = NPCManager.get(Integer.parseInt(string));
                if (npc == null)
                    continue;
                if (!LocationUtils.withinRange(progress.getPlayer().getLocation(), npc.getLocation(), leeway))
                    return false;
            }
        }
        String message = ((PlayerChatEvent) event).getMessage();
        for (String match : Splitter.on(",").split(progress.getObjective().getString())) {
            Set<Character> flags = Sets.newHashSet();
            int index = match.indexOf(':');
            if (index != -1) {
                int i = 0;
                while (i != index) {
                    flags.add(match.charAt(i));
                    ++i;
                }
                match = match.substring(i + 1);
            }
            if (flags.contains('c') && message.contains(match))
                return true;
            if (flags.contains('r') && message.matches(match))
                return true;
            if (message.equalsIgnoreCase(match))
                return true;
        }
        return false;
    }

    @Override
    public String getStatus(ObjectiveProgress progress) throws QuestCancelException {
        return "waiting for a message!";
    }
}
