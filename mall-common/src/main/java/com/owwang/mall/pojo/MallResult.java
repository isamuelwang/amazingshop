package com.owwang.mall.pojo;

import java.io.Serializable;

public class MallResult implements Serializable{
	
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    
    //构建其他状态的taotaoresult对象
    public static MallResult build(Integer status, String msg, Object data) {
        return new MallResult(status, msg, data);
    }

    public static MallResult ok(Object data) {
        return new MallResult(data);
    }

    public static MallResult ok() {
        return new MallResult(null);
    }

    public MallResult() {

    }

    public static MallResult build(Integer status, String msg) {
        return new MallResult(status, msg, null);
    }

    public MallResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MallResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}