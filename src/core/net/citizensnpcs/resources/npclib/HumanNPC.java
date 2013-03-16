package net.citizensnpcs.resources.npclib;

import java.util.Collection;
import java.util.Map;

import net.citizensnpcs.npcdata.NPCData;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.NPCAnimator.Animation;
import net.citizensnpcs.waypoints.WaypointPath;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.collect.MapMaker;

public class HumanNPC extends NPC {
    private double balance;
    private final CraftNPC mcEntity;
    private NPCData npcdata = new NPCData();
    private boolean paused;
    private final Map<String, CitizensNPC> types = new MapMaker().makeMap();

    private WaypointPath waypoints = new WaypointPath();

    public HumanNPC(CraftNPC entity, int UID, String name) {
        super(UID, name);
        this.mcEntity = entity;
        this.mcEntity.npc = this;
    }

    public <T extends CitizensNPC> void addType(String type) {
        if (!NPCTypeManager.validType(type))
            throw new IllegalArgumentException("illegal type: " + type + " added to: " + this.getUID());
        registerType(type);
        PropertyManager.load(type, this);
    }

    public void callDamageEvent(EntityDamageEvent event) {
        if (types.size() == 0) {
            event.setCancelled(true);
            return;
        }
        for (CitizensNPC type : types.values()) {
            type.onDamage(event);
        }
    }

    public void callDeathEvent(EntityDeathEvent event) {
        for (CitizensNPC type : types.values()) {
            type.onDeath(event);
        }
    }

    public void callLeftClick(Player player, HumanNPC npc) {
        for (CitizensNPC type : types.values()) {
            type.onLeftClick(player, npc);
        }
    }

    public void callRightClick(Player player, HumanNPC npc) {
        for (CitizensNPC type : types.values()) {
            type.onRightClick(player, npc);
        }
    }

    public void callTargetEvent(EntityTargetEvent event) {
        for (CitizensNPC type : types.values()) {
            type.onTarget(event);
        }
    }

    public void doTick() {
        this.mcEntity.moveTick();
        this.mcEntity.applyGravity();
    }

    public double getBalance() {
        return this.balance;
    }

    public Location getBaseLocation() {
        return this.waypoints.current() != null ? this.waypoints.current().getLocation() : this.npcdata
                .getLocation();
    }

    public int getChunkX() {
        return this.getLocation().getBlockX() >> 4;
    }

    public int getChunkZ() {
        return this.getLocation().getBlockZ() >> 4;
    }

    public CraftNPC getHandle() {
        return this.mcEntity;
    }

    public PlayerInventory getInventory() {
        return this.getPlayer().getInventory();
    }

    public ItemStack getItemInHand() {
        return this.getPlayer().getItemInHand();
    }

    public Location getLocation() {
        return this.getPlayer().getLocation();
    }

    public NPCData getNPCData() {
        return npcdata;
    }

    public String getOwner() {
        return this.npcdata.getOwner();
    }

    public Player getPlayer() {
        return this.mcEntity.getBukkitEntity();
    }

    @SuppressWarnings("unchecked")
    public <T> T getType(String type) {
        return (T) this.types.get(type);
    }

    public WaypointPath getWaypoints() {
        if (waypoints == null) {
            this.waypoints = new WaypointPath();
        }
        return this.waypoints;
    }

    public World getWorld() {
        return this.getPlayer().getWorld();
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isType(String type) {
        return this.types.get(type) != null;
    }

    public void performAction(Animation action) {
        this.mcEntity.performAction(action);
    }

    public void registerType(String type) {
        this.types.put(type, NPCTypeManager.getType(type).getInstance());
    }

    public void removeType(String type) {
        this.types.remove(type);
        PropertyManager.save(type, this);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setItemInHand(ItemStack item) {
        this.getPlayer().setItemInHand(item);
    }

    public void setNPCData(NPCData npcdata) {
        this.npcdata = npcdata;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void teleport(double x, double y, double z, float yaw, float pitch) {
        this.mcEntity.setLocation(x, y, z, yaw, pitch);
    }

    public void teleport(Location loc) {
        boolean multiworld = loc.getWorld() != this.getWorld();
        this.getPlayer().teleport(loc);
        if (multiworld) {
            ((CraftServer) Bukkit.getServer()).getHandle().players.remove(this.mcEntity);
        }
    }

    public Collection<CitizensNPC> types() {
        return this.types.values();
    }
}