package com.dw.imximeng.bean;

/**
 * @author hjd
 * @Created_Time 2018\6\26 0026
 */
public class UserInfo{


    /**
     * status : 1
     * message : 登录成功
     * data : {"id":"5","phone":"15260871893","nickname":"","status":"1","wxkey":"","qqkey":"","hportrait":"","sex":"0","birthday":"0000-00-00","signature":"","numopen":"1","hpageopen":"1","integral":"0","balance":"0.00","area":"0","pushopen":"1","showHportrait":"http://test.dingwei.cn/xmcircle/static/img/portrait_f.png","showSex":"未设置","issign":false,"isunread":false,"sessionid":"4dd3d1e6bde76b5d6c4449ad81fd52b0b3698998"}
     */

//    private int status;
//    private String message;
//    private DataBean data;
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
        /**
         * id : 5
         * phone : 15260871893
         * nickname :
         * status : 1
         * wxkey :
         * qqkey :
         * hportrait :
         * sex : 0
         * birthday : 0000-00-00
         * signature :
         * numopen : 1
         * hpageopen : 1
         * integral : 0
         * balance : 0.00
         * area : 0
         * pushopen : 1
         * showHportrait : http://test.dingwei.cn/xmcircle/static/img/portrait_f.png
         * showSex : 未设置
         * issign : false
         * isunread : false
         * sessionid : 4dd3d1e6bde76b5d6c4449ad81fd52b0b3698998
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
        private String sessionid;
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

        public int getSex() {
            return Integer.parseInt(sex);
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            if (birthday.equals("0000-00-00")){
                return "";
            }
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

        public String getSessionid() {
            return sessionid;
        }

        public void setSessionid(String sessionid) {
            this.sessionid = sessionid;
        }

    public boolean isIspaypwd() {
        return ispaypwd;
    }

    public void setIspaypwd(boolean ispaypwd) {
        this.ispaypwd = ispaypwd;
    }
    //    }
}
