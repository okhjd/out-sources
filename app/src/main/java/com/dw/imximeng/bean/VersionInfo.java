package com.dw.imximeng.bean;

/**
 * @author hjd
 * @Created_Time 2018\7\10 0010
 */
public class VersionInfo {

    private int id;
    private String vnum;
    private String remarks;
    private String showVfile;
    private String showTime;
    /**
     * id : 1
     * vfile : uploads/version/v_15181431909640.apk
     * time : 1518143190
     */

    private String vfile;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVnum() {
        return vnum;
    }

    public void setVnum(String vnum) {
        this.vnum = vnum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getShowVfile() {
        return showVfile;
    }

    public void setShowVfile(String showVfile) {
        this.showVfile = showVfile;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getVfile() {
        return vfile;
    }

    public void setVfile(String vfile) {
        this.vfile = vfile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
