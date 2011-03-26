package com.fullwall.resources.redecouverte.NPClib;

import java.lang.reflect.Field;
import java.util.logging.Logger;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

import com.fullwall.Citizens.Citizens;

public class HumanNPC extends NPC {

    private CraftNPC mcEntity;
    private double fallingSpeed = 0.0;
    private double GravityPerSecond = 9.81;
    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Minecraft");

    public HumanNPC(CraftNPC entity, int UID, String name) {
        super(UID, name);

        this.mcEntity = entity;
    }

    public HumanEntity getBukkitEntity() {
        return (HumanEntity) this.mcEntity.getBukkitEntity();
    }

    protected CraftNPC getMCEntity() {
        return this.mcEntity;
    }

    // For Teleportation
    public void moveTo(double x, double y, double z, float yaw, float pitch) {
        this.mcEntity.c(x, y, z, yaw, pitch);
    }
    
    // For NPC movement
    public void moveNPC(double x, double y, double z){
    	this.mcEntity.c(x, y, z);
    }
    
    public void applyGravity(){
    	fallingSpeed -= GravityPerSecond / 100;
    	double prevY = this.mcEntity.locY;
    	this.mcEntity.c(0, fallingSpeed, 0);
    	double diff = this.mcEntity.locY - prevY;
    	if(diff - fallingSpeed > 0.01)
    		fallingSpeed = 0.0;
    }

    public void attackLivingEntity(LivingEntity ent) {
        try {
            this.mcEntity.animateArmSwing();
            Field f = CraftLivingEntity.class.getDeclaredField("entity");
            f.setAccessible(true);
            EntityLiving lEntity = (EntityLiving) f.get(ent);
            this.mcEntity.h(lEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void animateArmSwing()
    {
        this.mcEntity.animateArmSwing();
    }


}
