package com.dw.imximeng.helper;

import com.dw.imximeng.BuildConfig;

/**
 * @author hjd
 * @Created_Time 2018\6\26 0026
 */
public interface MethodHelper {
    String TYPE_REGISTER = "1";
    String TYPE_FORGET = "2";
    String TYPE_BIND = "3";
    String TYPE_PAY = "4";

    String PRIVACY_PHONE_NUM = "num";//号码公开
    String PRIVACY_HOME_PAGE = "hpage";//主页公开
    String PRIVACY_PUSH = "push";//推送通知

    //登录
    String USER_LOGIN = BuildConfig.API_ENV + "/member/app/user/login";
    //注册
    String USER_REGISTER = BuildConfig.API_ENV + "/member/app/user/register";
    //短信验证码
    String USER_ID_CODE = BuildConfig.API_ENV + "/member/app/user/idcode";
    //图形验证码
    String GET_IMAGE_CODE = BuildConfig.API_ENV + "/member/app/user/imgcode";
    //忘记密码
    String FORGET_PASSWORD = BuildConfig.API_ENV + "/member/app/user/forgetPwd";
    //系统信息
    String GET_USER_SITE_INFO = BuildConfig.API_ENV + "/member/app/user/siteInfo";
    //地区列表
    String GET_REGION_LIST = BuildConfig.API_ENV + "/member/app/user/areaList";
    //主页信息
    String USER_INDEX_DATA = BuildConfig.API_ENV + "/member/app/user/indexData";
    //公告列表
    String USER_NOTICE_LIST = BuildConfig.API_ENV + "/member/app/user/noticeList";
    //工具列表
    String TOOLS_LIST = BuildConfig.API_ENV + "/member/app/information/toolList";
    //热门搜索
    String HOT_SEARCH = BuildConfig.API_ENV + "/member/app/information/hotsearch";
    //收藏广告信息列表
    String MY_COLLECTION = BuildConfig.API_ENV + "/member/app/user/collectList";
    //会员消息
    String MESSAGE_LIST = BuildConfig.API_ENV + "/member/app/user/msgList";
    //保存资料
    String SAVE_DATA = BuildConfig.API_ENV + "/member/app/user/saveData";
    //积分明细
    String GET_POINTS_RECORD = BuildConfig.API_ENV + "/member/app/user/integralList";
    //会员中心
    String GET_USER_CENTER = BuildConfig.API_ENV + "/member/app/user/userCenter";
    //隐私设置
    String PRIVACY_SET = BuildConfig.API_ENV + "/member/app/user/privacyset";
    //意见反馈
    String FEEDBACK = BuildConfig.API_ENV + "/member/app/user/feedback";
    //我的钱包
    String USER_WALLET = BuildConfig.API_ENV + "/member/app/user/balanceInfo";
    //余额明细
    String WALLET_RECORD = BuildConfig.API_ENV + "/member/app/user/balanceList";
    //我的银行卡
    String BANK_CARD_LIST = BuildConfig.API_ENV + "/member/app/user/bankList";
    //支付密码设置
    String SET_PAY_PASSWORD = BuildConfig.API_ENV + "/member/app/user/paypwdset";
    //登录密码设置
    String MODIFY_PASSWORD = BuildConfig.API_ENV + "/member/app/user/passwordset";
    //身份验证（支付密码校验）
    String USER_IDVERIFICATION = BuildConfig.API_ENV + "/member/app/user/idverification";
    //添加银行卡
    String ADD_BANK_CARD = BuildConfig.API_ENV + "/member/app/user/bankAdd";
    //银行列表
    String BANK_LIST = BuildConfig.API_ENV + "/member/app/user/bcodeList";
    //分享信息
    String SHARE_INFO = BuildConfig.API_ENV + "/member/app/user/shareInfo";
}
