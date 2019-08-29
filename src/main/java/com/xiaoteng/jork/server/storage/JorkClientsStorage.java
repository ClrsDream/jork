package com.xiaoteng.jork.server.storage;

import com.xiaoteng.jork.server.main.JorkClient;

import java.util.ArrayList;

public class JorkClientsStorage {

    private static ArrayList<JorkClient> clients = new ArrayList<>();

    public static void add(JorkClient jorkClient) {
        clients.add(jorkClient);
    }

    public static ArrayList<JorkClient> get() {
        return clients;
    }

    public static JorkClient findByDomain(String domain) {
        for (JorkClient jorkClient : clients) {
            if (jorkClient.getDomain().equals(domain)) {
                return jorkClient;
            }
        }
        return null;
    }

}
