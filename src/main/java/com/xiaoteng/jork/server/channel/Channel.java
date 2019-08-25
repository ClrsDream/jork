package com.xiaoteng.jork.server.channel;

import java.net.Socket;

/**
 * @author xiaoteng
 */
public class Channel {

    private Socket userSocket;

    private Socket clientSocket;

    public Channel(Socket userSocket, Socket clientSocket) {
        this.userSocket = userSocket;
        this.clientSocket = clientSocket;
    }

    public Socket getUserSocket() {
        return userSocket;
    }

    public void setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
