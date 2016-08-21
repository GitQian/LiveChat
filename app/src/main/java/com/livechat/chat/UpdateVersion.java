package com.livechat.chat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.livechat.chat.utils.CommonUtil;
import com.ta.util.cache.TAExternalOverFroyoUtils;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.FileHttpResponseHandler;
import java.io.File;

/**
 * 升级版本---类
 * Created by win7 on 2015/11/7.
 */
public class UpdateVersion {

    private Context mContext;
    // 提示语
    private String updateMsg = "有最新的版本哦，亲，快下载吧~";
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    private CommonUtil comm=new CommonUtil();
    private ProgressBar mProgress;
    private boolean bforce = false;
    private boolean interceptFlag = false;
    private String saveFileName="";
    FileHttpResponseHandler fHandler;
    private static String downloadPath="SzMain.apk";//apk名
    private String mDownload_Path="";       //下载地址ip

    public UpdateVersion(Context context){
        this.mContext=context;
    }

    /**
     * 提示更新[非强制提示更新]
     *
     * @author Administrator 2015-4-25 下午3:33:42 <br>
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
        builder.setInverseBackgroundForced(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.upgrade_layout, null, false);
        builder.setView(view);
        TextView versionTips = (TextView) view.findViewById(R.id.version_tips);
        versionTips.setText(updateMsg);
        // 下载
        view.findViewById(R.id.upgrade_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (comm.isNetworkUsable(mContext)) {
                    bforce = false;
                    noticeDialog.dismiss();
                    showDownloadDialog();
                } else {
                    showVersionDialog("网络异常，请检查网络");
                }
            }
        });
        // 退出程序
        view.findViewById(R.id.upgrade_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }
    /**
     * 更新中的进度条显示
     *
     * @author Administrator 2015-4-25 下午3:34:29 <br>
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        builder.setView(v);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        // 取消
        Button cancleBtn = (Button) v.findViewById(R.id.cancel_version_change);

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                downloadDialog.dismiss();
                interceptFlag = true;
//                if (bforce) {
//                }
//                android.os.Process.killProcess(android.os.Process.myPid());
                // 停止
//		        fHandler.setInterrupt(true);
            }
        });
        downloadDialog = builder.create();
        downloadDialog.setCancelable(interceptFlag);
        // downloadDialog.setCanceledOnTouchOutside(false);
        noticeDialog.setCanceledOnTouchOutside(false);
        downloadDialog.show();


        // 下载
//        downloadApk();
    }
    /**
     * 下载文件有进度条
     */
    public void downloadApk(){
        saveFileName=TAExternalOverFroyoUtils
                .getDiskCacheDir(mContext, "Szmain.apk")
                .getAbsolutePath();
        File filess=new File(saveFileName);
        System.out.println("==================filess:" + filess);
        if (filess.exists()){
            filess.delete();
        }
        AsyncHttpClient syncHttpClient = new AsyncHttpClient();
        fHandler = new FileHttpResponseHandler(
                saveFileName) {

            @Override
            public void onProgress(long totalSize,long currentSize, long speed) {
                super.onProgress(totalSize, currentSize, speed);
                float foa=(float) ((float)currentSize / (float)totalSize);
                int num=(int) ((float) ((float)currentSize / (float)totalSize) * 100);
                mProgress.setProgress(num);
                System.out.println(totalSize+":totalSize=============currentSize:"+currentSize+"====speed:"+num+"====foa:"+foa);
            }

            @Override
            public void onFailure(Throwable error) {
                super.onFailure(error);
                downloadDialog.dismiss();
                showVersionDialog("下载失败！");
            }

            @Override
            public void onSuccess(byte[] binaryData) {
                super.onSuccess(binaryData);
                downloadDialog.dismiss();
                installApk();
            }
        };
        syncHttpClient.download(
                mDownload_Path + downloadPath,
                fHandler);
        // 停止
//		fHandler.setInterrupt(true);

    }

    /**
     * 安装apk
     *
     * @author Administrator 2015-4-25 下午3:34:58 <br>
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
    /**
     * 显示提示消息
     *
     * @author Administrator 2015-4-25 下午3:41:16 <br>
     * @param msg
     */
    private void showVersionDialog(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
