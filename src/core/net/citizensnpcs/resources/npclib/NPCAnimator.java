package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.utils.PacketUtils;
import net.minecraft.server.v1_5_R1.DataWatcher;
import net.minecraft.server.v1_5_R1.Packet17EntityLocationAction;
import net.minecraft.server.v1_5_R1.Packet18ArmAnimation;
import net.minecraft.server.v1_5_R1.Packet40EntityMetadata;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCAnimator {
    private final PathNPC npc;

    public NPCAnimator(PathNPC pathNPC) {
        this.npc = pathNPC;
    }

    private void actHurt() {
        PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, getArmAnimationPacket(2), getPlayer());
    }

    private Packet18ArmAnimation getArmAnimationPacket(int data) {
        return new Packet18ArmAnimation(this.npc, data);
    }

    private Player getPlayer() {
        return this.npc.getBukkitEntity();
    }

    private DataWatcher getWatcher() {
        return this.npc.getDataWatcher();
    }

    public void performAnimation(Animation animation) {
        switch (animation) {
            case ACT_HURT:
                actHurt();
                break;
            case SNEAK:
                sneak();
                break;
            case SLEEP:
                sleep();
                break;
            case SWING_ARM:
                swingArm();
                break;
            case UNSLEEP:
                unsleep();
                break;
            case UNSNEAK:
                unsneak();
                break;
            default:
                break;
        }
    }

    private void sendMetadataPacket() {
        PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, new Packet40EntityMetadata(this.npc.id,
                getWatcher(), true), getPlayer());
    }

    private void sleep() {
        Location loc = getPlayer().getLocation();
        Packet17EntityLocationAction packet17 = new Packet17EntityLocationAction(npc, 0, loc.getBlockX(),
                loc.getBlockY(), loc.getBlockZ());

        // getPlayer().teleport(loc);
        PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet17, getPlayer());
    }

    private void sneak() {
        npc.setSneaking(true);
        sendMetadataPacket();
    }

    private void swingArm() {
        PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, getArmAnimationPacket(1), getPlayer());
    }

    private void unsleep() {
        PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, new Packet18ArmAnimation(this.npc, 3),
                getPlayer());
    }

    private void unsneak() {
        npc.setSneaking(false);
        sendMetadataPacket();
    }

    public enum Animation {
        ACT_HURT,
        SLEEP,
        SNEAK,
        SWING_ARM,
        UNSLEEP,
        UNSNEAK;
    }
}