package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @create-time 2018-07-05 15:30:10
 */
public class Information {

    /**
     * status : 1
     * message : 获得成功
     * data : {"areaInfo":{"id":"1","name":"二连浩特市","icon":"uploads/area//icon_15306843204062.png","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15306843204062.png"},"cateList":[{"id":"1","name":"房屋","iscomment":"0","icon":"http://test.dingwei.cn/xmcircle/uploads/service_cate//icon_15305820975516.png"}],"minfo":{"id":"11","phone":"15260871893","nickname":"asdfsdfsadf123123","status":"1","wxkey":"","qqkey":"","hportrait":"uploads/member//11_hp_15305116591068.jpg?r=78939","sex":"2","birthday":"2018-07-03","signature":"","numopen":"2","hpageopen":"2","integral":"100000","balance":"11110.00","area":"1","pushopen":"1","showHportrait":"http://test.dingwei.cn/xmcircle/uploads/member//11_hp_15305116591068.jpg?r=78939","showSex":"女","issign":false,"isunread":false,"ispaypwd":true},"list":[{"id":"1","uid":"1","hportrait":"http://test.dingwei.cn/xmcircle/uploads/member/img_15260218659090.png?r=517609","numopen":"1","title":"二手电脑出售，有意者联系","content":"酷狗繁星网，是酷狗于2012年倾力打造的在线视频互动演艺平台。针对目前中国音乐产业面临转型，演艺市场的兴起以及线上数字音乐市场的逐渐成熟，为顺应新的产业环境和结构，繁星网应运而生。作为中国数字音乐领域的领跑者","telephone":"15123333333","is_stick":"1","collectnum":"1","time":"1517387186","showName":"张三","showTime":"5个月前","is_new":false,"imgList":[{"id":"1","img":"uploads/advert//img_15254166583099.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254166583099.jpg"},{"id":"2","img":"uploads/advert//img_15254167134447.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254167134447.jpg"}],"commentnum":0,"iscollect":false,"showTelephone":"15123333333"}]}
     */

    /**
     * areaInfo : {"id":"1","name":"二连浩特市","icon":"uploads/area//icon_15306843204062.png","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15306843204062.png"}
     * cateList : [{"id":"1","name":"房屋","iscomment":"0","icon":"http://test.dingwei.cn/xmcircle/uploads/service_cate//icon_15305820975516.png"}]
     * minfo : {"id":"11","phone":"15260871893","nickname":"asdfsdfsadf123123","status":"1","wxkey":"","qqkey":"","hportrait":"uploads/member//11_hp_15305116591068.jpg?r=78939","sex":"2","birthday":"2018-07-03","signature":"","numopen":"2","hpageopen":"2","integral":"100000","balance":"11110.00","area":"1","pushopen":"1","showHportrait":"http://test.dingwei.cn/xmcircle/uploads/member//11_hp_15305116591068.jpg?r=78939","showSex":"女","issign":false,"isunread":false,"ispaypwd":true}
     * list : [{"id":"1","uid":"1","hportrait":"http://test.dingwei.cn/xmcircle/uploads/member/img_15260218659090.png?r=517609","numopen":"1","title":"二手电脑出售，有意者联系","content":"酷狗繁星网，是酷狗于2012年倾力打造的在线视频互动演艺平台。针对目前中国音乐产业面临转型，演艺市场的兴起以及线上数字音乐市场的逐渐成熟，为顺应新的产业环境和结构，繁星网应运而生。作为中国数字音乐领域的领跑者","telephone":"15123333333","is_stick":"1","collectnum":"1","time":"1517387186","showName":"张三","showTime":"5个月前","is_new":false,"imgList":[{"id":"1","img":"uploads/advert//img_15254166583099.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254166583099.jpg"},{"id":"2","img":"uploads/advert//img_15254167134447.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254167134447.jpg"}],"commentnum":0,"iscollect":false,"showTelephone":"15123333333"}]
     */

    private AreaInfoBean areaInfo;
    private MinfoBean minfo;
    private List<CateListBean> cateList;
    private List<ListBean> list;

    public AreaInfoBean getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(AreaInfoBean areaInfo) {
        this.areaInfo = areaInfo;
    }

    public MinfoBean getMinfo() {
        return minfo;
    }

    public void setMinfo(MinfoBean minfo) {
        this.minfo = minfo;
    }

    public List<CateListBean> getCateList() {
        return cateList;
    }

    public void setCateList(List<CateListBean> cateList) {
        this.cateList = cateList;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class AreaInfoBean {
        /**
         * id : 1
         * name : 二连浩特市
         * icon : uploads/area//icon_15306843204062.png
         * showIcon : http://test.dingwei.cn/xmcircle/uploads/area//icon_15306843204062.png
         */

        private String id;
        private String name;
        private String icon;
        private String showIcon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getShowIcon() {
            return showIcon;
        }

        public void setShowIcon(String showIcon) {
            this.showIcon = showIcon;
        }
    }

    public static class MinfoBean {
        /**
         * id : 11
         * phone : 15260871893
         * nickname : asdfsdfsadf123123
         * status : 1
         * wxkey :
         * qqkey :
         * hportrait : uploads/member//11_hp_15305116591068.jpg?r=78939
         * sex : 2
         * birthday : 2018-07-03
         * signature :
         * numopen : 2
         * hpageopen : 2
         * integral : 100000
         * balance : 11110.00
         * area : 1
         * pushopen : 1
         * showHportrait : http://test.dingwei.cn/xmcircle/uploads/member//11_hp_15305116591068.jpg?r=78939
         * showSex : 女
         * issign : false
         * isunread : false
         * ispaypwd : true
         */

        private String id;
        private String phone;
        private String nickname;
        private String status;
        private String wxkey;
        private String qqkey;
        private String hportrait;
        private String sex;
        private String birthday;
        private String signature;
        private String numopen;
        private String hpageopen;
        private String integral;
        private String balance;
        private String area;
        private String pushopen;
        private String showHportrait;
        private String showSex;
        private boolean issign;
        private boolean isunread;
        private boolean ispaypwd;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWxkey() {
            return wxkey;
        }

        public void setWxkey(String wxkey) {
            this.wxkey = wxkey;
        }

        public String getQqkey() {
            return qqkey;
        }

        public void setQqkey(String qqkey) {
            this.qqkey = qqkey;
        }

        public String getHportrait() {
            return hportrait;
        }

        public void setHportrait(String hportrait) {
            this.hportrait = hportrait;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getPushopen() {
            return pushopen;
        }

        public void setPushopen(String pushopen) {
            this.pushopen = pushopen;
        }

        public String getShowHportrait() {
            return showHportrait;
        }

        public void setShowHportrait(String showHportrait) {
            this.showHportrait = showHportrait;
        }

        public String getShowSex() {
            return showSex;
        }

        public void setShowSex(String showSex) {
            this.showSex = showSex;
        }

        public boolean isIssign() {
            return issign;
        }

        public void setIssign(boolean issign) {
            this.issign = issign;
        }

        public boolean isIsunread() {
            return isunread;
        }

        public void setIsunread(boolean isunread) {
            this.isunread = isunread;
        }

        public boolean isIspaypwd() {
            return ispaypwd;
        }

        public void setIspaypwd(boolean ispaypwd) {
            this.ispaypwd = ispaypwd;
        }
    }

    public static class CateListBean {
        /**
         * id : 1
         * name : 房屋
         * iscomment : 0
         * icon : http://test.dingwei.cn/xmcircle/uploads/service_cate//icon_15305820975516.png
         */

        private String id;
        private String name;
        private String iscomment;
        private String icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIscomment() {
            return iscomment;
        }

        public void setIscomment(String iscomment) {
            this.iscomment = iscomment;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class ListBean {
        /**
         * id : 1
         * uid : 1
         * hportrait : http://test.dingwei.cn/xmcircle/uploads/member/img_15260218659090.png?r=517609
         * numopen : 1
         * title : 二手电脑出售，有意者联系
         * content : 酷狗繁星网，是酷狗于2012年倾力打造的在线视频互动演艺平台。针对目前中国音乐产业面临转型，演艺市场的兴起以及线上数字音乐市场的逐渐成熟，为顺应新的产业环境和结构，繁星网应运而生。作为中国数字音乐领域的领跑者
         * telephone : 15123333333
         * is_stick : 1
         * collectnum : 1
         * time : 1517387186
         * showName : 张三
         * showTime : 5个月前
         * is_new : false
         * imgList : [{"id":"1","img":"uploads/advert//img_15254166583099.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254166583099.jpg"},{"id":"2","img":"uploads/advert//img_15254167134447.jpg","showImg":"http://test.dingwei.cn/xmcircle/uploads/advert//img_15254167134447.jpg"}]
         * commentnum : 0
         * iscollect : false
         * showTelephone : 15123333333
         */

        private String id;
        private String uid;
        private String hportrait;
        private String numopen;
        private String title;
        private String content;
        private String telephone;
        private String is_stick;
        private String collectnum;
        private String time;
        private String showName;
        private String showTime;
        private boolean is_new;
        private int commentnum;
        private boolean iscollect;
        private String showTelephone;
        private List<ImgListBean> imgList;

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

    public static class ImgListBean {
        /**
         * id : 1
         * img : uploads/advert//img_15254166583099.jpg
         * showImg : http://test.dingwei.cn/xmcircle/uploads/advert//img_15254166583099.jpg
         */

        private String id;
        private String img;
        private String showImg;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getShowImg() {
            return showImg;
        }

        public void setShowImg(String showImg) {
            this.showImg = showImg;
        }
    }
}
