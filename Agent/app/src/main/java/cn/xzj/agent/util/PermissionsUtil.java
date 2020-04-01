package cn.xzj.agent.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des
 */
public class PermissionsUtil {
    public static boolean allowPermissions(Activity activity, String... requestPermissions) {
        for (String permission : requestPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据权限获取权限名
     *
     * @param requestPermissions 具体权限
     * @return 返回权限对应的权限名 以空格隔开
     */
    public static String permissionNames(String... requestPermissions) {
        StringBuilder mStringBuffer = new StringBuilder();
        for (String permission : requestPermissions) {
            mStringBuffer.append(" ");
            mStringBuffer.append(permissionName(permission));
        }
        return mStringBuffer.toString();
    }
//    public static String permissionNames(List<String> requestPermissions){
//        StringBuilder mStringBuffer = new StringBuilder();
//        for (String permission : requestPermissions) {
//            mStringBuffer.append(" ");
//            mStringBuffer.append(permissionName(permission));
//        }
//        return mStringBuffer.toString();
//    }

    private static String permissionName(String permission) {
        String name = "";
        switch (permission) {
            case Manifest.permission.READ_SMS:
                name = "短信记录";
                break;
            case Manifest.permission.READ_PHONE_STATE:
                name = "手机状态信息";
                break;
            case Manifest.permission.READ_CALL_LOG:
                name = "通话记录";
                break;
            case Manifest.permission.CAMERA:
                name = "拍照";
                break;
            case Manifest.permission.CALL_PHONE:
                name = "拨打电话";
                break;
            case Manifest.permission.RECORD_AUDIO:
                name = "麦克风";
                break;
        }
        return name;
    }

    /**
     * 跳转到权限设置界面
     */
    public static void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getApplicationContext().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getApplicationContext().getPackageName());
        }
        context.startActivity(intent);
    }

    /**
     * 请求权限
     * @param activity 上下文
     * @param permission 权限
     * @param rationale 请求权限提示内容
     * @param requestCode 请求code
     */
    public static void requestPermission(final Activity activity, final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            new AlertDialog.Builder(activity)
                    .setTitle("权限请求")
                    .setMessage(rationale)
                    .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton("拒绝", null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }
}
