package com.ad.taoyou.swk.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/9/4.
 */

public class TYService extends Service {

    public static final String TAG = "TYService";

    private MyBinder mBinder = new MyBinder();
    private Context mContext;
    private int heart = 600;//心跳时间默认10分钟
    Timer timer = new Timer();
    private int time = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        Log.e("SplashActivity", "TaoYou pid:" + android.os.Process.myPid());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
        timer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!TextUtils.isEmpty(UserInfo.getInstance().getHeartbeatInterval())) {
                heart = Integer.valueOf(UserInfo.getInstance().getHeartbeatInterval());
            }
//            Log.d(TAG, "heart:" + heart);
            if (time % Integer.valueOf(heart) == 0) {
                Log.d(TAG, "heartbeat");
                time = 0;
                //心跳
                RequestParams params = new RequestParams();
                params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
                params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
                params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
                HttpRequest.post(HttpTaskValues.API_POST_HEARTBEAT, params, new HttpRequestCallBack(mContext));
            }

        }
    };

    public class MyBinder extends Binder {


        public void startHeartBeat(Context context) {
            Log.d(TAG, "startHeartBeat() executed");
            mContext = context;
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    time++;
                    handler.sendEmptyMessage(0);
                }
            };
            timer.schedule(task, 0, 1000);
        }

    }
}
