package com.xiaoteng.jork.server.main;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author xiaoteng
 */
public class RegisterTable {

    private static Map<Integer, ArrayList<Client>> table;

    public synchronized static void register(Integer port, Client client) {
        ArrayList<Client> clients = getClients(port);
        clients.add(client);
        table.put(port, clients);
    }

    public static ArrayList<Client> getClients(Integer port) {
        return table.get(port);
    }

}
