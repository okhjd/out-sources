package com.dw.imximeng.bean;

/**
 * @author hjd
 * @Created_Time 2018\6\27 0027
 */
public class UserSiteInfo {


    /**
     * status : 1
     * message : 获得成功
     * data : {"id":"1","icon":"http://test.dingwei.cn/xmcircle/static/img/no_img.png","name":"锡盟圈后台","website_url":"http://www.baidu.com/","cs_phone":"15123388888","cservice_qq":"12345678910","aboutus_url":"http://test.dingwei.cn/xmcircle/member/app/User/webInfo?field=cn_aboutus","agreement_url":"http://test.dingwei.cn/xmcircle/member/app/User/webInfo?field=cn_agreement","helpcenter_url":"http://test.dingwei.cn/xmcircle/member/app/User/webInfo?field=cn_helpcenter"}
     */

    private int status;
    private String message;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * icon : http://test.dingwei.cn/xmcircle/static/img/no_img.png
         * name : 锡盟圈后台
         * website_url : http://www.baidu.com/
         * cs_phone : 15123388888
         * cservice_qq : 12345678910
         * aboutus_url : http://test.dingwei.cn/xmcircle/member/app/User/webInfo?field=cn_aboutus
         * agreement_url : http://test.dingwei.cn/xmcircle/member/app/User/webInfo?field=cn_agreement
         * helpcenter_url : http://test.dingwei.cn/xmcircle/member/app/User/webInfo?field=cn_helpcenter
         * statement_url : "http://test.dingwei.cn/xmcircle/member/app/User/webInfo?field=cn_statement"
         */

        private String id;
        private String icon;
        private String name;
        private String website_url;
        private String cs_phone;
        private String cservice_qq;
        private String aboutus_url;
        private String agreement_url;
        private String helpcenter_url;
        private String statement_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWebsite_url() {
            return website_url;
        }

        public void setWebsite_url(String website_url) {
            this.website_url = website_url;
        }

        public String getCs_phone() {
            return cs_phone;
        }

        public void setCs_phone(String cs_phone) {
            this.cs_phone = cs_phone;
        }

        public String getCservice_qq() {
            return cservice_qq;
        }

        public void setCservice_qq(String cservice_qq) {
            this.cservice_qq = cservice_qq;
        }

        public String getAboutus_url() {
            return aboutus_url;
        }

        public void setAboutus_url(String aboutus_url) {
            this.aboutus_url = aboutus_url;
        }

        public String getAgreement_url() {
            return agreement_url;
        }

        public void setAgreement_url(String agreement_url) {
            this.agreement_url = agreement_url;
        }

        public String getHelpcenter_url() {
            return helpcenter_url;
        }

        public void setHelpcenter_url(String helpcenter_url) {
            this.helpcenter_url = helpcenter_url;
        }

        public String getStatement_url() {
            return statement_url;
        }

        public void setStatement_url(String statement_url) {
            this.statement_url = statement_url;
        }
    }
}
