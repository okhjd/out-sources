package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class UserHome {

    /**
     * userInfo : {"id":"1","hportrait":"http://test.dingwei.cn/xmcircle/uploads/member/img_15260218659090.png?r=517609","nickname":"张三","signature":"身体健康，生活美好！","numopen":"1","hpageopen":"1","showPhone":"151*****333","hpageimg":"http://test.dingwei.cn/xmcircle/uploads/siteconfig//hgimg_15305881301588.png"}
     * list : [{"id":"1","title":"二手电脑出售，有意者联系","content":"酷狗繁星网，是酷狗于2012年倾力打造的在线视频互动演艺平台。针对目前中国音乐产业面临转型，演艺市场的兴起以及线上数字音乐市场的逐渐成熟，为顺应新的产业环境和结构，繁星网应运而生。作为中国数字音乐领域的领跑者","telephone":"15123333333","is_stick":"1","collectnum":"2","time":"1517387186","showTime":"5个月前","is_new":false,"imgList":[{"id":"1","img":"uploads/advert//img_15254166583099.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254166583099.jpg"},{"id":"2","img":"uploads/advert//img_15254167134447.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254167134447.jpg"}],"commentnum":0,"iscollect":true,"showTelephone":"15123333333","showMonth":"1月","showDay":"31"}]
     */

    private UserInfoBean userInfo;
    private List<ListBean> list;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class UserInfoBean {
        /**
         * id : 1
         * hportrait : http://test.dingwei.cn/xmcircle/uploads/member/img_15260218659090.png?r=517609
         * nickname : 张三
         * signature : 身体健康，生活美好！
         * numopen : 1
         * hpageopen : 1
         * showPhone : 151*****333
         * hpageimg : http://test.dingwei.cn/xmcircle/uploads/siteconfig//hgimg_15305881301588.png
         */

        private String id;
        private String hportrait;
        private String nickname;
        private String signature;
        private String numopen;
        private String hpageopen;
        private String showPhone;
        private String hpageimg;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHportrait() {
            return hportrait;
        }

        public void setHportrait(String hportrait) {
            this.hportrait = hportrait;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getNumopen() {
            return numopen;
        }

        public void setNumopen(String numopen) {
            this.numopen = numopen;
        }

        public String getHpageopen() {
            return hpageopen;
        }

        public void setHpageopen(String hpageopen) {
            this.hpageopen = hpageopen;
        }

        public String getShowPhone() {
            return showPhone;
        }

        public void setShowPhone(String showPhone) {
            this.showPhone = showPhone;
        }

        public String getHpageimg() {
            return hpageimg;
        }

        public void setHpageimg(String hpageimg) {
            this.hpageimg = hpageimg;
        }
    }

    public static class ListBean {
        /**
         * id : 1
         * title : 二手电脑出售，有意者联系
         * content : 酷狗繁星网，是酷狗于2012年倾力打造的在线视频互动演艺平台。针对目前中国音乐产业面临转型，演艺市场的兴起以及线上数字音乐市场的逐渐成熟，为顺应新的产业环境和结构，繁星网应运而生。作为中国数字音乐领域的领跑者
         * telephone : 15123333333
         * is_stick : 1
         * collectnum : 2
         * time : 1517387186
         * showTime : 5个月前
         * is_new : false
         * imgList : [{"id":"1","img":"uploads/advert//img_15254166583099.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254166583099.jpg"},{"id":"2","img":"uploads/advert//img_15254167134447.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254167134447.jpg"}]
         * commentnum : 0.0
         * iscollect : true
         * showTelephone : 15123333333
         * showMonth : 1月
         * showDay : 31
         */

        private String id;
        private String title;
        private String content;
        private String telephone;
        private String is_stick;
        private String collectnum;
        private String time;
        private String showTime;
        private boolean is_new;
        private double commentnum;
        private boolean iscollect;
        private String showTelephone;
        private String showMonth;
        private String showDay;
        private List<ImgListBean> imgList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public String getShowTelephone() {
            return showTelephone;
        }

        public void setShowTelephone(String showTelephone) {
            this.showTelephone = showTelephone;
        }

        public String getShowMonth() {
            return showMonth;
        }

        public void setShowMonth(String showMonth) {
            this.showMonth = showMonth;
        }

        public String getShowDay() {
            return showDay;
        }

        public void setShowDay(String showDay) {
            this.showDay = showDay;
        }

        public List<ImgListBean> getImgList() {
            return imgList;
        }

        public void setImgList(List<ImgListBean> imgList) {
            this.imgList = imgList;
        }

    }
}
