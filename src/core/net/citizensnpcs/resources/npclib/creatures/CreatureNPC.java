package net.citizensnpcs.resources.npclib.creatures;

import net.citizensnpcs.resources.npclib.CraftNPC;
import net.minecraft.server.v1_5_R2.MinecraftServer;
import net.minecraft.server.v1_5_R2.PlayerInteractManager;
import net.minecraft.server.v1_5_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class CreatureNPC extends CraftNPC {
    protected final double range = 25;
    protected final int[] weapons = { 267, 268, 272, 283 };

    public CreatureNPC(MinecraftServer minecraftserver, World world, String s,
            PlayerInteractManager iteminworldmanager) {
        super(minecraftserver, world, s, iteminworldmanager);
    }

    /**
     * Called every tick. This method provides a default action, which can be
     * overridden by subclasses. The default action attempts to target the
     * closest player based on a predefined range, else takes a random path.
     */
    public void doTick() {
        moveTick();
        applyGravity();
    }

    /**
     * Helper method.
     * 
     * @return
     */
    public Player getEntity() {
        return (Player) this.bukkitEntity;
    }

    /**
     * Helper method.
     * 
     * @return
     */
    public Location getLocation() {
        return this.getEntity().getLocation();
    }

    /**
     * Returns the type of this creature.
     */
    public abstract CreatureNPCType getType();

    /**
     * Called when the creature is damaged. Can be used for targeting attacker
     * etc. Provides a damage event parameter. The type of damage should be
     * checked using the event's DamageCause. Function calls on the event will
     * change the underlying instance.
     * 
     * @param event
     */
    public void onDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            Entity damager = e.getDamager();
            if (damager != null && damager instanceof LivingEntity) {
                this.targetAggro = true;
                this.targetEntity = ((CraftEntity) damager).getHandle();
            }
        }
    }

    /**
     * Called when the creature dies. Can be used for loot drops or playing
     * sound effects.
     */
    public abstract void onDeath();

    /**
     * Called when a player left clicks the NPC - this can cause a damage event
     * as well.
     * 
     * @param player
     */
    public abstract void onLeftClick(Player player);

    /**
     * Called when a player right clicks the NPC.
     * 
     * @param player
     */
    public abstract void onRightClick(Player player);

    /**
     * Called when a creature spawns. Provides a default method, which sets the
     * creature to randomly path in updateMove().
     */
    public void onSpawn() {
        this.randomPather = true;
    }
}