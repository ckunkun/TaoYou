package com.ad.taoyou.swk.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ad.taoyou.HomeActivity;
import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/24.
 */
public class FragmentHome extends BaseFragment {
    public static final String TAG = "FragmentHome";
    private static final String ARG_PARAM = "param";
    private String mParam;

    public TextView mTvBalance;

    public static FragmentHome newInstance(String param) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mTvBalance = (TextView) mRootView.findViewById(R.id.tv_balance);
        initView();
        initData();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(TAG);
        // 注册广播
        mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    // 广播处理
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: "+action );
            if (action.equals(TAG)) {
                if (intent.getBooleanExtra("hide", false))
                    mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
                else {
                    mTvBalance.setText("余额：" + UserInfo.getInstance().getBalanceCoin());
                    mRootView.findViewById(R.id.bg).setVisibility(View.VISIBLE);
                }
            } else if (action.equals(FragmentBalance.TAG)) {
                //刷新淘币余额
                initData();
            } else if (action.equals(HomeActivity.TAG)) {
                MyApplication.showToast("隐藏首页");
                mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
            }
        }

    };

    @Override
    public void onClick(View view) {
        mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
        int i = view.getId();
        if (i == R.id.rl_balance) {//充值
            //存在游戏进来的充值金额时
            Bundle bundle = new Bundle();
            bundle.putString("param", mParam);
            skipFragment(FragmentBalance.TAG, new FragmentBalance(), null, bundle);
        } else if (i == R.id.rl_billing_record) {//账单记录
            skipFragment(FragmentRecharge.TAG, new FragmentRecharge(), null);
        } else if (i == R.id.rl_vouches) {//我的代金劵
            mRootView.findViewById(R.id.bg).setVisibility(View.VISIBLE);
            MyApplication.showToast("暂未开放");
            return;
//            skipFragment(FragmentVouches.TAG, new FragmentVouches(), null);
        } else if (i == R.id.rl_safe_center) {//安全中心
            skipFragment(FragmentSafe.TAG, new FragmentSafe(), null);
        } else if (i == R.id.rl_help) {//帮助
            skipFragment(FragmentHelp.TAG, new FragmentHelp(), null);
        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.rl_balance).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_billing_record).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_vouches).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_safe_center).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_help).setOnClickListener(this);
        mParam = getArguments().getString(ARG_PARAM);
        if (!"p".equals(mParam)) {
            //游戏直接充值时直接跳转充值页面
            Bundle bundle = new Bundle();
            bundle.putString("param", mParam);
            mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
            skipFragment(FragmentBalance.TAG, new FragmentBalance(), null, bundle);
        }
    }

    @Override
    public void initData() {
        String url = HttpTaskValues.API_POST_COIN;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.5.3 我的淘币余额接口" + rawJsonData);
                    try {
                        String balanceCoin = new org.json.JSONObject(jsonObject.get("data").toString()).getString("balanceCoin");
                        if (TextUtils.isEmpty(balanceCoin)) {
                            mTvBalance.setText("余额：0");
                            UserInfo.instance.setBalanceCoin("0");
                        } else {
                            mTvBalance.setText("余额：" + balanceCoin);
                            UserInfo.instance.setBalanceCoin(balanceCoin);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return super.onSuccess(jsonObject, msg);
            }
        });
        getScore();
    }

    private void getScore() {
        String url = HttpTaskValues.API_POST_SCORE;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.5.3 我的淘币余额接口" + rawJsonData);
                    try {
                        String balanceScore = new org.json.JSONObject(jsonObject.get("data").toString()).getString("balanceScore");
                        UserInfo.instance.setBalanceScore(!TextUtils.isEmpty(balanceScore) ? balanceScore : "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

}
