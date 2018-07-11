package com.dw.imximeng.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author hjd
 * @Created_Time 2018\7\9 0009
 */
public class Wxinfo {


    /**
     * status : 1
     * message : 提交成功
     * data : {"appid":"wx0179314396d8efb8","partnerid":"1507458791","prepayid":"wx1109484514266481e4b16d093948944918","package":"Sign=WXPay","noncestr":"owq7yh2q8l3eyhixbo5n9v16sxwgyvkr","timestamp":1531273725,"paysign":"59AFAEE75F4DDDD91D1DA349A4AEECBB"}
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
         * appid : wx0179314396d8efb8
         * partnerid : 1507458791
         * prepayid : wx1109484514266481e4b16d093948944918
         * package : Sign=WXPay
         * noncestr : owq7yh2q8l3eyhixbo5n9v16sxwgyvkr
         * timestamp : 1531273725
         * paysign : 59AFAEE75F4DDDD91D1DA349A4AEECBB
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private String timestamp;
        private String paysign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPaysign() {
            return paysign;
        }

        public void setPaysign(String paysign) {
            this.paysign = paysign;
        }
    }
}
