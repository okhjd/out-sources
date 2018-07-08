package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\8 0008
 */
public class MyCollection {
    private int iid;
    private int uid;
    private String showName;
    private String hportrait;
    private String title;
    private String content;
    private String showTime;
    private String telephone;
    private String showTelephone;
    private int is_stick;
    private boolean is_new;
    private int collectnum;
    private int commentnum;
    private boolean iscollect;
    private List<ImgListBean> imgList;

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getShowTelephone() {
        return showTelephone;
    }

    public void setShowTelephone(String showTelephone) {
        this.showTelephone = showTelephone;
    }

    public int getIs_stick() {
        return is_stick;
    }

    public void setIs_stick(int is_stick) {
        this.is_stick = is_stick;
    }

    public boolean isIs_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public int getCollectnum() {
        return collectnum;
    }

    public void setCollectnum(int collectnum) {
        this.collectnum = collectnum;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public boolean isIscollect() {
        return iscollect;
    }

    public void setIscollect(boolean iscollect) {
        this.iscollect = iscollect;
    }

    public List<ImgListBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgListBean> imgList) {
        this.imgList = imgList;
    }
}
