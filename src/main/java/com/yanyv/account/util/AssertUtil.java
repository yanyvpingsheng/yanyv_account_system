package com.yanyv.account.util;

import com.yanyv.account.execptions.ParamsException;

public class AssertUtil {
    public static void isTrue(Boolean flag, String msg) {
        if(flag) {
            throw new ParamsException(msg);
        }
    }
}
