package cn.xzj.agent.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.channey.utils.DeviceUtils;
import com.channey.utils.SharedPreferencesUtils;
import com.channey.utils.StringUtils;
import com.leon.channel.helper.ChannelReaderUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xzj.agent.R;
import cn.xzj.agent.constants.Config;
import cn.xzj.agent.constants.Constants;
import cn.xzj.agent.entity.agentinfo.AgentInfo;
import cn.xzj.agent.net.Client;
import cn.xzj.agent.net.Observer2;
import cn.xzj.agent.widget.CommonDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.megvii.livenesslib.util.ConUtil.getDeviceID;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/24
 * @ Des
 */
public class CommonUtils {
    /**
     * 浏览器代理 下载安装
     */
    public static void browserAgentUpdate(Context mContext, String appUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri content_url = Uri
                .parse(appUrl);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param number01 用于显示在弹框中（脱敏）
     * @param number02 真实号码（未脱敏）
     */
    public static void makePhoneCall(final Activity context, String number01, final String number02) {

        if (PermissionsUtil.allowPermissions(context, Manifest.permission.CALL_PHONE)) {
            CommonDialog.newBuilder(context)
                    .setMessage("确认拨打电话 " + number01 + " ?")
                    .setPositiveButton(context.getResources().getString(R.string.confirm), new CommonDialog.OnClickListener() {
                        @Override
                        public void onClick(CommonDialog dialog) {
                            dialog.cancel();
                            callPhone(context, number02);
                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.cancel), null)
                    .create()
                    .show();
        } else {
            new RxPermissions(context)
                    .request(Manifest.permission.CALL_PHONE)
                    .subscribe(new Observer2<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                callPhone(context, number02);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public static void callPhone(Context cxt, String phone) {
        Intent mIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cxt.startActivity(mIntent);
    }

    /**
     * 直接拨打电话
     *
     * @param cxt
     * @param phone
     */
    public static void directCallPhone(Context cxt, String phone) {
        Intent mIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        cxt.startActivity(mIntent);
    }

    /**
     * 文件转base64字符串
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }

    /**
     * 公共统计接口（埋点接口）
     *
     * @param context 上下文对象
     * @param type    类型 EnumValue.STATISTICS_TYPE_BTN 、EnumValue.STATISTICS_TYPE_PAGE
     * @param metric  EnumValue.STATISTICS_METRIC_VIEW 、EnumValue.STATISTICS_METRIC_CLICK
     */
    public static void statistics(Context context, String eventId, String type, String metric) {
        Date mDate = new Date();
        String userId = null;
        AgentInfo agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(context);
        if (agentInfo != null) {
            userId = agentInfo.getAgentId();
        }
        final Disposable[] mDisposable = new Disposable[1];
        Client.getInstance(context)
                .getApiStatisticsManager()
                .statistics(Constants.STATISTICS_DOMAIN,
                        Constants.STATISTICS_CHANNEL,
                        Constants.STATISTICS_SOURCE,
                        userId,
                        SharedPreferencesUtil.getDeviceInfo(context).getId(),
                        mDate.getTime(),
                        eventId,
                        type,
                        metric)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer2<Object>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mDisposable[0].dispose();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable[0] = d;
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    public static String buildUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeyBoard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && activity.getCurrentFocus() != null) {
                if (inputMethodManager.isActive() && activity.getCurrentFocus().getWindowToken() != null) {

                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRootPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        return path;
    }

    /**
     * 获取小职姐根文件夹
     */
    public static File getAgentRootFile() {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File mFile = new File(rootPath + "/" + Config.ROOT_FILE_NAME);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        return mFile;
    }

    /**
     * 获取小职姐 图片根文件夹
     */
    public static File getAgentImageRootFile() {
        File mAgentRootFile = getAgentRootFile();
        File mFile = new File(mAgentRootFile.getPath() + "/" + Config.FILE_IMAGE_ROOT_NAME);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        return mFile;
    }

    /**
     * 获取小职姐 下载根文件夹
     */
    public static File getAgentDownloadRootFile() {
        File mAgentRootFile = getAgentRootFile();
        File mFile = new File(mAgentRootFile.getPath() + "/" + Config.FILE_DOWNLOAD_ROOT_NAME);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        return mFile;
    }

    /**
     * 将图片保存到相册文件夹中去
     */
    public static String getDICMAgentPath() {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File mFile = new File(rootPath + "/" + Config.DCIM + "/" + Config.ROOT_FILE_NAME);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        return mFile.getPath();
    }

    /**
     * 获取相册位置
     */
    public static String getDICMCameraPath() {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return rootPath + "/" + Config.DCIM + "/" + Config.CAMERA;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }

    }

    /**
     * 这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
     */
    public static void sendBroadcastFileChange(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        context.sendBroadcast(intent);
    }

    /**
     *  //截取数字  【读取字符串中第一个连续的字符串，不包含后面不连续的数字】
     * @param content 字符串
     * @return 返回数字
     */

    public static String getNumbers(String content) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return m.replaceAll("").trim();
    }

    /**
     * 设置tabLayoutIndicator 和 textView等宽
     * @param tabLayout 控件
     * @param padding itemView 距离left and right 的 padding
     */
    public static void setTabWidth(final TabLayout tabLayout, final int padding){
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距 注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = padding;
                        params.rightMargin = padding;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static String getUUIDString(Context mContext) {
        String KEY_UUID = "key_uuid";
        String uuid = SharedPreferencesUtils.INSTANCE.getString(mContext, KEY_UUID);
        if (StringUtils.INSTANCE.isNotEmpty(uuid))
            return uuid;

        uuid = DeviceUtils.INSTANCE.getMacAddress();
        if (TextUtils.isEmpty(uuid)) {
            uuid = getDeviceID(mContext);
            if (TextUtils.isEmpty(uuid)) {
                uuid = UUID.randomUUID().toString();
                uuid = Base64.encodeToString(uuid.getBytes(), Base64.DEFAULT);
            }
        }
        SharedPreferencesUtils.INSTANCE.saveString(mContext, KEY_UUID, uuid);
        return uuid;
    }

    /**
     * 删除文件
     * @param filePath 文件绝对路径
     */
    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = "";
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public static String byteArrayToBase64(byte[] array) {
        String result = Base64.encodeToString(array, Base64.DEFAULT);
        return result;
    }

    public static String strToBase64(String string) {
        String result = Base64.encodeToString(string.getBytes(), Base64.DEFAULT);
        return result;
    }
    public static String getChannel(Context context){
        String channel = ChannelReaderUtil.getChannel(context.getApplicationContext());
        return StringUtils.INSTANCE.isEmpty(channel) ? "xzjagent" : channel;
    }

    public static String getMetaData(Context context,String key) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }
}
