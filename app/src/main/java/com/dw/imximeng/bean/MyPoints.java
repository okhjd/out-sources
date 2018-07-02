package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\1 0001
 */
public class MyPoints {
    private String integral;//积分余额
    private List<PointsItem> list;

    public static class PointsItem {
        private int id;//明细ID
        private int type;//明细类型（1：增加，2：减少）
        private String typeName;//明细类型名称
        private int cate; //明细类别（1：签到，2：平台调整）
        private String cateName;//明细类别名称
        private String number;//明细积分
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

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getCate() {
            return cate;
        }

        public void setCate(int cate) {
            this.cate = cate;
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

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public List<PointsItem> getList() {
        return list;
    }

    public void setList(List<PointsItem> list) {
        this.list = list;
    }
}
