package com.dw.imximeng.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class UserIndexData {

    private List<LbListBean> lbList;
    private List<NoticeListBean> noticeList;
    private NoticeListBean noticeInfo;

    public List<LbListBean> getLbList() {
        return lbList;
    }

    public void setLbList(List<LbListBean> lbList) {
        this.lbList = lbList;
    }

    public List<NoticeListBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeListBean> noticeList) {
        this.noticeList = noticeList;
    }

    public NoticeListBean getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(NoticeListBean noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public static class LbListBean {
        /**
         * id : 6
         * type : none
         * img : http://test.dingwei.cn/xmcircle/uploads/advert//img_15300896709417.jpg
         * url :
         * oid : 0
         * ard_schemeurl :
         * ios_schemeurl :
         * insideHtml :
         */

        private String id;
        private String type;
        private String img;
        private String url;
        private String oid;
        private String ard_schemeurl;
        private String ios_schemeurl;
        private String insideHtml;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getArd_schemeurl() {
            return ard_schemeurl;
        }

        public void setArd_schemeurl(String ard_schemeurl) {
            this.ard_schemeurl = ard_schemeurl;
        }

        public String getIos_schemeurl() {
            return ios_schemeurl;
        }

        public void setIos_schemeurl(String ios_schemeurl) {
            this.ios_schemeurl = ios_schemeurl;
        }

        public String getInsideHtml() {
            return insideHtml;
        }

        public void setInsideHtml(String insideHtml) {
            this.insideHtml = insideHtml;
        }
    }

    public static class NoticeListBean implements Parcelable {
        /**
         * id : 1
         * content : 二连浩特市
         * time : 1517387186
         * showTime : 2018-01-31 16:26:26
         */

        private String id;
        private String content;
        private String time;
        private String showTime;

        NoticeListBean(Parcel in) {
            id = in.readString();
            content = in.readString();
            time = in.readString();
            showTime = in.readString();
        }

        public static final Creator<NoticeListBean> CREATOR = new Creator<NoticeListBean>() {
            @Override
            public NoticeListBean createFromParcel(Parcel in) {
                return new NoticeListBean(in);
            }

            @Override
            public NoticeListBean[] newArray(int size) {
                return new NoticeListBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getShowTime() {
            return showTime;
        }

        public void setShowTime(String showTime) {
            this.showTime = showTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(content);
            dest.writeString(time);
            dest.writeString(showTime);
        }
    }
}
