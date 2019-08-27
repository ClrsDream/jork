package com.xiaoteng.jork.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author xiaoteng
 */
public class Helper {

    public static String getChildDomain(String domain) {
        String[] s = domain.split(".");
        return s[0];
    }

    public static String fileGetContent(String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        StringBuilder sb = null;
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

}
