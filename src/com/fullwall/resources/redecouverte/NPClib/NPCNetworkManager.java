package com.fullwall.resources.redecouverte.NPClib;

import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketAddress;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;

public class NPCNetworkManager extends NetworkManager {

	public NPCNetworkManager(Socket paramSocket, String paramString,
			NetHandler paramNetHandler) {
		super(paramSocket, paramString, paramNetHandler);

		try {
			Field f = NetworkManager.class.getDeclaredField("j");
			f.setAccessible(true);
			f.set(this, false);
		} catch (Exception e) {

		}
	}

	@Override
	public void a(NetHandler paramNetHandler) {
	}

	@Override
	public void a(Packet paramPacket) {
	}

	@Override
	public void a(String paramString, Object[] paramArrayOfObject) {
	}

	@Override
	public void a() {
	}

	@Override
	public SocketAddress b() {
		return super.b();
	}

	@Override
	public void c() {
	}

	@Override
	public int d() {
		return 0;
	}
}
