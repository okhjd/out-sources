package com.dw.imximeng.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dw.imximeng.BuildConfig;
import com.dw.imximeng.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author hjd
 * @time 2017-11-27 15:48
 */
public class UpdateManger {
    // 应用程序Context
    private Context mContext;
    // 下载安装包的网络路径
    //http://222.82.250.218:5012/module-portalweb/JZHC_Mamsf.apk
    private static final String fileName = "imximeng.apk";
    private String apkUrl = "";
    private Dialog downloadDialog;// 下载对话框
    private static final String savePath = "/sdcard/updatedemo/";// 保存apk的文件夹
    private static final String saveFileName = savePath + fileName;

    // 进度条与通知UI刷新的handler和msg常量
    private ProgressBar mProgress;
    private TextView tv_message;
    private TextView tv_message2;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_ERROR = 3;
    private int progress;// 当前进度
    private Thread downLoadThread; // 下载线程
    private boolean interceptFlag = false;// 用户取消下载
    int currentSize = 0;
    int updateTotalSize = 0;
    // 通知处理刷新界面的handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    tv_message.setText(progress+"%");
                    tv_message2.setText(progress+"/100");
                    break;
                case DOWN_OVER:
                    downloadDialog.dismiss();
                    installApk();
                    break;
                case DOWN_ERROR:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
                    downloadDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public UpdateManger(Context context, String apkUrl) {
        this.mContext = context;
        this.apkUrl = apkUrl;
    }

    // 显示更新程序对话框，供主程序调用
    public void checkUpdateInfo() {
        //showNoticeDialog();
        showDownloadDialog();
    }

    protected void showDownloadDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        //builder.setTitle("软件版本更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_app_progress, null);
        mProgress = v.findViewById(R.id.progress);
        tv_message = v.findViewById(R.id.tv_message);
        tv_message2 = v.findViewById(R.id.tv_message2);
        builder.setView(v);// 设置对话框的内容为一个View
        builder.setCancelable(false);

        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    protected void installApk() {
        File apkfile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),fileName);
        if (!apkfile.exists()) {
            return;
        }
        long size = apkfile.length();
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");// File.toString()会返回路径信息
//        mContext.startActivity(i);

        Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", apkfile); //修改  downloadFile 来源于上面下载文件时保存下来的
        //  BuildConfig.APPLICATION_ID + ".fileprovider" 是在manifest中 Provider里的authorities属性定义的值
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //临时授权
        installIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(installIntent);
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            URL url;
            try {
                url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.connect();
                if (conn.getResponseCode() == 302 ){
                    url = new URL(conn.getHeaderField("Location"));
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.connect();
                }
                if (conn.getResponseCode() == 200) {
                    int length = conn.getContentLength();
                    updateTotalSize = length / 1024;
                    InputStream ins = conn.getInputStream();
//                File file = new File(savePath);
                    File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                    if (!file.exists()) {
                        file.mkdir();
                    }
//                String apkFile = saveFileName;
                    String apkFile = file.getPath() + "/" + fileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream outStream = new FileOutputStream(ApkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do {
                        int numread = ins.read(buf);
                        count += numread;
                        progress = (int) Math.ceil(((float) count / length) * 100);
                        currentSize = count / 1024;
                        // 下载进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            // 下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                        outStream.write(buf, 0, numread);
                    }
                    while (!interceptFlag);// 点击取消停止下载
                    outStream.close();
                    ins.close();
                }else {
                    mHandler.sendEmptyMessage(DOWN_ERROR);
                }
            } catch (Exception e) {
                mHandler.sendEmptyMessage(DOWN_ERROR);
                e.printStackTrace();
            }
        }
    };
}

