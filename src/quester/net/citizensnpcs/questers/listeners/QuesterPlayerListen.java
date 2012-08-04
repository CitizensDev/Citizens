package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.questers.QuestManager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuesterPlayerListen implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        if (!event.isAsynchronous())
            QuestManager.incrementQuest(event.getPlayer(), event);
        else
            Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin, new Runnable() {
                @Override
                public void run() {
                    QuestManager.incrementQuest(event.getPlayer(), event);
                }
            });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        QuestManager.unload(event.getPlayer());
    }
}