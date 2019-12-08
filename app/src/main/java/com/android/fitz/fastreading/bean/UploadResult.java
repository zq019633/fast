package com.android.fitz.fastreading.bean;

/**
 * Created by fitz on 2017/07/20.
 */

public class UploadResult {
    /**
     * success : true
     * message :
     */

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
