package com.fullwall.resources.redecouverte.NPClib;

import net.minecraft.server.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class NPCNetHandler extends NetServerHandler {

	public NPCNetHandler(MinecraftServer minecraftserver,
			EntityPlayer entityplayer, NetworkManager netMgr) {
		super(minecraftserver, netMgr, entityplayer);
		netMgr.a(this);
	}

	@Override
	public CraftPlayer getPlayer() {
		return null;
	}

	@Override
	public void a() {
	}

	@Override
	public void a(Packet10Flying packet10flying) {
	}

	@Override
	public void sendMessage(String s) {
	}

	@Override
	public void a(double d0, double d1, double d2, float f, float f1) {
	}

	@Override
	public void a(Packet14BlockDig packet14blockdig) {
	}

	@Override
	public void a(Packet15Place packet15place) {
	}

	@Override
	public void a(String s, Object[] aobject) {
	}

	@Override
	public void a(Packet packet) {
	}

	@Override
	public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
	}

	@Override
	public void a(Packet3Chat packet3chat) {
	}

	@Override
	public void a(Packet255KickDisconnect packet255kickdisconnect) {
	}

	@Override
	public void sendPacket(Packet packet) {
	}

	@Override
	public void a(Packet7UseEntity packet7useentity) {
	}

	@Override
	public void a(Packet9Respawn packet9respawn) {
	}

	@Override
	public void a(Packet101CloseWindow packet101closewindow) {
	}

	@Override
	public void a(Packet102WindowClick packet102windowclick) {
	}

	@Override
	public void a(Packet106Transaction packet106transaction) {
	}

	@Override
	public int b() {
		return super.b();
	}

	@Override
	public void a(Packet130UpdateSign packet130updatesign) {
	}
}