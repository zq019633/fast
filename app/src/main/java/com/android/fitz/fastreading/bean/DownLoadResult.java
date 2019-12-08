package com.android.fitz.fastreading.bean;

/**
 * Created by fitz on 2017/07/13.
 */

public class DownLoadResult {

    /**
     * ok : true
     * lasttime : 20170713143659
     * file : http://dqrx.hzshenlan.com/fastread/upload/32e995ed8fb847fd8683e7997bf114be20170713143659.zip
     */

    private boolean ok;
    private Long lasttime;
    private String file;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Long getLasttime() {
        return lasttime;
    }

    public void setLasttime(Long lasttime) {
        this.lasttime = lasttime;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
