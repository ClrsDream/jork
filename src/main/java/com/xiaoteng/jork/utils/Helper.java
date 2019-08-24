package com.xiaoteng.jork.utils;

/**
 * @author xiaoteng
 */
public class Helper {

    public static String getChildDomain(String domain) {
        String[] s = domain.split(".");
        return s[0];
    }

}
