package com.ad.taoyou.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/***
 *
 */
public class FileUtil {
    //SD卡根路径
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

    //手机的缓存根目录
    private static String mDataRootPath = "";

    //保存图片的目录
    private final static String IMG_PATH = "/taoyou/image";
    private final static String IMG_SUFFIX = ".png";
    private final static String CACHE_PATH = "/taoyou";


    public FileUtil(Context context) {
        mDataRootPath = context.getCacheDir().getPath();
    }

    //获取图片存储的根目录
    public static String getImgDIrectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + IMG_PATH : mDataRootPath + IMG_PATH;
    }

    public static String getCachePath() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + CACHE_PATH : mDataRootPath + CACHE_PATH;
    }

    public static String getImgpath(String imgName) {
        File file = new File(getImgDIrectory());
        if (!file.exists()) {
            file.mkdirs();
        }
        return getImgDIrectory() + imgName + IMG_SUFFIX;
    }

    //图片保存到本地，--返回图片的路径
    public static String saveImg(ByteArrayOutputStream bos) {
        FileOutputStream fos = null;
        String path = getImgpath(String.valueOf(System.currentTimeMillis()));
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.flush();
        } catch (IOException e) {
            path = "";
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return path;

    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

}
