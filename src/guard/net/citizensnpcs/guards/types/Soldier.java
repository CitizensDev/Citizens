package net.citizensnpcs.guards.types;

import java.util.Map;

import net.citizensnpcs.guards.Guard;
import net.citizensnpcs.guards.GuardState;
import net.citizensnpcs.guards.GuardUpdater;
import net.citizensnpcs.guards.OwnerSelection;
import net.citizensnpcs.guards.Selection;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Maps;

public class Soldier implements GuardUpdater {
    public Soldier() {
        PlayerListener listener = new SelectionHooks();
        NPCTypeManager.registerEvent(Type.PLAYER_QUIT, listener);
        NPCTypeManager.registerEvent(Type.PLAYER_LOGIN, listener);
        NPCTypeManager.registerEvent(Type.PLAYER_INTERACT, listener);
        NPCTypeManager.registerEvent(Type.PLAYER_INTERACT_ENTITY, listener);
    }

    @Override
    public GuardStatus updateStatus(GuardStatus current, HumanNPC npc) {
        return current;
    }

    @Override
    public void onDamage(HumanNPC npc, LivingEntity attacker) {
    }

    private static class SelectionHooks extends PlayerListener {
        @Override
        public void onPlayerQuit(PlayerQuitEvent event) {
            selections.remove(event.getPlayer());
        }

        @Override
        public void onPlayerInteract(PlayerInteractEvent event) {
            Selection<HumanNPC> selection = getSelection(event.getPlayer());
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                    || selection.size() == 0
                    || !UtilityProperties.isHoldingTool("SoldierSelectTool",
                            event.getPlayer())) {
                if ((event.getAction() == Action.RIGHT_CLICK_AIR || event
                        .getAction() == Action.RIGHT_CLICK_BLOCK)
                        && selection.size() > 0
                        && UtilityProperties.isHoldingTool(
                                "SoldierDeselectAllTool", event.getPlayer())) {
                    event.getPlayer().sendMessage(
                            ChatColor.GRAY
                                    + "Deselecting "
                                    + StringUtils.wrap(selection.size(),
                                            ChatColor.GRAY)
                                    + StringUtils.pluralise(" NPC",
                                            selection.size()) + ".");
                    selection.deselectAll();
                }
                return;
            }
            for (HumanNPC npc : getSelection(event.getPlayer())) {
                if (npc == null) {
                    continue;
                }
                PathUtils.createPath(npc,
                        event.getClickedBlock().getLocation(), -1);
            }
            event.getPlayer().sendMessage(
                    StringUtils.wrap(selection.size()) + " "
                            + StringUtils.pluralise("NPC", selections.size())
                            + " enroute.");
        }

        @Override
        public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
            HumanNPC npc = NPCManager.get(event.getRightClicked());
            boolean holding = UtilityProperties.isHoldingTool(
                    "SoldierSelectTool", event.getPlayer());
            if (npc == null) {
                if (holding && event.getRightClicked() instanceof LivingEntity)
                    attack(event.getPlayer(),
                            (LivingEntity) event.getRightClicked(),
                            getSelection(event.getPlayer()));
                return;
            }
            if (isSoldier(npc)) {
                if (holding) {
                    select(event.getPlayer(), npc,
                            getSelection(event.getPlayer()));
                } else if (UtilityProperties.isHoldingTool("SoldierReturnTool",
                        event.getPlayer())) {
                    npc.getHandle().cancelTarget();
                    npc.teleport(npc.getBaseLocation());
                }
                return;
            }
        }

        private void attack(Player player, LivingEntity attack,
                Selection<HumanNPC> selection) {
            if (selection.size() == 0)
                return;
            for (HumanNPC npc : selection) {
                PathUtils.target(npc, attack, true, -1);
            }
            player.sendMessage(ChatColor.GREEN + "Set "
                    + StringUtils.wrap(selection.size())
                    + StringUtils.pluralise(" NPC", selection.size())
                    + " on your target.");
        }

        private void select(Player player, HumanNPC npc,
                Selection<HumanNPC> selection) {
            if (selection.isSelected(npc)) {
                selection.deselect(npc);
            } else {
                selection.select(npc);
                PathUtils.target(npc, player, false, -1);
            }
            player.sendMessage(ChatColor.GREEN
                    + (selection.isSelected(npc) ? "Selected" : "Deselected")
                    + StringUtils.wrap(npc.getName()) + ". "
                    + StringUtils.wrap(selection.size())
                    + StringUtils.pluralise(" NPC", selection.size())
                    + " now selected.");

        }

        private boolean isSoldier(HumanNPC npc) {
            return npc.isType("guard")
                    && ((Guard) npc.getType("guard"))
                            .isState(GuardState.SOLDIER);
        }
    }

    private static Selection<HumanNPC> getSelection(Player player) {
        if (!selections.containsKey(player))
            selections.put(player, new OwnerSelection(player));
        return selections.get(player);
    }

    private static final Map<Player, Selection<HumanNPC>> selections = Maps
            .newHashMap();
}
