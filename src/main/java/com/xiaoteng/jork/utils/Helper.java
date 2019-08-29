package com.xiaoteng.jork.utils;

import java.io.*;
import java.net.Socket;

/**
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

    public static void ioCopy(Socket fromSocket, Socket toSocket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fromSocket.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(toSocket.getOutputStream()));
        int c;
        while ((c = bufferedReader.read()) != -1) {
            bufferedWriter.write(c);
            bufferedWriter.flush();
        }
    }

}
