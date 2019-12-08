package com.android.fitz.fastreading.bean;

/**
 * Created by fitz on 2017/07/31.
 */

public class CommonResult {

    /**
     * ok : false
     * msg : 授权码已被使用
     */

    private boolean ok;
    private String msg;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
