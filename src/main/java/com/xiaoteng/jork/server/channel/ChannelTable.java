package com.xiaoteng.jork.server.channel;

import java.util.Map;

/**
 * @author xiaoteng
 */
public class ChannelTable {

    private static Map<Integer, Channel> table;

    private static Map<Integer, Integer> clientMap;

    public synchronized static void add(Integer id, Channel channel) {
        table.put(id, channel);
    }

    public static Channel getChannel(Integer id) {
        return table.get(id);
    }

    public synchronized static void registerClient(Integer clientSocketId, Integer userSocketId) {
        clientMap.put(clientSocketId, userSocketId);
    }

    public static Integer getUserClientId(Integer clientSocketId) {
        return clientMap.get(clientSocketId);
    }

}
