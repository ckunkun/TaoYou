package com.ad.taoyou;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.ad.taoyou.common.utils.FileUtil;
import com.snowfish.cn.ganga.helper.SFOnlineApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.finalteam.okhttpfinal.Part;
import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * Created by sunweike on 2017/8/24.
 */

public class MyApplication extends SFOnlineApplication {
    private static final String TAG = "MyApplication";
    public static final String USER_INFO = "userinfo";
    public static String secretKey = "";
    public static String gc = "";
    public static String tpfCode = "";
    public static Application sApplication;
    public static WindowManager sWindowManager;
    public static Display sDisplay;
    public static Point sScreensPoint;
    public static int sScreensWidth, sScreensHeight;
    private HttpUrl url;
    public static HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

    public static Application getInstance() {
//        if (sApplication == null) {
//            sApplication = new MyApplication();
//        }
        return sApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        initOkHttpFinal();
        gc = getChannel(this, "gc");
        tpfCode = getChannel(this, "tpfCode");
        secretKey = getChannel(this, "secretKey");
//        gc = "EwNUOu";
//        tpfCode = "SDKYYB";
//        secretKey = "NXdXYIhxsuUWWyYvySlanwMtsVXPdhHg";
    }

    private void initOkHttpFinal() {
        List<Part> commomParams = new ArrayList<>();
        Headers commonHeaders = new Headers.Builder().build();
        commonHeaders.newBuilder().set("connection", "close");

        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(35000)
                .setInterceptors(interceptorList)
                .setCache(new Cache(new File(FileUtil.getCachePath() + "/caches"), 10240 * 1024))
                .setCookieJar(new CookieJar() {

                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
                        url = httpUrl;
                        cookieStore.put(httpUrl, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                //.setCertificates(...)
                //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
                .setDebug(true);
//        addHttps(builder);
        OkHttpFinal.getInstance().init(builder.build());
    }

    public static void showToast(String msg) {
        showToast(msg, false);
    }


    public static void showToast(String msg, boolean isCenter) {
        Toast toast = Toast.makeText(getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        if (isCenter)
            toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static WindowManager getWindowManager() {
        if (sWindowManager == null) {
            sWindowManager = (WindowManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return sWindowManager;
    }

    public static Display getDisplay() {
        if (sDisplay == null) {
            sDisplay = getWindowManager().getDefaultDisplay();
        }
        return sDisplay;
    }

    public static Point getScreensPoint() {
        if (sScreensPoint == null) {
            sScreensPoint = new Point();
            getDisplay().getSize(sScreensPoint);
        }
        return sScreensPoint;
    }

    public static int getScreensWidth() {
        if (sScreensWidth <= 0) {
            sScreensWidth = getScreensPoint().x;
        }
        return sScreensWidth;
    }

    public static int getScreensHeight() {
        if (sScreensHeight <= 0) {
            sScreensHeight = getScreensPoint().y;
        }
        return sScreensHeight;
    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取meta-data属性
     *
     * @param context
     * @return
     */
    public String getChannel(Context context, String str) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo.metaData.getString(str);
    }
}
