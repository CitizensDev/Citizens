package net.citizensnpcs.guards;

import net.citizensnpcs.Settings;
import net.citizensnpcs.TickTask;
import net.citizensnpcs.guards.flags.FlagList;
import net.citizensnpcs.guards.types.GuardStatus;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Guard extends CitizensNPC {
    private final FlagList flags = new FlagList();
    private GuardState guardState = GuardState.NULL;
    private boolean isAggressive = true;
    private double radius = 10;

    // Get a guard's blacklist
    public FlagList getFlags() {
        return flags;
    }

    // Get the protection radius for a bouncer
    public double getProtectionRadius() {
        return radius;
    }

    @Override
    public CitizensNPCType getType() {
        return new GuardType();
    }

    // Get whether a bodyguard NPC kills on sight
    public boolean isAggressive() {
        return isAggressive;
    }

    private boolean isCoOwned(Entity damager, HumanNPC npc) {
        HumanNPC other = NPCManager.get(damager);
        return other != null && other.getOwner().equalsIgnoreCase(npc.getOwner());
    }

    private boolean isOwner(Entity damager, HumanNPC npc) {
        return damager instanceof Player ? NPCManager.isOwner((Player) damager, npc.getUID()) : false;
    }

    public boolean isState(GuardState state) {
        return guardState == state;
    }

    @Override
    public void load(Storage profiles, int UID) {
        guardState = GuardState.parse(profiles.getString(UID + ".guard.type"));
        isAggressive = profiles.getBoolean(UID + ".guard.aggressive");
        radius = profiles.getDouble(UID + ".guard.radius",
                Settings.getDouble("DefaultBouncerProtectionRadius"));
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent))
            return;
        EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
        if (!(ev.getDamager() instanceof LivingEntity))
            return;
        HumanNPC npc = NPCManager.get(event.getEntity());
        if (isOwner(ev.getDamager(), npc) || isCoOwned(ev.getDamager(), npc)) {
            event.setCancelled(true);
            return;
        }
        if (guardState != GuardState.NULL)
            guardState.getUpdater().onDamage(npc, (LivingEntity) ev.getDamager());
        else if (this.isAggressive) {
            target((LivingEntity) ev.getDamager(), npc);
        }
        event.setCancelled(false);
    }

    @Override
    public void onDeath(EntityDeathEvent event) {
        HumanNPC npc = NPCManager.get(event.getEntity());
        Player player = Bukkit.getServer().getPlayerExact(npc.getOwner());
        if (player != null) {
            player.sendMessage(ChatColor.GRAY + "Your guard NPC "
                    + StringUtils.wrap(npc.getName(), ChatColor.GRAY) + " died.");
        }
        event.getDrops().clear();
        TickTask.scheduleRespawn(npc, Settings.getInt("GuardRespawnDelay"));
    }

    @Override
    public void save(Storage profiles, int UID) {
        profiles.setString(UID + ".guard.type", guardState.name());
        profiles.setBoolean(UID + ".guard.aggressive", isAggressive);
        profiles.setDouble(UID + ".guard.radius", radius);
    }

    // Set whether a bodyguard kills on sight
    public void setAggressive(boolean state) {
        this.isAggressive = state;
    }

    // Set the type of a guard that a guard NPC is
    public void setGuardState(GuardState guardState) {
        this.guardState = guardState;
    }

    // Set the protection radius for a bouncer
    public void setProtectionRadius(double radius) {
        this.radius = radius;
    }

    public void target(LivingEntity entity, HumanNPC npc) {
        if (isOwner(entity, npc) || isCoOwned(entity, npc))
            return;
        if (Settings.getBoolean("RealisticPathing")
                && !npc.getHandle().isInSight(((CraftEntity) entity).getHandle()))
            return;
        npc.setPaused(true);
        PathUtils.target(npc, entity, true, -1, -1, Settings.getDouble("PathfindingRange"));
    }

    public GuardStatus updateStatus(GuardStatus guardStatus, HumanNPC npc) {
        return guardState == GuardState.NULL ? GuardStatus.NORMAL : guardState.getUpdater().updateStatus(
                guardStatus, npc);
    }
}