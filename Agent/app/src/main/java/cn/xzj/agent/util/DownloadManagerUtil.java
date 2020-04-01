package cn.xzj.agent.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import cn.xzj.agent.MyApplication;
import cn.xzj.agent.R;
import cn.xzj.agent.core.common.CommonHandler;
import cn.xzj.agent.widget.CommonDialog;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/24
 * @ Des 下载apk
 */
public class DownloadManagerUtil {
    private DownloadManager mDownloadManager;
    private DownloadManager.Request request;
    private Context mContext;
    private long downloadId;
    private TimerTask task;
    private CommonDialog mCommonDialog;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private String  downloadUrl;
    private String apkName = "update.apk";
    private String apkFilePath;


    DownloadManagerUtil(Context cxt, String downloadUrl) {
        this.mContext = cxt;
        this.downloadUrl = downloadUrl;
        init();
    }

    @SuppressLint("HandlerLeak")
    private CommonHandler<DownloadManagerUtil> handler = new CommonHandler<DownloadManagerUtil>(this) {
        @SuppressLint("SetTextI18n")
        @Override
        public void executeMessage(Message msg) {
            Bundle bundle = msg.getData();
            int pro = bundle.getInt("pro");
            if (pro>=0) {
                mProgressBar.setProgress(pro);
                mTextView.setText(pro + " %");
            }

        }
    };

    private void init() {
        mCommonDialog = CommonDialog.newBuilder(mContext)
                .setCancelable(false)
                .setTitle("正在下载中")
                .setView(R.layout.dialog_download_apk)
                .create();
        mTextView = mCommonDialog.getView(R.id.tv_progress);
        mProgressBar = mCommonDialog.getView(R.id.pb_download_bar);


    }


    private void enqueue() {

        Timer timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                checkStatus();
            }
        };
        timer.schedule(task, 0, 1000);
        downloadId = mDownloadManager.enqueue(request);
        task.run();
        mCommonDialog.show();
    }

    public void download(String url, String name) {
        final String packageName = MyApplication.application.getPackageName();
        int state = mContext.getPackageManager().getApplicationEnabledSetting(packageName);
        //检测下载管理器是否被禁用
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("温馨提示").setMessage
                    ("系统下载管理器被禁止，需手动打开").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + packageName));
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        mContext.startActivity(intent);
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CommonUtils.browserAgentUpdate(mContext, downloadUrl);
                }
            });
            builder.create().show();
        } else {
            //正常下载流程
            apkName = name;
            apkFilePath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/"+apkName;
            File downloadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadFile.exists()) {
                downloadFile.mkdir();
            }
            //判断apk是否存在如果存在便删除
            String apkPath = downloadFile.getPath() + "/" + name;
            File apkfile = new File(apkPath);
            if (apkfile.exists()) {
                apkfile.delete();
            }

            request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedOverRoaming(false);

            //通知栏显示
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("小职姐服务版");
            request.setDescription("正在下载中...");
            request.setVisibleInDownloadsUi(true);
            //设置下载的路径
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);

            //获取DownloadManager
            mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            assert mDownloadManager != null;
            enqueue();
        }
    }

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
            String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

            LogLevel.w("bytes_downloaded", "" + bytes_downloaded);
            LogLevel.w("bytes_total", "" + bytes_total);

            int pro = bytes_downloaded * 100 / bytes_total;
            LogLevel.w("当前进度", "" + pro);
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putInt("pro", pro);
            msg.setData(bundle);
            handler.sendMessage(msg);
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            Log.w("---", "checkStatus: " + status);
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    mCommonDialog.dismiss();
                    task.cancel();
                    handler = null;
                    //下载完成
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    CommonUtils.browserAgentUpdate(mContext, downloadUrl);
                    mCommonDialog.dismiss();
                    task.cancel();
                    handler = null;
                    mDownloadManager.remove(downloadId);
                    break;
            }
        }
        cursor.close();
    }


    /**
     * 7.0兼容
     */
    private void installAPK() {
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String authority = mContext.getPackageName()+".fileProvider";
            Uri apkUri = FileProvider.getUriForFile(mContext, authority, apkFile);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
        ((Activity) mContext).finish();

    }


}
