package com.xiaoteng.jork;

import com.xiaoteng.jork.server.Server;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("hello jork.");
        Server server = new Server();
        server.run();
    }

}
