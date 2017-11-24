package com.ad.taoyou.swk.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ad.taoyou.ActivityWeb;
import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.utils.LoadingDialogUtil;
import com.ad.taoyou.common.utils.SignUtils;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/24.
 * 充值
 */

public class FragmentBalance extends BaseFragment {
    public static final String TAG = "FragmentVBalance";
    private int money = 10;//用户进入充值页面默认选择10元

    public TextView mTvPayType;
    public TextView mTvPayMoney;
    private TextView mTvcoin, mTvScore;
    private EditText mEtOther;
    private String order;

    private TextView mTvTMoney, mTvTTotle;

    private String tMoney, tName,tOrder;
    private int tCount;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_balance;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        mTvPayType = (TextView) mRootView.findViewById(R.id.tv_pay_type);
        mTvPayMoney = (TextView) mRootView.findViewById(R.id.tv_pay_money);
        mTvcoin = (TextView) mRootView.findViewById(R.id.tv_balance);
        mTvScore = (TextView) mRootView.findViewById(R.id.tv_amoy_bean);
        mTvTTotle = (TextView) mRootView.findViewById(R.id.tv_ttotle);
        mEtOther = (EditText) mRootView.findViewById(R.id.btn_other);
        mTvTMoney = (TextView) mRootView.findViewById(R.id.tv_tmoney);


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
            if (action.equals(TAG)) {
                callBack();
            }
        }

    };

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, "");

        } else if (i == R.id.rl_alipay) {//选择支付宝支付
            mTvPayType.setText("支付宝");
            mRootView.findViewById(R.id.img_alipay_check).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.img_card_check).setVisibility(View.GONE);
            mRootView.findViewById(R.id.img_wx_check).setVisibility(View.GONE);

        } else if (i == R.id.rl_wx) {//选择微信支付
            mTvPayType.setText("微信");
            mRootView.findViewById(R.id.img_alipay_check).setVisibility(View.GONE);
            mRootView.findViewById(R.id.img_card_check).setVisibility(View.GONE);
            mRootView.findViewById(R.id.img_wx_check).setVisibility(View.VISIBLE);

        } else if (i == R.id.rl_card) {//选择信用卡支付
            mTvPayType.setText("信用卡");
            mRootView.findViewById(R.id.img_alipay_check).setVisibility(View.GONE);
            mRootView.findViewById(R.id.img_card_check).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.img_wx_check).setVisibility(View.GONE);

        } else if (i == R.id.btn_ten) {//10元
            mEtOther.setText("");
            money = 10;
            clear();
            mTvPayMoney.setText("10元");
            mRootView.findViewById(R.id.btn_ten).setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
            ((TextView) mRootView.findViewById(R.id.btn_ten)).setTextColor(getResources().getColor(R.color.white));

        } else if (i == R.id.btn_twenty) {//20元
            mEtOther.setText("");
            money = 20;

            clear();
            mTvPayMoney.setText("20元");
            mRootView.findViewById(R.id.btn_twenty).setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
            ((TextView) mRootView.findViewById(R.id.btn_twenty)).setTextColor(getResources().getColor(R.color.white));

        } else if (i == R.id.btn_fifty) {//50元
            mEtOther.setText("");
            money = 50;
            clear();
            mTvPayMoney.setText("50元");
            mRootView.findViewById(R.id.btn_fifty).setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
            ((TextView) mRootView.findViewById(R.id.btn_fifty)).setTextColor(getResources().getColor(R.color.white));

        } else if (i == R.id.btn_one_hundred) {//100元
            mEtOther.setText("");

            money = 100;
            clear();
            mTvPayMoney.setText("100元");
            mRootView.findViewById(R.id.btn_one_hundred).setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
            ((TextView) mRootView.findViewById(R.id.btn_one_hundred)).setTextColor(getResources().getColor(R.color.white));

        } else if (i == R.id.btn_two_hundred) {//200元
            mEtOther.setText("");

            money = 200;
            clear();
            mTvPayMoney.setText("200元");
            mRootView.findViewById(R.id.btn_two_hundred).setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
            ((TextView) mRootView.findViewById(R.id.btn_two_hundred)).setTextColor(getResources().getColor(R.color.white));

        } else if (i == R.id.btn_three_hundred) {//300元
            mEtOther.setText("");

            money = 300;
            clear();
            mTvPayMoney.setText("300元");
            mRootView.findViewById(R.id.btn_three_hundred).setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
            ((TextView) mRootView.findViewById(R.id.btn_three_hundred)).setTextColor(getResources().getColor(R.color.white));

        } else if (i == R.id.btn_instant_echarge) {//充值
            Log.e(TAG, "money:" + money);
            if (money == 0) {
                MyApplication.showToast("请选择您需要充值的金额");
                return;
            }
            if ("支付宝".equals(mTvPayType.getText().toString())) {
                if (!TextUtils.isEmpty(tMoney))
                    //游戏直接充值
                    alipayGame();
                else
                    //sdk内充值
                    alipayBalance();
            }
        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_alipay).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_wx).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_card).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_ten).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_twenty).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_fifty).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_one_hundred).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_two_hundred).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_three_hundred).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_instant_echarge).setOnClickListener(this);
        mEtOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clear();
                mRootView.findViewById(R.id.btn_other).setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
                ((TextView) mRootView.findViewById(R.id.btn_other)).setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                money = !TextUtils.isEmpty(editable.toString()) ? Integer.valueOf(editable.toString()) : 0;
                mTvPayMoney.setText(!TextUtils.isEmpty(editable.toString()) ? editable.toString() : "0");
            }
        });

    }

    /**
     * 清楚所有金额的选中状态
     */
    private void clear() {
        //清除背景
        mRootView.findViewById(R.id.btn_ten).setBackgroundResource(R.drawable.dra_pay_balance_bg);
        mRootView.findViewById(R.id.btn_twenty).setBackgroundResource(R.drawable.dra_pay_balance_bg);
        mRootView.findViewById(R.id.btn_fifty).setBackgroundResource(R.drawable.dra_pay_balance_bg);
        mRootView.findViewById(R.id.btn_one_hundred).setBackgroundResource(R.drawable.dra_pay_balance_bg);
        mRootView.findViewById(R.id.btn_two_hundred).setBackgroundResource(R.drawable.dra_pay_balance_bg);
        mRootView.findViewById(R.id.btn_three_hundred).setBackgroundResource(R.drawable.dra_pay_balance_bg);
        mRootView.findViewById(R.id.btn_other).setBackgroundResource(R.drawable.dra_pay_balance_bg);

        //清除字体颜色
        ((TextView) mRootView.findViewById(R.id.btn_ten)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
        ((TextView) mRootView.findViewById(R.id.btn_twenty)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
        ((TextView) mRootView.findViewById(R.id.btn_fifty)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
        ((TextView) mRootView.findViewById(R.id.btn_one_hundred)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
        ((TextView) mRootView.findViewById(R.id.btn_two_hundred)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
        ((TextView) mRootView.findViewById(R.id.btn_three_hundred)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
        ((TextView) mRootView.findViewById(R.id.btn_other)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
    }

    @Override
    public void initData() {
        //默认选中支付宝
        mTvPayType.setText("支付宝");
        //默认充值金额是10元
        mTvPayMoney.setText("10元");

        mTvcoin.setText(!TextUtils.isEmpty(UserInfo.getInstance().getBalanceCoin())
                ? "余额：" + UserInfo.getInstance().getBalanceCoin() + "淘币" :
                "余额：0淘币");
        mTvScore.setText(!TextUtils.isEmpty(UserInfo.getInstance().getBalanceScore())
                ? UserInfo.getInstance().getBalanceScore() + "淘豆" :
                "0淘豆");

        String param = getArguments().getString("param");
        if (!"p".equals(param)) {
            String[] tmp = param.split("&");
            tMoney = tmp[0];
            tName = tmp[1];
            tCount = Integer.valueOf(tmp[2]);
            tOrder = tmp[3];
            mRootView.findViewById(R.id.rl_balance).setVisibility(View.GONE);
            mRootView.findViewById(R.id.money1).setVisibility(View.GONE);
            mRootView.findViewById(R.id.money2).setVisibility(View.GONE);
            mRootView.findViewById(R.id.t_ll).setVisibility(View.VISIBLE);
            mTvTMoney.setText(tName);
            money = Integer.valueOf(tMoney);
            mTvPayMoney.setText(money + "元");
//            // 支付宝×100
//            money = money * 100;
            mTvTTotle.setText(!TextUtils.isEmpty(UserInfo.getInstance().getBalanceCoin())
                    ? UserInfo.getInstance().getBalanceCoin() + "淘币" : "0淘币");
        }
    }

    private void alipayGame() {
//        money = money * 100;
        LoadingDialogUtil.showLoadingDialog(getActivity());
        long time = System.currentTimeMillis() / 1000;
        String url = HttpTaskValues.API_POST_GAME_PREORDER;
        RequestParams params = new RequestParams();
        params.addFormDataPart("tpfCode", "ITOYO");
        params.addFormDataPart("gc", MyApplication.gc);
        params.addFormDataPart("pscodeEnum", "USIOYO");
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("time", time);
        params.addFormDataPart("unitPrice", money);
        params.addFormDataPart("itemName", tName);
        params.addFormDataPart("count", tCount);
        params.addFormDataPart("gameOrderNo", tOrder);
        params.addFormDataPart("ty_payDiscount", 0);
        HttpRequest.post(url,params,new HttpRequestCallBack(mContext){
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    OrderInfo info = new Gson().fromJson(jsonObject.get("data").toString(), OrderInfo.class);
//                    try {
//                        info.setPreUrl(new java.net.URLDecoder().decode(info.getPreUrl(), "UTF-8"));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
                    order = info.getPayOrderNo();
                    LoadingDialogUtil.dismissLoadingDialog();
                    Intent intent = new Intent(mContext, ActivityWeb.class);
                    intent.putExtra(ActivityWeb.EXTRA_TYPE,ActivityWeb.EXTRA_TYPE_GAME );
                    intent.putExtra(ActivityWeb.EXTRA_URL, info.getPreUrl());
                    intent.putExtra(ActivityWeb.EXTRA_ORDER, info.getPayOrderNo());
                    getActivity().startActivity(intent);
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    private void alipayBalance() {
//        money = money * 100;

        LoadingDialogUtil.showLoadingDialog(getActivity());
        long time = System.currentTimeMillis() / 1000;
        Map<String, String> map = new HashMap<>();
        map.put("ty_ctoken", UserInfo.getInstance().getToken());
        map.put("ty_cid", UserInfo.getInstance().getCid());
        map.put("gopenid", UserInfo.getInstance().getGopenid());
        map.put("time", time + "");
        map.put("payMoney", money + "");
        String sign = SignUtils.getSign(map, MyApplication.secretKey);
        Log.e(TAG, sign);
        String url = HttpTaskValues.API_POST_ALIPAY_PREWAPCOINORDER;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("payMoney", money);
        params.addFormDataPart("time", time);
        params.addFormDataPart("sign", sign);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.4.3 创建手机网站淘币预订单" + rawJsonData);
                    OrderInfo info = new Gson().fromJson(jsonObject.get("data").toString(), OrderInfo.class);
                    try {
                        info.setPayUrl(new java.net.URLDecoder().decode(info.getPayUrl(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    order = info.getPayOrderNo();
                    LoadingDialogUtil.dismissLoadingDialog();
                    Intent intent = new Intent(mContext, ActivityWeb.class);
                    intent.putExtra(ActivityWeb.EXTRA_TYPE,ActivityWeb.EXTRA_TYPE_TB );
                    intent.putExtra(ActivityWeb.EXTRA_URL, info.getPayUrl());
                    intent.putExtra(ActivityWeb.EXTRA_ORDER, info.getPayOrderNo());
                    getActivity().startActivity(intent);
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    private void getCoin() {
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
                        UserInfo.instance.setBalanceCoin(balanceCoin);
                        mTvcoin.setText("余额：" + balanceCoin + "淘币");
                        mTvTTotle.setText(balanceCoin + "淘币");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    private void callBack() {
        //回调服务器
        long time = System.currentTimeMillis() / 1000;
        Map<String, String> map = new HashMap<>();
        map.put("ty_ctoken", UserInfo.getInstance().getToken());
        map.put("ty_cid", UserInfo.getInstance().getCid());
        map.put("gopenid", UserInfo.getInstance().getGopenid());
        map.put("signTime", time + "");
        map.put("payOrderNo", order);
        String sign = SignUtils.getSign(map, MyApplication.secretKey);
        final RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("payOrderNo", order);
        params.addFormDataPart("signTime", time);
        params.addFormDataPart("sign", sign);
        HttpRequest.post(HttpTaskValues.API_POST_ALIPAY_CALLBACK, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
//                Log.d(TAG, JsonFormatUtils.formatJson(jsonObject.toJSONString()));
                getCoin();
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
