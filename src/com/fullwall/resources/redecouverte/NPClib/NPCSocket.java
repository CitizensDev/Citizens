package com.fullwall.resources.redecouverte.NPClib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NPCSocket extends Socket{

    @Override
    public InputStream getInputStream()
    {
        byte[] buf = new byte[1];
        return new ByteArrayInputStream(buf);
    }

    @Override
    public OutputStream getOutputStream()
    {
        return new ByteArrayOutputStream();
    }

}
