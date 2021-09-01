package com.yanyv.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

    //@ApiModelProperty("响应状态")
    private long code;
    private String message;
    private Object obj;

    public static RespBean success(String message, Object obj) {
        return new RespBean(200, message, obj);
    }

    public static RespBean success(String message) {
        return new RespBean(200, message, null);
    }

    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }

    public static RespBean error(String message, Object obj) {
        return new RespBean(500, message, null);
    }

}
