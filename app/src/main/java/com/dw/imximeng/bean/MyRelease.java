package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\8 0008
 */
public class MyRelease {
    private int id;
    private String title;
    private String content;
    private String telephone;
    private int sh_status;//审核状态（0：待审核，1：审核通过，-1：审核失败）
    private String sh_remarks;
    private String showShtime;
    private int is_stick;//是否置顶（1：是，0：否）
    private boolean is_new;
    private int collectnum;
    private int commentnum;
    private String showTime;
    private boolean iscollect;
    private String showTelephone;
    private List<ImgListBean> imgList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getSh_status() {
        return sh_status;
    }

    public void setSh_status(int sh_status) {
        this.sh_status = sh_status;
    }

    public String getSh_remarks() {
        return sh_remarks;
    }

    public void setSh_remarks(String sh_remarks) {
        this.sh_remarks = sh_remarks;
    }

    public String getShowShtime() {
        return showShtime;
    }

    public void setShowShtime(String showShtime) {
        this.showShtime = showShtime;
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

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public boolean isIscollect() {
        return iscollect;
    }

    public void setIscollect(boolean iscollect) {
        this.iscollect = iscollect;
    }

    public String getShowTelephone() {
        return showTelephone;
    }

    public void setShowTelephone(String showTelephone) {
        this.showTelephone = showTelephone;
    }

    public List<ImgListBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgListBean> imgList) {
        this.imgList = imgList;
    }
}
