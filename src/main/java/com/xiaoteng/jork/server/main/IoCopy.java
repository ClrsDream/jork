package com.xiaoteng.jork.server.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class IoCopy {

    private final static Logger log = LogManager.getLogger(IoCopy.class);

    private volatile Socket localSocket;

    private volatile Socket clientSocket;

    public IoCopy(Socket localSocket, Socket clientSocket) {
        this.localSocket = localSocket;
        this.clientSocket = clientSocket;
    }

    public void writeToLocal() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(localSocket.getOutputStream()));
            int c;
            while ((c = bufferedReader.read()) != -1) {
                bufferedWriter.write(c);
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToClient() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            int c;
            while ((c = bufferedReader.read()) != -1) {
                bufferedWriter.write(c);
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
