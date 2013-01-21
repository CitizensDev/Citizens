package net.citizensnpcs.resources.npclib;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.security.PrivateKey;

import net.minecraft.server.v1_4_R1.Connection;
import net.minecraft.server.v1_4_R1.NetworkManager;
import net.minecraft.server.v1_4_R1.Packet;

public class NPCNetworkManager extends NetworkManager {
    public NPCNetworkManager(Socket paramSocket, String paramString, Connection paramNetHandler,
            PrivateKey key) throws IOException {
        super(paramSocket, paramString, paramNetHandler, key);

        if (THREAD_STOPPER != null) {
            try {
                THREAD_STOPPER.set(this, false);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void a() {
    }

    @Override
    public void a(Connection nethandler) {
    }

    @Override
    public void a(String s, Object... aobject) {
    }

    @Override
    public void b() {
    }

    @Override
    public void d() {
    }

    @Override
    public int e() {
        return 0;
    }

    @Override
    public void queue(Packet packet) {
    }

    private static Field THREAD_STOPPER;

    static {
        try {
            THREAD_STOPPER = NetworkManager.class.getDeclaredField("m");
            THREAD_STOPPER.setAccessible(true);
        } catch (Exception ex) {
            THREAD_STOPPER = null;
        }
    }
}