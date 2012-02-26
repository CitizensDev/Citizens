package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.questers.QuestManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuesterPlayerListen implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        QuestManager.unload(event.getPlayer());
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }
}