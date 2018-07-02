package com.dw.imximeng.helper;

import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;

/**
 * @author hjd
 * @Created_Time 2018\6\24 0024
 */

public class LanguageHelper {
    private String home;
    private String region;
    private String advertisement;
    private String myself;

    private String accountNumber;
    private String password;
    private String signIn;
    private String register;
    private String forget;
    private String otherSignIn;


    public LanguageHelper(BaseActivity activity, boolean language) {
        new SharedPreferencesHelper(activity).setSwitchLanguage(language);
        BaseApplication.language = language;

        home = BaseApplication.language ? "首页" : "home";
        region = BaseApplication.language ? "地区" : "region";
        advertisement = BaseApplication.language ? "广告" : "advertisement";
        myself = BaseApplication.language ? "我" : "myself";

        accountNumber = BaseApplication.language ? "手机号" : "accountnumber";
        password = BaseApplication.language ? "密码" : "password";
        signIn = BaseApplication.language ? "登录" : "Sign in";
        register = BaseApplication.language ? "新注册用户" : "Newly registered user";
        forget = BaseApplication.language ? "忘记密码?" : "Forget the password?";
        otherSignIn = BaseApplication.language ? "其他登录方式" : "Other login methods";
    }

    public String getHome() {
        return home;
    }

    public String getRegion() {
        return region;
    }

    public String getAdvertisement() {
        return advertisement;
    }

    public String getMyself() {
        return myself;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getSignIn() {
        return signIn;
    }

    public String getRegister() {
        return register;
    }

    public String getForget() {
        return forget;
    }

    public String getOtherSignIn() {
        return otherSignIn;
    }
}
