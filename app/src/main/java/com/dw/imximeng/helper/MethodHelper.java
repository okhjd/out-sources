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
    //广告信息列表
    String INFORMATION_LIST = BuildConfig.API_ENV + "/member/app/information/informationList";
    //地区单个信息
    String USER_AREA_INFO = BuildConfig.API_ENV + "/member/app/user/areaInfo";
    //设置默认地区
    String SET_DEFAULT_AREA = BuildConfig.API_ENV + "/member/app/user/setUserArea";
    //发布分类列表
    String INFORMATION_CATE_LIST = BuildConfig.API_ENV + "/member/app/information/cateList";
    //用户签到
    String USER_SIGN_IN = BuildConfig.API_ENV + "/member/app/user/signIn";
    //发布广告信息
    String PUSH_ADVERTISMENT_INFO = BuildConfig.API_ENV + "/member/app/information/publishInfo";
    //信息详情
    String INFO_DETETAILS = BuildConfig.API_ENV + "/member/app/information/informationInfo";
    //广告信息评论列表
    String COMMENT_LIST = BuildConfig.API_ENV + "/member/app/information/commentList";
    //评论广告信息
    String PUSH_COMMENT_INFO = BuildConfig.API_ENV + "/member/app/information/toComment";
    //操作收藏广告信息
    String COLLECT_INFO = BuildConfig.API_ENV + "/member/app/user/opcollect";
    //举报广告信息
    String TO_REPORT = BuildConfig.API_ENV + "/member/app/information/toReport";
    //用户主页
    String USER_HOME = BuildConfig.API_ENV + "/member/app/information/userHomepage";
    //同城服务
    String SERVICE_LIST = BuildConfig.API_ENV + "/member/app/information/serviceList";
    //同城服务标题检索
    String SERVICE_TITLE = BuildConfig.API_ENV + "/member/app/information/serviceTitle";
    //标题检索
    String SEARCH_TITLE = BuildConfig.API_ENV + "/member/app/information/titleRetrieval";
    //我的发布
    String MY_RELEASE = BuildConfig.API_ENV + "/member/app/user/informationList";
    //删除我发布的广告信息
    String DELETE_RELEASE = BuildConfig.API_ENV + "/member/app/user/informationDelete";
    //三方账号登录
    String KEY_LOGIN = BuildConfig.API_ENV + "/member/app/user/keyLogin";
    //三方账号绑定
    String BIND_KEY_ACCOUNT = BuildConfig.API_ENV + "/member/app/user/keyAccountBind";
}
