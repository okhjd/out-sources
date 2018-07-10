package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @create-time 2018-07-10 10:17:53
 */
public class Payment {

    /**
     * balance : 11109.99
     * ptypeList : [{"code":"wxpay","name":"微信"},{"code":"alipay","name":"支付宝"}]
     */

    private String balance;
    private List<PtypeListBean> ptypeList;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<PtypeListBean> getPtypeList() {
        return ptypeList;
    }

    public void setPtypeList(List<PtypeListBean> ptypeList) {
        this.ptypeList = ptypeList;
    }

    public static class PtypeListBean {
        /**
         * code : wxpay
         * name : 微信
         */

        private String code;
        private String name;
        private boolean check;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
