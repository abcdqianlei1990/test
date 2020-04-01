package cn.xzj.agent.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.xzj.agent.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by channey on 2018/3/6.
 */

public class ImageLoader extends com.youth.banner.loader.ImageLoader {

    /**
     *
     * @param context
     * @param path
     * @param imageView
     */
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).placeholder(R.mipmap.ic_default_company_img).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int placeholderResId){
        Glide.with(context).load(encoding(url)).placeholder(placeholderResId).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(encoding(url)).into(imageView);
    }

//    public static void loadImagePicasso(Context context, String url, ImageView imageView, int placeholderResId){
//        Picasso.with(context).load(encoding(url)).placeholder(placeholderResId).into(imageView);
//    }
//
//    public static void loadImagePicasso(Context context, String url, ImageView imageView){
//        Picasso.with(context).load(encoding(url)).into(imageView);
//    }
//
//    /**
//     * 加载圆角图片
//     * @param context
//     * @param url
//     * @param imageView
//     * @param placeholderResId
//     * @param radius
//     */
//    public static void loadRoundImg(Context context, String url, ImageView imageView, int placeholderResId, int radius){
//        if (!StringUtils.INSTANCE.isEmpty(url)){
//            Transformation transformation = new RoundedTransformationBuilder()
//                    .cornerRadiusDp(radius)
//                    .oval(false)
//                    .build();
//            Picasso.with(context)
//                    .load(encoding(url))
//                    .placeholder(placeholderResId)
//                    .fit()
//                    .transform(transformation)
//                    .into(imageView);
//        }
//    }

//    public static void loadCircleImg(Context context, String url, ImageView view,int placeholderResId){
//        Glide.with(context).load(url).placeholder(placeholderResId)
//                .bitmapTransform(new CropCircleTransformation(new BitmapPool() {
//                    @Override
//                    public int getMaxSize() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void setSizeMultiplier(float sizeMultiplier) {
//
//                    }
//
//                    @Override
//                    public boolean put(Bitmap bitmap) {
//                        return false;
//                    }
//
//                    @Override
//                    public Bitmap get(int width, int height, Bitmap.Config config) {
//                        return null;
//                    }
//
//                    @Override
//                    public Bitmap getDirty(int width, int height, Bitmap.Config config) {
//                        return null;
//                    }
//
//                    @Override
//                    public void clearMemory() {
//
//                    }
//
//                    @Override
//                    public void trimMemory(int level) {
//
//                    }
//                })).crossFade(1000).into(view);
//    }

    /**
     * 解决中文编码问题
     * @param url
     * @return
     */
    private static String encoding(String url){
        return Uri.encode(url, "-![.:/,%?&=]");
    }
}
