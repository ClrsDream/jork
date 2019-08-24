package com.xiaoteng.jork.server.channel;

import java.net.Socket;

/**
 * @author xiaoteng
 */
public class Channel {

    private Socket fromSocket;

    private Socket toSocket;

    public Channel(Socket fromSocket, Socket toSocket) {
        this.fromSocket = fromSocket;
        this.toSocket = toSocket;
    }

    public Socket getFromSocket() {
        return fromSocket;
    }

    public void setFromSocket(Socket fromSocket) {
        this.fromSocket = fromSocket;
    }

    public Socket getToSocket() {
        return toSocket;
    }

    public void setToSocket(Socket toSocket) {
        this.toSocket = toSocket;
    }
}
