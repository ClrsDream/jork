package com.xiaoteng.jork.server;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author xiaoteng
 */
public class RegisterTable {

    private Map<Integer, ArrayList<Client>> table;

    public synchronized void register(Integer port, Client client) {
        ArrayList<Client> clients = this.getClients(port);
        clients.add(client);
        this.table.put(port, clients);
    }

    public ArrayList<Client> getClients(Integer port) {
        return this.table.get(port);
    }

}
