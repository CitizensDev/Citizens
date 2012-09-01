package net.citizensnpcs.resources.npclib;

import java.io.IOException;

import net.citizensnpcs.resources.npclib.NPCAnimator.Animation;
import net.minecraft.server.EnumGamemode;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.World;

import org.bukkit.entity.LivingEntity;

public class CraftNPC extends PathNPC {
    public CraftNPC(MinecraftServer minecraftserver, World world, String s,
            ItemInWorldManager iteminworldmanager) {
        super(minecraftserver, world, s, iteminworldmanager);
        iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL);

        NPCSocket socket = new NPCSocket();
        NetworkManager netMgr;
        try {
            netMgr = new NPCNetworkManager(socket, "npc mgr", new NetHandler() {
                @Override
                public boolean a() {
                    return false;
                }
            }, server.E().getPrivate());
            this.netServerHandler = new NPCNetHandler(minecraftserver, this, netMgr);
            netMgr.a(this.netServerHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException ex) {
        }
    }

    public void applyGravity() {
        return;
        /*
         * if (Citizens.initialized && chunkLoaded() && (!this.onGround ||
         * ((Player) this.getBukkitEntity()) .getEyeLocation().getY() % 1 <=
         * 0.62)) { // onGround only checks if they're at least below 0.62 above
         * it -> // need to check if they actually are standing on the block. //
         * TODO: fix this, as it's broken -- need a good way to do it. }
         */
    }

    @SuppressWarnings("unused")
    private boolean chunkLoaded() {
        return this.bukkitEntity.getWorld().isChunkLoaded(this.npc.getChunkX(), this.npc.getChunkZ());
    }

    public LivingEntity getTarget() {
        return this.targetEntity == null ? null : ((LivingEntity) this.targetEntity.getBukkitEntity());
    }

    public boolean hasTarget() {
        return this.targetEntity != null;
    }

    @Override
    public void performAction(Animation action) {
        this.animations.performAnimation(action);
    }
}