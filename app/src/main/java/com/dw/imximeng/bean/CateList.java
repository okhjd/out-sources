package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\4 0004
 */
public class CateList {
    private List<CateItem> cateList;
    private PubInfo pubInfo;

    public static class CateItem{
        private int id;
        private String name;
        private String icon;
        private List<AttrList> attrList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public List<AttrList> getAttrList() {
            return attrList;
        }

        public void setAttrList(List<AttrList> attrList) {
            this.attrList = attrList;
        }
    }

    public static class OptionItem{
        private String key; //属性选值KEY
        private String val;//属性选址VAL

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public static class PubInfo{
        private String surplusNum; //当前剩余次数（免费模式且剩余次数为0时不可再发布，收费模式剩余次数为0时可付费发布，剩余次数大于0时可直接发布）
        private String costPrice;//广告信息发布收取费用（大于0表示收费模式，等于0表示免费模式）

        public String getSurplusNum() {
            return surplusNum;
        }

        public void setSurplusNum(String surplusNum) {
            this.surplusNum = surplusNum;
        }

        public String getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(String costPrice) {
            this.costPrice = costPrice;
        }
    }
    public static class AttrList{
        private int id;
        private String name;//属性名称
        private String type; //属性类型（文本：text，单选：singlesel，多选：multisel）
        private String code;//属性CODE
        private List<OptionItem> optionList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<OptionItem> getOptionList() {
            return optionList;
        }

        public void setOptionList(List<OptionItem> optionList) {
            this.optionList = optionList;
        }
    }

    public List<CateItem> getCateList() {
        return cateList;
    }

    public void setCateList(List<CateItem> cateList) {
        this.cateList = cateList;
    }

    public PubInfo getPubInfo() {
        return pubInfo;
    }

    public void setPubInfo(PubInfo pubInfo) {
        this.pubInfo = pubInfo;
    }
}
