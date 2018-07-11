package com.dw.imximeng.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.dw.imximeng.app.UnCeHandler;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.bean.UserSiteInfo;
import com.dw.imximeng.bean.VersionInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
    static Context _context;
    static Resources _resource;
    public static boolean language = true;
    public static UserInfo userInfo = new UserInfo();
    public static UserSiteInfo userSiteInfo = new UserSiteInfo();
    public static VersionInfo versionInfo = new VersionInfo();

    //自定义的屏幕宽度，代表手机总得宽度为1440pt
    public final static float DESIGN_WIDTH = 1440;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        _context = getApplicationContext();
        _resource = _context.getResources();
        initUnCeHandler();
        //okhttp网络请求初始化
        initOkhttp();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        initSdk();
    }

    /**
     * 异常退出处理
     */
    private void initUnCeHandler(){
        UnCeHandler catchExcep = new UnCeHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    private void initOkhttp() {
        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//设置可访问所有的https网站
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .readTimeout(OkHttpUtils.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(OkHttpUtils.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(cookieJar1)
                //配置Https
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resetDensity();
    }

    public void resetDensity() {
        Point size = new Point();
        ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);

        getResources().getDisplayMetrics().xdpi = size.x / DESIGN_WIDTH * 72f;
    }

    private void initSdk() {
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wx0179314396d8efb8", "da18d94ce099a0c8b12135f313ef0c2d");
        PlatformConfig.setQQZone("1106928955", "flJU4erkx44F1zXX");
    }
}
