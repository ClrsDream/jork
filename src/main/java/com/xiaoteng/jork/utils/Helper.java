package com.xiaoteng.jork.utils;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.messages.ActionMessage;

import java.io.*;
import java.net.Socket;

/**
 * 一些辅助函数
 *
 * @author xiaoteng
 */
public class Helper {

    public static String getChildDomain(String domain) {
        String[] s = domain.split(".");
        if (s.length == 0) {
            return domain;
        }
        return s[0];
    }

    public static String fileGetContent(String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * io复制
     *
     * @param fromSocket Socket
     * @param toSocket   Socket
     * @throws IOException
     */
    public static void ioCopy(Socket fromSocket, Socket toSocket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fromSocket.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(toSocket.getOutputStream()));
        int c;
        while ((c = bufferedReader.read()) != -1) {
            bufferedWriter.write(c);
            bufferedWriter.flush();
        }
    }

    public static <T> void sendMessage(String method, T t, Socket socket) throws IOException {
        ActionMessage actionMessage = new ActionMessage(method, JSON.toJSONString(t));
        String s = JSON.toJSONString(actionMessage);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(s);
        printWriter.flush();
    }

}
