package com.yanyv.account.util;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmail(String str) {
        return str.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    }

    public static boolean isNotEmail(String str) {
        return !isEmail(str);
    }
}
