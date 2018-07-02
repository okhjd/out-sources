package com.dw.imximeng.bean;

/**
 * @author hjd
 * @Created_Time 2018\6\30 0030
 */
public class UserMessage {
//    'id' => '2',        //消息ID
//            'issee' => '0',    //阅读状态（1：已读，0：未读）
//            'title' => '所有用户消息标题', //消息标题
//            'brief' => '消息简介',    //消息简介
//            'showTime' => '2018-05-10 15:30',    //发布时间
//            'weburl' => 'http://localhost/xmcircle/member/app/User/msgWeb?id=3&uid=1',    //详细wap地址

    private int id;
    private int issee;
    private String title;
    private String brief;
    private String showTime;
    private String weburl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIssee() {
        return issee;
    }

    public void setIssee(int issee) {
        this.issee = issee;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }
}
