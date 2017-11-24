package com.ad.taoyou.common.imageloder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ad.taoyou.R;
import com.ad.taoyou.common.utils.FileUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UILUtil {
    private static ImageLoader sImageLoader;

    public static ImageLoader getLoader(Context context) {
        if (sImageLoader == null) {

//            File cacheDir = StorageUtils.getOwnCacheDirectory(
//                    context.getApplicationContext(),
//                    MainApplication.PROJECT_DISK_PATH + "image" + File.separator + "caches");
            File cacheDir = new File(FileUtil.getImgDIrectory() + File.separator + "caches");

            ImageLoaderConfiguration config = new ImageLoaderConfiguration
                    .Builder(context)
                    //.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                    //.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                    .threadPoolSize(3)//线程池内加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
//                                        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                    .memoryCacheSize(2 * 1024 * 1024)
                    .diskCacheSize(50 * 1024 * 1024)
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                    //                    .diskCacheFileCount(100) //缓存的文件数量
//                    .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                    //.writeDebugLogs() // Remove for release app
                    .build();//开始构建
            // Initialize ImageLoader with configuration.

            ImageLoader.getInstance().init(config);
            sImageLoader = ImageLoader.getInstance();
        }
        return sImageLoader;
    }

    public static DisplayImageOptions circleOption() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_user_empty) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_user_empty)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_user_empty)  //设置图片加载/解码过程中错误时候显示的图片
                //.cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                //.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                //.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new CircleBitmapDisplayer())//是否设置为圆角，弧度为多少
                //.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    public static DisplayImageOptions normalOption() {
        return new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_empty) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.ic_empty)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.ic_empty)  //设置图片加载/解码过程中错误时候显示的图片
                //.cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                //.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                //.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                //.displayer(new CircleBitmapDisplayer())//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    public static DisplayImageOptions normalOption1() {
        return new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_normal_empty) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.ic_normal_empty)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.ic_normal_empty)  //设置图片加载/解码过程中错误时候显示的图片
                //.cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                //.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                //.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                //.displayer(new CircleBitmapDisplayer())//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }


    /**
     * return a bitmap from service
     *
     * @param url
     * @return bitmap type
     */
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;

        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;

            conn = (HttpURLConnection) myFileUrl.openConnection();

            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }
}
