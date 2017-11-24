package com.ad.taoyou;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.taoyou.common.base.BaseActivity;
import com.ad.taoyou.common.imageloder.UILUtil;
import com.ad.taoyou.common.utils.AppManager;
import com.ad.taoyou.common.utils.DisplayUtil;
import com.ad.taoyou.common.utils.FragManager;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.utils.SharedPreferencesUtil;
import com.ad.taoyou.common.utils.Utils;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.common.view.CustomViewPager;
import com.ad.taoyou.swk.activity.FragmentActivity;
import com.ad.taoyou.swk.circle.FragmentCircle;
import com.ad.taoyou.swk.gift.FragmentGift;
import com.ad.taoyou.swk.home.ActivityMessage;
import com.ad.taoyou.swk.home.FragmentHome;
import com.ad.taoyou.swk.login.ActivityLogin;
import com.ad.taoyou.swk.login.UserInfo;
import com.ad.taoyou.swk.service.TYService;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

public class HomeActivity extends BaseActivity {
    public static final String TAG = "HomeActivity";
    public static final int TAB_HOME = 0, TAB_CIRCLE = 1, TAB_GIFT = 2, TAB_ACTIVITY = 3;
    public static int TAB_COUNT = 4;

    private Fragment mHomeFragment, mCircleFragment, mGiftFragment, mActivityFragment;
    private int mCurrentItem;

    private TYService.MyBinder myBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (TYService.MyBinder) service;
            myBinder.startHeartBeat(mContext);
        }
    };

    public CustomViewPager mViewPager;
    public ImageView img_menu1;
    public ImageView img_menu2;
    public ImageView img_menu3;
    public ImageView img_menu4;
    public TextView menu1;
    public TextView menu2;
    public TextView menu3;
    public TextView menu4;
    public ImageView imgAva;
    public TextView mTvName;
    public TextView mTvId, mTvCount;
    private Intent intent;

    @Override
    public int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        UserInfo.instance = (UserInfo) SharedPreferencesUtil.getObj(mContext, MyApplication.USER_INFO, UserInfo.class);
        if (UserInfo.getInstance() == null || TextUtils.isEmpty(UserInfo.getInstance().getCid())) {
            startActivity(new Intent(mContext, ActivityLogin.class));
            AppManager.getAppManager().finishActivity();
        }
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        img_menu1 = (ImageView) findViewById(R.id.img_menu1);
        img_menu2 = (ImageView) findViewById(R.id.img_menu2);
        img_menu3 = (ImageView) findViewById(R.id.img_menu3);
        img_menu4 = (ImageView) findViewById(R.id.img_menu4);
        imgAva = (ImageView) findViewById(R.id.tv_ava);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvId = (TextView) findViewById(R.id.tv_id);
        menu1 = (TextView) findViewById(R.id.menu1);
        menu2 = (TextView) findViewById(R.id.menu2);
        menu3 = (TextView) findViewById(R.id.menu3);
        menu4 = (TextView) findViewById(R.id.menu4);
        mTvCount = (TextView) findViewById(R.id.tv_num_message);
        initView();
        initData();
    }


    public void initView() {
        /****************************************
         * 配置viewpager
         */
        mViewPager.setOffscreenPageLimit(TAB_COUNT);
        mViewPager.setNoScroll(true);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        /*****************************************/

        View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.ll_menu1) {
                    onHome();
                    findViewById(R.id.container).setVisibility(View.VISIBLE);
                    findViewById(R.id.container2).setVisibility(View.GONE);
                    findViewById(R.id.container3).setVisibility(View.GONE);
                    findViewById(R.id.container4).setVisibility(View.GONE);

                } else if (i == R.id.ll_menu2) {
                    onCircle();
                    findViewById(R.id.container).setVisibility(View.GONE);
                    findViewById(R.id.container2).setVisibility(View.VISIBLE);
                    findViewById(R.id.container3).setVisibility(View.GONE);
                    findViewById(R.id.container4).setVisibility(View.GONE);

                } else if (i == R.id.ll_menu3) {
                    onGift();
                    findViewById(R.id.container).setVisibility(View.GONE);
                    findViewById(R.id.container2).setVisibility(View.GONE);
                    findViewById(R.id.container3).setVisibility(View.VISIBLE);
                    findViewById(R.id.container4).setVisibility(View.GONE);

                } else if (i == R.id.ll_menu4) {
                    onActivity();
                    findViewById(R.id.container).setVisibility(View.GONE);
                    findViewById(R.id.container2).setVisibility(View.GONE);
                    findViewById(R.id.container3).setVisibility(View.GONE);
                    findViewById(R.id.container4).setVisibility(View.VISIBLE);

                }
                mViewPager.setCurrentItem(mCurrentItem);
                supportInvalidateOptionsMenu();
            }
        };

        findViewById(R.id.ll_menu1).setOnClickListener(menuListener);
        findViewById(R.id.ll_menu2).setOnClickListener(menuListener);
        findViewById(R.id.ll_menu3).setOnClickListener(menuListener);
        findViewById(R.id.ll_menu4).setOnClickListener(menuListener);
        findViewById(R.id.btn_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ActivityMessage.class));
            }
        });

        onHome();

        activation();

    }

    private void activation() {
        //激活日志
        String url = HttpTaskValues.API_POST_ACTIVATES;
        RequestParams params = new RequestParams();
        params.addFormDataPart("gc", MyApplication.gc);
        params.addFormDataPart("device", Utils.getModel());
        params.addFormDataPart("system", Utils.getSystem());
        params.addFormDataPart("IMEI", Utils.getIdenty(mContext));
        params.addFormDataPart("mac", Utils.getMacAddress());
        HttpRequest.post(url, params, null);
    }


    @Override
    public void initData() {
        if (UserInfo.getInstance() != null && !TextUtils.isEmpty(UserInfo.getInstance().getCid())) {
            //打开服务开启心跳(一分钟一次)
            intent = new Intent(mContext, TYService.class);
            startService(intent);
            Intent bindIntent = new Intent(this, TYService.class);
            bindService(bindIntent, connection, BIND_AUTO_CREATE);
        }

        UILUtil.getLoader(mContext).displayImage(UserInfo.getInstance().getHeadImgUrl(), imgAva, UILUtil.circleOption());
        mTvName.setText(UserInfo.getInstance().getNickname());
        mTvId.setText("ID:" + UserInfo.getInstance().getNo());
    }

    private void onHome() {
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.view_bg1).setVisibility(View.GONE);
            findViewById(R.id.view_bg2).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg3).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg4).setVisibility(View.VISIBLE);
        } else {
            menu1.setTextColor(getResources().getColor(R.color.white));
            menu2.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu3.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu4.setTextColor(getResources().getColor(R.color.TranslucentWhite));

            img_menu1.setImageResource(R.mipmap.ic_home);
            img_menu2.setImageResource(R.mipmap.ic_hui_circle);
            img_menu3.setImageResource(R.mipmap.ic_hui_gift);
            img_menu4.setImageResource(R.mipmap.ic_hui_activity);
        }
        mCurrentItem = TAB_HOME;
        FragManager.curItem = TAB_HOME;
    }

    private void onCircle() {
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.view_bg1).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg2).setVisibility(View.GONE);
            findViewById(R.id.view_bg3).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg4).setVisibility(View.VISIBLE);
        } else {
            menu1.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu2.setTextColor(getResources().getColor(R.color.white));
            menu3.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu4.setTextColor(getResources().getColor(R.color.TranslucentWhite));

            img_menu1.setImageResource(R.mipmap.ic_hui_home);
            img_menu2.setImageResource(R.mipmap.ic_circle);
            img_menu3.setImageResource(R.mipmap.ic_hui_gift);
            img_menu4.setImageResource(R.mipmap.ic_hui_activity);
        }

        mCurrentItem = TAB_CIRCLE;
        FragManager.curItem = TAB_CIRCLE;
    }

    private void onGift() {
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.view_bg1).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg2).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg3).setVisibility(View.GONE);
            findViewById(R.id.view_bg4).setVisibility(View.VISIBLE);
        } else {
            menu1.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu2.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu3.setTextColor(getResources().getColor(R.color.white));
            menu4.setTextColor(getResources().getColor(R.color.TranslucentWhite));

            img_menu1.setImageResource(R.mipmap.ic_hui_home);
            img_menu2.setImageResource(R.mipmap.ic_hui_circle);
            img_menu3.setImageResource(R.mipmap.ic_gift);
            img_menu4.setImageResource(R.mipmap.ic_hui_activity);
        }

        mCurrentItem = TAB_GIFT;
        FragManager.curItem = TAB_GIFT;
    }

    private void onActivity() {
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.view_bg1).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg2).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg3).setVisibility(View.VISIBLE);
            findViewById(R.id.view_bg4).setVisibility(View.GONE);
        } else {
            menu1.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu2.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu3.setTextColor(getResources().getColor(R.color.TranslucentWhite));
            menu4.setTextColor(getResources().getColor(R.color.white));

            img_menu1.setImageResource(R.mipmap.ic_hui_home);
            img_menu2.setImageResource(R.mipmap.ic_hui_circle);
            img_menu3.setImageResource(R.mipmap.ic_hui_gift);
            img_menu4.setImageResource(R.mipmap.ic_activity);
        }

        mCurrentItem = TAB_ACTIVITY;
        FragManager.curItem = TAB_ACTIVITY;
    }

    private void getCount() {
        String url = HttpTaskValues.API_POST_MESSAGE_COUNT;
        final RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, "1.9.7 统计未读系统消息数" + rawJsonData);
                    try {
                        String count = new org.json.JSONObject(jsonObject.get("data").toString()).getString("countUnread");
                        if (Integer.valueOf(count) != 0) {
                            mTvCount.setText(count);
                            RelativeLayout.LayoutParams params;
                            if (Integer.valueOf(count) < 10) {
                                params = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(mContext, 15), DisplayUtil.dip2px(mContext, 15));
                                params.setMargins(DisplayUtil.dip2px(mContext, 12), DisplayUtil.dip2px(mContext, 4), 0, 0);
                            } else if (10 <= Integer.valueOf(count) && Integer.valueOf(count) < 100) {
                                params = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(mContext, 20), DisplayUtil.dip2px(mContext, 15));
                                params.setMargins(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 4), 0, 0);
                            } else {
                                params = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(mContext, 25), DisplayUtil.dip2px(mContext, 15));
                                params.setMargins(DisplayUtil.dip2px(mContext, 8), DisplayUtil.dip2px(mContext, 4), 0, 0);
                                mTvCount.setText("99+");
                            }
                            mTvCount.setGravity(Gravity.CENTER);
                            mTvCount.setTextSize(12);
                            mTvCount.setLayoutParams(params);
                        }
                        findViewById(R.id.tv_num_message).setVisibility(Integer.valueOf(count) == 0 ? View.GONE : View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (FragManager.homeTags != null && FragManager.homeTags.size() > 0) {
            for (int i = 0; i < FragManager.homeTags.size(); i++) {
                Log.e(TAG, FragManager.homeTags.get(i));
                if (FragManager.homeTags.get(i).equals("FragmentVBalance")) {
                    sendBroadcast(new Intent(FragmentHome.TAG).putExtra("hide", true));
                }
            }
        }

        getCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            unbindService(connection);
            stopService(intent);
        }
    }

    /* ***************FragmentPagerAdapter***************** */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            switch (position) {
                case TAB_HOME:
                    mHomeFragment = (FragmentHome) super.instantiateItem(container, position);
                    return mHomeFragment;
                case TAB_CIRCLE:
                    mCircleFragment = (FragmentCircle) super.instantiateItem(container, position);
                    return mCircleFragment;
                case TAB_GIFT:
                    mGiftFragment = (FragmentGift) super.instantiateItem(container, position);
                    return mGiftFragment;
                case TAB_ACTIVITY:
                    mActivityFragment = (FragmentActivity) super.instantiateItem(container, position);
                    return mActivityFragment;
                default:
                    return super.instantiateItem(container, position);
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_HOME:
                    if (!TextUtils.isEmpty(getIntent().getStringExtra("money"))) {
                        String param = getIntent().getStringExtra("money") + "&" + getIntent().getStringExtra("name")
                                + "&" + getIntent().getIntExtra("count", 1) + "&" + getIntent().getStringExtra("gameOrder");
                        return FragmentHome.newInstance(param);
                    }

                    return FragmentHome.newInstance("p");
                case TAB_CIRCLE:
                    return FragmentCircle.newInstance(null);
                case TAB_GIFT:
                    return FragmentGift.newInstance(null);
                case TAB_ACTIVITY:
                    return FragmentActivity.newInstance(null);
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && KeyEvent.KEYCODE_BACK == keyCode) {
            String curTag = FragManager.getFragManager().getCurFragment();
            if (TextUtils.isEmpty(curTag)) {
                //在首页四个页面时
                return false;
            } else {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                //有fragment时
                //获取当前显示的fragment
                Fragment f1 = manager.findFragmentByTag(curTag);

                String curTag2 = FragManager.getFragManager().getCurSecendFrag();
                if (TextUtils.isEmpty(curTag2)) {
                    //显示首页的view
                    //显示首页的view
                    switch (mCurrentItem) {
                        case TAB_HOME:
                            sendBroadcast(new Intent(FragmentHome.TAG));
                            break;
                        case TAB_CIRCLE:
                            break;
                        case TAB_GIFT:
                            sendBroadcast(new Intent(FragmentGift.TAG));
                            break;
                        case TAB_ACTIVITY:
                            sendBroadcast(new Intent(FragmentActivity.TAG));
                            break;
                    }
                    fragmentTransaction.hide(f1).commit();
                } else {
                    //获取第二个fragment
                    Fragment f2 = manager.findFragmentByTag(curTag2);
                    fragmentTransaction.show(f2).hide(f1).commit();
                }
                //清除fragment
//                List<Fragment> fragments = manager.getFragments();
//                fragments.remove(f1);
//                fragmentTransaction.remove(f1);
                //销毁最上层fragment的TAG
                FragManager.getFragManager().finishFragment();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
