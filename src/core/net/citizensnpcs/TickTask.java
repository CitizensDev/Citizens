package net.citizensnpcs;

import java.util.List;

import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.WaypointPath;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;

public class TickTask implements Runnable {
    private static final SetMultimap<HumanNPC, String> cachedActions = HashMultimap.create();
    private static final List<String> toRemove = Lists.newArrayList();

    @Override
    public void run() {
        Player[] online = Bukkit.getServer().getOnlinePlayers();
        if (toRemove.size() > 0) {
            synchronized (toRemove) {
                cachedActions.values().removeAll(toRemove);
                toRemove.clear();
            }
        }
        for (HumanNPC npc : NPCManager.getList().values()) {
            updateWaypoints(npc);
            npc.doTick();
            if (!npc.getNPCData().isLookClose() && !npc.getNPCData().isTalkClose())
                continue;
            boolean canLookClose = npc.getHandle().pathFinished() && !npc.getHandle().hasTarget()
                    && npc.getNPCData().isLookClose();
            if (!npc.getNPCData().isTalkClose() && !canLookClose)
                continue;
            Location npcLoc = npc.getLocation();
            for (Player player : online) {
                // If the player is within 'seeing' range
                if (LocationUtils.withinRange(npcLoc, player.getLocation(), Settings.getDouble("NPCRange"))) {
                    if (canLookClose) {
                        NPCManager.faceEntity(npc, player);
                    }
                    if (npc.getNPCData().isTalkClose())
                        cacheActions(npc, player);
                } else {
                    cachedActions.remove(npc, player.getName());
                }
            }
        }
    }

    private void updateWaypoints(HumanNPC npc) {
        WaypointPath waypoints = npc.getWaypoints();
        switch (waypoints.size()) {
            case 0:
                break;
            case 1:
                // TODO: merge the default and this case.
                if (waypoints.currentIndex() >= 1) {
                    if (!waypoints.isStarted()) {
                        waypoints.schedule(npc, 0);
                    }
                    if (waypoints.isStarted() && !npc.isPaused() && npc.getHandle().pathFinished()) {
                        waypoints.setIndex(0);
                    }
                } else {
                    if (!npc.getWaypoints().isStarted()) {
                        PathUtils.createPath(npc, npc.getNPCData().getLocation(), -1, -1,
                                Settings.getDouble("PathfindingRange"));
                        waypoints.setStarted(true);
                    }
                    if (waypoints.isStarted() && !npc.isPaused() && npc.getHandle().pathFinished()) {
                        waypoints.setIndex(1);
                    }
                }
                if (waypoints.isStarted() && !npc.isPaused() && npc.getHandle().pathFinished()) {
                    waypoints.setStarted(false);
                    waypoints.onReach(npc);
                }
                break;
            default:
                if (!waypoints.isStarted()) {
                    waypoints.scheduleNext(npc);
                }
                if (waypoints.isStarted() && !npc.isPaused() && npc.getHandle().pathFinished()) {
                    waypoints.setIndex(waypoints.currentIndex() + 1);
                    waypoints.setStarted(false);
                    waypoints.onReach(npc);
                }
        }
    }

    private static void cacheActions(HumanNPC npc, Player player) {
        if (!cachedActions.containsEntry(npc, player.getName().toLowerCase())
                && npc.getNPCData().isTalkClose()) {
            MessageUtils.sendText(npc, player);
            cachedActions.put(npc, player.getName().toLowerCase());
        }
    }

    public static void scheduleRespawn(HumanNPC npc, int delay) {
        new RespawnTask(npc).register(delay);
    }

    public static class RespawnTask implements Runnable {
        private final int UID;
        private final String owner;

        public RespawnTask(HumanNPC npc) {
            this.UID = npc.getUID();
            this.owner = npc.getOwner();
        }

        public void register(int delay) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin, this, delay);
        }

        @Override
        public void run() {
            NPCManager.register(UID, owner, NPCCreateReason.RESPAWN);
            Messaging.sendUncertain(owner, StringUtils.wrap(NPCManager.get(UID).getName())
                    + " has respawned.");
        }
    }

    public static void clearActions(Player player) {
        synchronized (toRemove) {
            toRemove.add(player.getName().toLowerCase());
        }
    }
}