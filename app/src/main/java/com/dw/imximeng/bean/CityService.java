package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\8 0008
 */
public class CityService {

    private List<CateListBean> cateList;
    private List<ListBean> list;

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

    public static class CateListBean {
        /**
         * id : 1
         * name : 房屋
         */

        private String id;
        private String name;

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
    }

    public static class ListBean {
        /**
         * id : 4
         * icon : uploads/service//icon_15305854103819.png
         * phone : 15959224262
         * name : 家政
         * label : 保姆,打扫
         * labelArray : ["保姆","打扫"]
         * showIcon : http://test.dingwei.cn/xmcircle/uploads/service//icon_15305854103819.png
         */

        private String id;
        private String icon;
        private String phone;
        private String name;
        private String label;
        private String showIcon;
        private List<String> labelArray;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getShowIcon() {
            return showIcon;
        }

        public void setShowIcon(String showIcon) {
            this.showIcon = showIcon;
        }

        public List<String> getLabelArray() {
            return labelArray;
        }

        public void setLabelArray(List<String> labelArray) {
            this.labelArray = labelArray;
        }
    }
}
