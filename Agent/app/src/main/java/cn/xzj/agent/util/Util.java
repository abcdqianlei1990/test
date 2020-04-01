package cn.xzj.agent.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.channey.utils.SharedPreferencesUtils;
import com.channey.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.xzj.agent.BuildConfig;
import cn.xzj.agent.constants.Keys;


/**
 * Created by channey
 */
public class Util {

    public static Toast toast;
    private static ExecutorService newCachedThreadPool = null;

    private static ExecutorService getNewCachedThreadPool() {
        if (newCachedThreadPool == null) {
            synchronized (Executors.class) {
                if (newCachedThreadPool == null) {
                    newCachedThreadPool = Executors.newCachedThreadPool();
                }
            }
        }
        return newCachedThreadPool;
    }


    public static Camera.Size getNearestRatioSize(Camera.Parameters para,
                                                  final int screenWidth, final int screenHeight) {
        List<Camera.Size> supportedSize = para.getSupportedPreviewSizes();
        for (Camera.Size tmp : supportedSize) {
            if (tmp.width == 1280 && tmp.height == 720) {
                return tmp;
            }
        }
        Collections.sort(supportedSize, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                int diff1 = (((int) ((1000 * (Math.abs(lhs.width
                        / (float) lhs.height - screenWidth
                        / (float) screenHeight))))) << 16)
                        - lhs.width;
                int diff2 = (((int) (1000 * (Math.abs(rhs.width
                        / (float) rhs.height - screenWidth
                        / (float) screenHeight)))) << 16)
                        - rhs.width;

                return diff1 - diff2;
            }
        });

        return supportedSize.get(0);
    }


//    public static Camera.Size getNearestRatioSize(Camera.Parameters para, final int screenWidth, final int screenHeight) {
//        List<Camera.Size> supportedSize = para.getSupportedPreviewSizes();
//        Collections.sort(supportedSize, new Comparator<Camera.Size>() {
//            @Override
//            public int compare(Camera.Size lhs, Camera.Size rhs) {
//                int diff1 = (((int) ((1000 * (Math.abs(lhs.width / (float) lhs.height -
//                        screenWidth / (float) screenHeight))))) << 16) + lhs.width;
//                int diff2 = (((int) (1000 * (Math.abs(rhs.width / (float) rhs.height -
//                        screenWidth / (float) screenHeight)))) << 16) + rhs.width;
//
//                return diff1 - diff2;
//            }
//        });
//
//        return supportedSize.get(0);
//    }

    public static String getTimeStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return simpleDateFormat.format(new Date());
    }

    public static void closeStreamSilently(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {

            }
        }
    }

    public static byte[] bmp2byteArr(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled())
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        Util.closeStreamSilently(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


//    public static void saveBitmap(final Bitmap bitmap, final String picName,final BaseActivity activity) {
//        RxPermissions rxPermissions = new RxPermissions(activity);
//        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .subscribe(new Action1<Permission>() {
//                    @Override
//                    public void call(Permission permission) {
//                        switch (permission.name) {
//                            case Manifest.permission.READ_EXTERNAL_STORAGE:
//                                if (permission.granted) {
//                                    try {
//                                        String filePath = Constants.PATH+picName;
//                                        //先穿件目录，为了避免open failed: ENOENT (No such file or directory).
//                                        File dir = new File(Constants.PATH);
//                                        if (!dir.exists()){
//                                            dir.mkdir();
//                                        }
//                                        File f = new File(filePath);
//                                        if (f.exists()) {
//                                            f.delete();
//                                        }
//                                        FileOutputStream out = new FileOutputStream(f);
//                                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//                                        out.flush();
//                                        out.close();
//
////            // 其次把文件插入到系统图库
////            try {
////                MediaStore.Images.Media.insertImage(context.getContentResolver(),
////                        f.getAbsolutePath(), picName, null);
////            } catch (FileNotFoundException e) {
////                e.printStackTrace();
////            }
//                                        // 最后通知图库更新
//                                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
//                                        showToast(activity,"保存图片成功");
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                } else {
//
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                });
//    }

    public static Bitmap getBitmap(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    /**
     * 删除文件
     *
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

    public static boolean storageIsAvailableForApplication() {
        long availableBytes = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                availableBytes = fs.getAvailableBytes();
                if (availableBytes < 1024 * 1024) {
                    return false;
                }
            }

        }
        return true;
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
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

    /**
     * Compress image by size, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param image
     * @param pixelW target pixel of width
     * @param pixelH target pixel of height
     * @return
     */
    public static Bitmap compressBitmap(@NonNull Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //compress第二个参数100表示当前压缩率设置为0，即不压缩
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        //设置inJustDecodeBounds为true后，调用decodeFile方法时，并不真正分配创建Bitmap所需的空间，
        //适用于仅仅只需要获取一些属性——比如原始图片的长度和宽度等的情况，比如在压缩图片时，需要计算属性inSampleSize的值，就需要获取原始图片的长度和宽度
        newOpts.inJustDecodeBounds = true;
        // Bitmap的一个内部类Bitmap.Config的枚举值：常用ALPHA_8、RGB_565和ARGB_8888，默认是ARGB_8888，其中，A代表透明度，R代表红色，G代表绿色，B代表蓝色
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
        //return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    public static void showToast(Context context) {
        Toast.makeText(context, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
    }

    public static String[] listToArray(@NonNull ArrayList<String> list) {
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }


    public static String getPath(Context context) {
        String path = context.getFilesDir().getAbsolutePath();
        return path + "/";
    }


    public static void shuffle(ArrayList<Object> list) {
        ArrayList<Object> newList = new ArrayList();
        Object[] array = list.toArray();
        Random random = new Random();
        int i = random.nextInt(5);
    }

    /**
     * 获取一个月前的日期
     *
     * @param date 传入的日期
     * @return
     */
    public static String getMonthAfter(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }


    public static String getTotalRam(Context context) {//GB
        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLine != null) {
            totalRam = (int) Math.ceil((new Float(Float.valueOf(firstLine) / (1024 * 1024)).doubleValue()));
        }

        return totalRam + "GB";//返回1GB/2GB/3GB/4GB
    }

    /**
     * 判断当前版本是否需要更新
     *
     * @param versionCode 服务器上记录的最新版本号
     * @return
     */
    public static boolean needUpdate(String versionCode) {
        if (!TextUtils.isEmpty(versionCode)) {
            if (BuildConfig.VERSION_CODE < Integer.parseInt(versionCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否首次激活（）
     *
     * @return
     */
    public static boolean isFirstActive(Context context) {
        String firstActiveTime = SharedPreferencesUtils.INSTANCE.getString(context, Keys.FIRST_ACTIVE_TIME, Activity.MODE_MULTI_PROCESS);
        return StringUtils.INSTANCE.isEmpty(firstActiveTime);
    }

    public static void saveStringSet(Context context, String key, Set<String> value) {
        SharedPreferences sp = context.getSharedPreferences("", Activity.MODE_PRIVATE);
        sp.getStringSet(key, new HashSet<String>());
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        return c.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMin() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        return c.get(Calendar.MINUTE);
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }

    public static boolean isChinese(String text) {
        if (text.toString().matches("[\u4e00-\u9fa5]+")) {
            return true;
        }
        return false;
    }
}
