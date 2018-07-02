package com.dw.imximeng.bean;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class RegionList {

    /**
     * status : 1
     * message : 获得成功
     * data : [{"id":"1","name":"二连浩特市","icon":"uploads/area//icon_15278435219374.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15278435219374.jpg","isdefault":false},{"id":"2","name":"锡林浩特市","icon":"uploads/area//icon_15278435219374.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15278435219374.jpg","isdefault":false},{"id":"3","name":"阿巴嘎旗","icon":"uploads/area//icon_15278435219374.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15278435219374.jpg","isdefault":false},{"id":"4","name":"苏尼特左旗","icon":"uploads/area//icon_15278435219374.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15278435219374.jpg","isdefault":false},{"id":"6","name":"苏尼特右旗","icon":"uploads/area//icon_15289393844060.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289393844060.jpg","isdefault":false},{"id":"7","name":"东乌珠穆沁旗","icon":"uploads/area//icon_15289394031129.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289394031129.jpg","isdefault":false},{"id":"8","name":"西乌珠穆沁旗","icon":"uploads/area//icon_15289394181668.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289394181668.jpg","isdefault":false},{"id":"9","name":"太仆寺旗","icon":"uploads/area//icon_15289394409159.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289394409159.jpg","isdefault":false},{"id":"10","name":"镶黄旗","icon":"uploads/area//icon_15289394589071.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289394589071.jpg","isdefault":false},{"id":"11","name":"正镶白旗","icon":"uploads/area//icon_15289394752171.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289394752171.jpg","isdefault":false},{"id":"12","name":"正蓝旗","icon":"uploads/area//icon_15289394957304.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289394957304.jpg","isdefault":false},{"id":"13","name":"多伦县","icon":"uploads/area//icon_15289395121337.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289395121337.jpg","isdefault":false},{"id":"14","name":"乌拉盖开发区","icon":"uploads/area//icon_15289395287413.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289395287413.jpg","isdefault":false},{"id":"15","name":"重庆","icon":"uploads/area//icon_15289398643923.jpg","showIcon":"http://test.dingwei.cn/xmcircle/uploads/area//icon_15289398643923.jpg","isdefault":false}]
     */

    private int status;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 二连浩特市
         * icon : uploads/area//icon_15278435219374.jpg
         * showIcon : http://test.dingwei.cn/xmcircle/uploads/area//icon_15278435219374.jpg
         * isdefault : false
         */

        private String id;
        private String name;
        private String icon;
        private String showIcon;
        private boolean isdefault;

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

        public boolean isIsdefault() {
            return isdefault;
        }

        public void setIsdefault(boolean isdefault) {
            this.isdefault = isdefault;
        }
    }
}
