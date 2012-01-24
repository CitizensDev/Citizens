package net.citizensnpcs.listeners;

import net.citizensnpcs.Settings;
import net.citizensnpcs.TickTask;
import net.citizensnpcs.api.event.NPCTargetEvent;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.ConversationUtils;
import net.citizensnpcs.utils.Web;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListen implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (!PermissionManager.hasPermission(event.getPlayer(), "citizens.admin.notifyupdates")
                || !Settings.getBoolean("NotifyUpdates"))
            return;
        new Thread() {
            @Override
            public void run() {
                Web.notifyUpdate(event.getPlayer());
            }
        }.run();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        CreatureTask.setDirty();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        NPCDataManager.pathEditors.remove(event.getPlayer());
        TickTask.clearActions(event.getPlayer());
        CreatureTask.setDirty();
        ConversationUtils.verify();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        NPCDataManager.handlePathEditor(event);
        if (NPCDataManager.equipmentEditors.containsKey(event.getPlayer())
                && event.getAction() == Action.RIGHT_CLICK_AIR) {
            event.setUseItemInHand(Result.DENY);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        HumanNPC npc = NPCManager.get(event.getRightClicked());
        if (npc != null) {
            EntityTargetEvent rightClickEvent = new NPCTargetEvent(npc.getPlayer(), event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(rightClickEvent);
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        ConversationUtils.onChat(event);
    }
}