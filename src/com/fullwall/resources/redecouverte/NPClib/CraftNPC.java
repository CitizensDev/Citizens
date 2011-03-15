package com.fullwall.resources.redecouverte.NPClib;

import java.util.logging.Logger;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityTargetEvent;

public class CraftNPC extends EntityPlayer {

    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Minecraft");
    private int lastTargetId;
    private long lastBounceTick;
    private int lastBounceId;

    public CraftNPC(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
        super(minecraftserver, world, s, iteminworldmanager);

        NetworkManager netMgr = new NPCNetworkManager(new NPCSocket(), "npc mgr", null);
        this.a = new NPCNetHandler(minecraftserver, this, netMgr);

        this.lastTargetId = -1;
        this.lastBounceId = -1;
        this.lastBounceTick = 0;
    }

    public void animateArmSwing() {
        this.b.k.a(this, new Packet18ArmAnimation(this, 1));
    }

    @Override
    public boolean a(EntityHuman entity) {

        EntityTargetEvent event = new NPCEntityTargetEvent(getBukkitEntity(), entity.getBukkitEntity(), NPCEntityTargetEvent.NpcTargetReason.NPC_RIGHTCLICKED);
        CraftServer server = ((WorldServer) this.world).getServer();
        server.getPluginManager().callEvent(event);

        return super.a(entity);
    }

    @Override
    public void b(EntityHuman entity) {
        if (lastTargetId == -1 || lastTargetId != entity.id) {
            EntityTargetEvent event = new NPCEntityTargetEvent(getBukkitEntity(), entity.getBukkitEntity(), NPCEntityTargetEvent.NpcTargetReason.CLOSEST_PLAYER);
            CraftServer server = ((WorldServer) this.world).getServer();
            server.getPluginManager().callEvent(event);
        }
        lastTargetId = entity.id;

        super.b(entity);
    }

    @Override
    public void c(Entity entity) {
        if (lastBounceId != entity.id || System.currentTimeMillis() - lastBounceTick > 1000) {
            EntityTargetEvent event = new NPCEntityTargetEvent(getBukkitEntity(), entity.getBukkitEntity(), NPCEntityTargetEvent.NpcTargetReason.NPC_BOUNCED);
            CraftServer server = ((WorldServer) this.world).getServer();
            server.getPluginManager().callEvent(event);
            
            lastBounceTick = System.currentTimeMillis();
        }

        lastBounceId = entity.id;

        super.c(entity);
    }
}
