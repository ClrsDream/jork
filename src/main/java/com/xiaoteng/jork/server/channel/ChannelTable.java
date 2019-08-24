package com.xiaoteng.jork.server.channel;

import java.util.Map;

/**
 * @author xiaoteng
 */
public class ChannelTable {

    private static Map<Integer, Channel> table;

    public synchronized static void add(Integer id, Channel channel) {
        table.put(id, channel);
    }

    public static Channel getChannel(Integer id) {
        return table.get(id);
    }

}
