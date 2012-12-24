package net.citizensnpcs.resources.npclib;

import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.MinecraftServer;
import net.minecraft.server.v1_4_6.NetworkManager;
import net.minecraft.server.v1_4_6.Packet;
import net.minecraft.server.v1_4_6.Packet102WindowClick;
import net.minecraft.server.v1_4_6.Packet106Transaction;
import net.minecraft.server.v1_4_6.Packet10Flying;
import net.minecraft.server.v1_4_6.Packet130UpdateSign;
import net.minecraft.server.v1_4_6.Packet14BlockDig;
import net.minecraft.server.v1_4_6.Packet15Place;
import net.minecraft.server.v1_4_6.Packet16BlockItemSwitch;
import net.minecraft.server.v1_4_6.Packet255KickDisconnect;
import net.minecraft.server.v1_4_6.Packet28EntityVelocity;
import net.minecraft.server.v1_4_6.Packet3Chat;
import net.minecraft.server.v1_4_6.Packet51MapChunk;
import net.minecraft.server.v1_4_6.PlayerConnection;

public class NPCNetHandler extends PlayerConnection {
    public NPCNetHandler(MinecraftServer minecraftserver, EntityPlayer entityplayer, NetworkManager netMgr) {
        super(minecraftserver, netMgr, entityplayer);
    }

    @Override
    public void a(Packet102WindowClick packet102windowclick) {
    }

    @Override
    public void a(Packet106Transaction packet106transaction) {
    }

    @Override
    public void a(Packet10Flying packet10flying) {
    }

    @Override
    public void a(Packet130UpdateSign packet130updatesign) {
    }

    @Override
    public void a(Packet14BlockDig packet14blockdig) {
    }

    @Override
    public void a(Packet15Place packet15place) {
    }

    @Override
    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
    }

    @Override
    public void a(Packet255KickDisconnect packet255kickdisconnect) {
    }

    @Override
    public void a(Packet28EntityVelocity packet28entityvelocity) {
    }

    @Override
    public void a(Packet3Chat packet3chat) {
    }

    @Override
    public void a(Packet51MapChunk packet50mapchunk) {
    }

    @Override
    public void a(String s, Object[] aobject) {
    }

    @Override
    public void onUnhandledPacket(Packet packet) {
    }

    @Override
    public void sendPacket(Packet packet) {
    }
}