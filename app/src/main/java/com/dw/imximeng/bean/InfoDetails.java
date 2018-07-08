package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class InfoDetails {

    /**
     * id : 1
     * uid : 1
     * cid : 1
     * hportrait : http://test.dingwei.cn/xmcircle/uploads/member/img_15260218659090.png?r=517609
     * numopen : 1
     * title : 二手电脑出售，有意者联系
     * content : 酷狗繁星网，是酷狗于2012年倾力打造的在线视频互动演艺平台。针对目前中国音乐产业面临转型，演艺市场的兴起以及线上数字音乐市场的逐渐成熟，为顺应新的产业环境和结构，繁星网应运而生。作为中国数字音乐领域的领跑者
     * attribute : [{"code":"color","vals":"\u767d\u8272"},{"code":"size","vals":"10"},{"code":"changd","vals":"1,3"}]
     * telephone : 15123333333
     * is_stick : 1
     * collectnum : 1
     * time : 1517387186
     * sh_status : 1
     * catename : 房屋
     * iscomment : 0
     * cateicon : http://test.dingwei.cn/xmcircle/uploads/service_cate//icon_15305820975516.png
     * showName : 张三
     * showTime : 5个月前
     * is_new : false
     * imgList : [{"id":"1","img":"uploads/advert//img_15254166583099.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254166583099.jpg"},{"id":"2","img":"uploads/advert//img_15254167134447.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254167134447.jpg"}]
     * commentnum : 0.0
     * iscollect : false
     * isreport : false
     * showTelephone : 15123333333
     * attrList : [{"name":"颜色","vals":"白色"},{"name":"大小","vals":"10米"},{"name":"长短","vals":"1米，3米"}]
     * commentList : []
     */

    private String id;
    private String uid;
    private String cid;
    private String hportrait;
    private String numopen;
    private String title;
    private String content;
    private String attribute;
    private String telephone;
    private String is_stick;
    private String collectnum;
    private String time;
    private String sh_status;
    private String catename;
    private String iscomment;
    private String cateicon;
    private String showName;
    private String showTime;
    private boolean is_new;
    private double commentnum;
    private boolean iscollect;
    private boolean isreport;
    private String showTelephone;
    private List<ImgListBean> imgList;
    private List<AttrListBean> attrList;
    private List<?> commentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getHportrait() {
        return hportrait;
    }

    public void setHportrait(String hportrait) {
        this.hportrait = hportrait;
    }

    public String getNumopen() {
        return numopen;
    }

    public void setNumopen(String numopen) {
        this.numopen = numopen;
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

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIs_stick() {
        return is_stick;
    }

    public void setIs_stick(String is_stick) {
        this.is_stick = is_stick;
    }

    public String getCollectnum() {
        return collectnum;
    }

    public void setCollectnum(String collectnum) {
        this.collectnum = collectnum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSh_status() {
        return sh_status;
    }

    public void setSh_status(String sh_status) {
        this.sh_status = sh_status;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public String getIscomment() {
        return iscomment;
    }

    public void setIscomment(String iscomment) {
        this.iscomment = iscomment;
    }

    public String getCateicon() {
        return cateicon;
    }

    public void setCateicon(String cateicon) {
        this.cateicon = cateicon;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public boolean isIs_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public double getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(double commentnum) {
        this.commentnum = commentnum;
    }

    public boolean isIscollect() {
        return iscollect;
    }

    public void setIscollect(boolean iscollect) {
        this.iscollect = iscollect;
    }

    public boolean isIsreport() {
        return isreport;
    }

    public void setIsreport(boolean isreport) {
        this.isreport = isreport;
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

    public List<AttrListBean> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<AttrListBean> attrList) {
        this.attrList = attrList;
    }

    public List<?> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<?> commentList) {
        this.commentList = commentList;
    }

    public static class AttrListBean {
        /**
         * name : 颜色
         * vals : 白色
         */

        private String name;
        private String vals;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVals() {
            return vals;
        }

        public void setVals(String vals) {
            this.vals = vals;
        }
    }
}
