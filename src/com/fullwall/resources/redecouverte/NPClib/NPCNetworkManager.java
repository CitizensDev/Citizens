package com.fullwall.resources.redecouverte.NPClib;

import java.lang.reflect.Field;
import java.net.Socket;
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
	public void a(NetHandler nethandler) {
	}

	@Override
	public void a(Packet packet) {
	}

	@Override
	public void a(String s, Object... aobject) {
	}

	@Override
	public void a() {
	}

	@Override
	public void c() {
	}

	@Override
	public int d() {
		return 0;
	}
}