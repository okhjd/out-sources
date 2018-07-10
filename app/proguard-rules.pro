# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
-optimizationpasses 5
#指定不去忽略非公共的库类
-dontusemixedcaseclassnames
#指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
#这样再次崩溃的时候就有源文件和行号的信息了
-keepattributes SourceFile,LineNumberTable
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#忽略警告
-ignorewarning
##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######
#####混淆保护自己项目的部分代码以及引用的第三方jar包library#######
#-libraryjars libs/umeng-analytics-v5.2.4.jar
#三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
#-libraryjars libs/sdk-v1.0.0.jar
#-libraryjars libs/look-v1.0.1.jar
#如果不想混淆 keep 掉
-keep class com.lippi.recorder.iirfilterdesigner.** {*; }
#友盟
-keep class com.umeng.**{*;}
#项目特殊处理代码
#忽略警告
-dontwarn com.lippi.recorder.utils**
#保留一个完整的包
-keep class com.lippi.recorder.utils.** {
        *;
}
-keep class  com.lippi.recorder.utils.AudioRecorder{*;}
#如果引用了v4或者v7包
-dontwarn android.support.**
####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####
-keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
 }
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
        native <methods>;
 }
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
 }
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
 }
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    #保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
    #-keepclassmembers enum * {
    #  public static **[] values();
    #  public static ** valueOf(java.lang.String);
    #}

-keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
}
#不混淆资源类
-keepclassmembers class **.R$* {
        public static <fields>;
}
#避免混淆泛型 如果混淆报错建议关掉–keepattributes Signature
-keepattributes Signature,LineNumberTable
-renamesourcefileattribute SourceFile
-dontwarn com.readystatesoftware.systembartint.**
-keep class com.readystatesoftware.systembartint.** { *; }
#Umeng sdk混淆配置
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class com.umeng.**
-keep public class com.dw.imximeng.R$*{
    public static final int *;
}
-keep public class com.umeng.fb.ui.ThreadView {
}
-dontwarn com.umeng.**
-dontwarn org.apache.commons.**
-keep public class * extends com.umeng.**

-keep class com.umeng.** {*; }
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#Gson混淆配置
-keepattributes *Annotation*
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.stream.** { *; }
-keep class com.chjif.shzs.model.entity.** { *; }
-dontwarn com.google.gson.**
-keep class com.google.gson.** {*;}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#-libraryjars libs/BaiduLBS_Android.jar
-dontwarn com.baidu.**
-keep class vi.com.** {*;}
-keep class com.baidu.** { *; }

-dontwarn vi.com.gdi.bgl.android.**
-keep class vi.com.gdi.bgl.android.**{*;}

-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-keep class okio.** { *; }
-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.** { *; }
-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.** { *; }
-dontwarn junit.**
-keep class junit.** { *; }
-dontwarn org.junit.**
-keep class org.junit.** { *; }

-dontwarn javax.crypto.**
-keep class javax.crypto.** { *; }
-dontwarn org.bouncycastle.jce.provider.**
-keep class org.bouncycastle.jce.provider.** { *; }

-dontwarn com.dw.imximeng.bean.**
-keep class com.dw.imximeng.bean.** { *; }

#蒲公英
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }

-dontoptimize
-dontpreverify

#极光
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#不混淆base里的基类
-dontwarn com.necer.ncalendar.**
-keep class com.dw.imximeng.base.**{*;}

# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**

# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

 -keep public class * extends java.lang.annotation.Annotation {
   *;
 }


-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}


#微信
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

#微信新版本需要再填写下面2个
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}