package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class CommentItem {
    private int id;
    private int uid;
    private String showName;
    private String hportrait;
    private String content;
    private String showTime;
    private List<PinInfo> pinfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getHportrait() {
        return hportrait;
    }

    public void setHportrait(String hportrait) {
        this.hportrait = hportrait;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public List<PinInfo> getPinfo() {
        return pinfo;
    }

    public void setPinfo(List<PinInfo> pinfo) {
        this.pinfo = pinfo;
    }

    public static class PinInfo{
        private int uid;
        private String showName;
        private String hportrait;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public String getHportrait() {
            return hportrait;
        }

        public void setHportrait(String hportrait) {
            this.hportrait = hportrait;
        }
    }
}
