package net.citizensnpcs.resources.npclib;

import java.io.IOException;

import net.citizensnpcs.resources.npclib.NPCAnimator.Animation;
import net.minecraft.server.v1_5_R2.Connection;
import net.minecraft.server.v1_5_R2.EnumGamemode;
import net.minecraft.server.v1_5_R2.MinecraftServer;
import net.minecraft.server.v1_5_R2.NetworkManager;
import net.minecraft.server.v1_5_R2.PlayerInteractManager;
import net.minecraft.server.v1_5_R2.World;

import org.bukkit.entity.LivingEntity;

public class CraftNPC extends PathNPC {
    public CraftNPC(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager iteminworldmanager) {
        super(minecraftserver, world, s, iteminworldmanager);
        iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL);

        NPCSocket socket = new NPCSocket();
        NetworkManager netMgr;
        try {
            netMgr = new NPCNetworkManager(server.getLogger(), socket, "npc mgr", new Connection() {
                @Override
                public boolean a() {
                    return false;
                }
            }, server.F().getPrivate());
            this.playerConnection = new NPCNetHandler(minecraftserver, this, netMgr);
            netMgr.a(this.playerConnection);
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