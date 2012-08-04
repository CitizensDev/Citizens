package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.questers.QuestCancelException;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class DeliveryQuest implements QuestUpdater {
    @Override
    public Class<? extends Event>[] getEventTypes() {
        return EVENTS;
    }

    @Override
    public String getStatus(ObjectiveProgress progress) throws QuestCancelException {
        if (CitizensManager.getNPC(progress.getObjective().getDestNPCID()) == null) {
            throw new QuestCancelException(ChatColor.GRAY + "Cancelling quest due to missing destination NPC.");
        }
        int amount = progress.getObjective().getAmount();
        if (progress.getObjective().getMaterial() == null || progress.getObjective().getMaterial() == Material.AIR)
            return ChatColor.GREEN + "Talking to "
                    + StringUtils.wrap(CitizensManager.getNPC(progress.getObjective().getDestNPCID()).getName()) + ".";
        return ChatColor.GREEN + "Delivering " + StringUtils.wrap(amount) + " "
                + StringUtils.formatter(progress.getObjective().getMaterial()).plural(amount) + " to "
                + StringUtils.wrap(CitizensManager.getNPC(progress.getObjective().getDestNPCID()).getName()) + ".";
    }

    @Override
    public boolean update(Event event, ObjectiveProgress progress) {
        if (!(event instanceof NPCRightClickEvent))
            return false;
        NPCRightClickEvent e = (NPCRightClickEvent) event;
        if (e.getPlayer().getEntityId() != progress.getPlayer().getEntityId()
                || e.getNPC().getUID() != progress.getObjective().getDestNPCID())
            return false;
        Player player = progress.getPlayer();

        if (progress.getObjective().getMaterial() == null || progress.getObjective().getMaterial() == Material.AIR) {
            return true;
        }

        if (player.getItemInHand().getType() == progress.getObjective().getMaterial()) {
            if (progress.getObjective().hasParameter("data")) {
                if (player.getItemInHand().getDurability() != progress.getObjective().getParameter("data").getInt())
                    return false;
            }

            boolean completed = InventoryUtils.has(player, progress.getObjective().getMaterial(), progress
                    .getObjective().getAmount());
            if (completed && progress.getObjective().getAmount() > 0) {
                InventoryUtils.removeItems(player, progress.getObjective().getMaterial(), progress.getObjective()
                        .getAmount(), player.getInventory().getHeldItemSlot());
            }
            return completed;
        }
        return false;
    }

    private static final Class<? extends Event>[] EVENTS = new Class[] { NPCRightClickEvent.class };
}