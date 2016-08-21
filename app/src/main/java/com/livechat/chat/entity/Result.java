package com.livechat.chat.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by itszfung on 16/2/27.
 */
public class Result implements Serializable {

    private Integer code;
    private String message;
    private Object data;

    public Result() {

    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object toJSON() {
        return JSONObject.toJSON(this);
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }

    public static Object JSONResult(Integer code, String message) {
        return new Result(code, message).toJSON();
    }

    public static Object JSONResult(Integer code, String message, Object data) {
        Object object = JSON.toJSON(data);
        if (object == null) {
            return new Result(code, message);
        }
        return new Result(code, message, object).toJSON();
    }

    public static String JSONStringResult(Integer code, String message) {
        return new Result(code, message).toJSONString();
    }

    public static String JSONStringResult(Integer code, String message, Object data) {
        return new Result(code, message, JSON.toJSON(data)).toJSONString();
    }

}
