package com.xiaoteng.jork.client.storage;

import java.net.Socket;

public class LocalClientStorage {

    private static Socket socket;

    public static Socket getSocket() {
        return socket;
    }

    public synchronized static void setSocket(Socket socket) {
        LocalClientStorage.socket = socket;
    }
}
