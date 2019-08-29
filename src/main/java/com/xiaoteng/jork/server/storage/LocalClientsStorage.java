package com.xiaoteng.jork.server.storage;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LocalClientsStorage {

    private static Map<Integer, Socket> socketMap = new HashMap<>();

    public static void add(Integer id, Socket socket) {
        socketMap.put(id, socket);
    }

    public static Socket get(Integer id) {
        return socketMap.get(id);
    }

}
