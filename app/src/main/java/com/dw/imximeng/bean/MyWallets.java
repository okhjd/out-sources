package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\3 0003
 */
public class MyWallets {
    private String balance;//  账号余额
    private List<Wallet> list;//余额明细

    public static class Wallet{
        private int id;//明细ID
        private int type;//明细类型（1：增加，2：减少）
        private String cateName;//类别名称
        private String number;//明细金额
        private String showTime;//明细时间

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getShowTime() {
            return showTime;
        }

        public void setShowTime(String showTime) {
            this.showTime = showTime;
        }
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<Wallet> getList() {
        return list;
    }

    public void setList(List<Wallet> list) {
        this.list = list;
    }
}
