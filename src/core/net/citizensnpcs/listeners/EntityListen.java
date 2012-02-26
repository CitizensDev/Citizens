package net.citizensnpcs.listeners;

import net.citizensnpcs.Settings;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCTalkEvent;
import net.citizensnpcs.api.event.NPCTargetEvent;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class EntityListen implements Listener {
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        if (NPCManager.get(event.getPlayer()) == null)
            return;
        ((CraftServer) Bukkit.getServer()).getHandle().players.remove(NPCManager.get(event.getPlayer()).getHandle());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        CreatureTask.onDamage(event.getEntity(), event);
        HumanNPC npc = NPCManager.get(event.getEntity());
        if (npc != null) {
            npc.callDamageEvent(event);
        }
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (npc != null) {
                if (e.getDamager() instanceof Player) {
                    Player player = (Player) e.getDamager();
                    npc.callLeftClick(player, npc);
                }
            } else if (e.getDamager() instanceof Player) {
                CreatureTask.onDamage(e.getEntity(), event);
            }
        }
    }

    @EventHandler
    public void onEntityTarget(NPCTargetEvent event) {
        if (CreatureTask.getCreature(event.getEntity()) != null) {
            CreatureTask.getCreature(event.getEntity()).onRightClick((Player) event.getTarget());
        }
        if (NPCManager.isNPC(event.getTarget())) {
            NPCManager.get(event.getTarget()).callTargetEvent(event);
        }
        NPCTargetEvent e = event;
        HumanNPC npc = NPCManager.get(e.getEntity());
        if (npc != null && event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (npc.getNPCData().isLookClose()) {
                NPCManager.faceEntity(npc, player);
            }
            if (UtilityProperties.isHoldingTool("SelectItems", player)) {
                if (!NPCManager.hasSelected(player, npc.getUID())) {
                    NPCDataManager.selectNPC(player, npc);
                    if (PermissionManager.hasPermission(player, "citizens.basic.modify.select"))
                        Messaging.send(player, npc, Settings.getString("SelectionMessage"));
                    if (!Settings.getBoolean("QuickSelect")) {
                        return;
                    }
                }
            }
            // Call NPC talk event
            if (npc.getNPCData().isTalk() && UtilityProperties.isHoldingTool("TalkItems", player)) {
                Player target = (Player) e.getTarget();
                NPCTalkEvent talkEvent = new NPCTalkEvent(npc, target, MessageUtils.getText(npc, target));
                Bukkit.getServer().getPluginManager().callEvent(talkEvent);
                if (!talkEvent.isCancelled()) {
                    if (!talkEvent.getText().isEmpty()) {
                        Messaging.send(target, npc, talkEvent.getText());
                    }
                }
            }
            NPCRightClickEvent rightClickEvent = new NPCRightClickEvent(npc, player);
            Bukkit.getServer().getPluginManager().callEvent(rightClickEvent);
            if (!rightClickEvent.isCancelled()) {
                NPCDataManager.handleEquipmentEditor(rightClickEvent);
                NPCDataManager.handlePathRestart(rightClickEvent);
                if (npc.getWaypoints().isStarted() && npc.getWaypoints().current() != null) {
                    npc.getWaypoints().scheduleDelay(npc, npc.getWaypoints().current().getLocation(),
                            Settings.getInt("RightClickPause"));
                }
                npc.callRightClick(player, npc);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        CreatureTask.onEntityDeath(event.getEntity());
        if (NPCManager.isNPC(event.getEntity())) {
            HumanNPC npc = NPCManager.get(event.getEntity());
            npc.callDeathEvent(event);
            NPCManager.removeForRespawn(npc.getUID());
        }
    }
}