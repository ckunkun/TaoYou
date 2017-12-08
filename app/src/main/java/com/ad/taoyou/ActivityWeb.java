package com.ad.taoyou;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ad.taoyou.common.helper.GamePayCallBack;
import com.ad.taoyou.common.helper.PayCallBack;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.home.FragmentBalance;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import java.util.HashMap;
import java.util.Map;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

/**
 * Created by sunweike on 2017/9/13.
 */

public class ActivityWeb extends Activity {
    public static final String TAG = "ActivityWeb";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_ORDER = "order";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_TYPE_GAME = "game";
    public static final String EXTRA_TYPE_TB = "coin";

    public static PayCallBack payCallBack;
    public static GamePayCallBack gamePayCallBack;
    private WebView mWebView;
    private String url;
    private int payType = 2;
    private boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web);
        initView();
    }

    public void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setMinimumFontSize(settings.getMinimumFontSize() + 8);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        mWebView.setVerticalScrollbarOverlay(true);
        url = getIntent().getStringExtra(EXTRA_URL);

        if (url.contains("wx.tenpay")) {
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("Referer", "http://m.xjtmjoy.com/");
            mWebView.loadUrl(url, extraHeaders);
            payType = 1;
        } else {
            mWebView.loadUrl(url);
            payType = 2;
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                Log.i(TAG, "shouldOverrideUrlLoading:" + url);
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    /**
                     * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
                     */
                    //通过服务器的支付链接由web调起APP支付
                    final PayTask task = new PayTask(ActivityWeb.this);
                    boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {

                        @Override
                        public void onPayResult(H5PayResultModel result) {
                            // 如果是游戏的type
                            if (EXTRA_TYPE_GAME.equals(getIntent().getStringExtra(EXTRA_TYPE))) {
                                gamePayCallBack.onResult(result.getResultCode(), getIntent().getStringExtra(EXTRA_ORDER));
                            } else {
                                // 淘币充值的回调
                                payCallBack.onResult(result.getResultCode(), getIntent().getStringExtra(EXTRA_ORDER));
                            }
                            //支付结果返回
                            if (result.getResultCode().equals("9000")) {
                                //支付成功
                                Log.d(TAG, "pay success");
                                sendBroadcast(new Intent(FragmentBalance.TAG));
                                ActivityWeb.this.finish();
                            } else if (result.getResultCode().equals("6001")) {
                                //用户关闭
                                Log.d(TAG, "pay fail");
                                ActivityWeb.this.finish();
                            }
//                        final String u = result.getReturnUrl();
//
//                        if (!TextUtils.isEmpty(u)) {
//                            ActivityWeb.this.runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//

//                                }
//                            });
//                        }
                        }
                    });
                    /**
                     * 判断是否成功拦截
                     * 若成功拦截，则无需继续加载该Url；否则继续加载
                     */
                    if (!isIntercepted) {
                        mWebView.loadUrl(url);
                    }
                    return true;
                }

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirst = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFirst = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            return;
        }
        if (payType == 2) {
            return;
        }
        RequestParams params = new RequestParams();
        params.addFormDataPart("orderNo", getIntent().getStringExtra(EXTRA_ORDER));
        Log.i(TAG, getIntent().getStringExtra(EXTRA_ORDER));
        HttpRequest.post(HttpTaskValues.WECHAT_FIND_IS_PAY, params, new HttpRequestCallBack(this) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {

                    Log.i(TAG, jsonObject.toString());
                    String ret = jsonObject.getString("ret");
                    if (ret.equals("0")) {
                        Toast.makeText(ActivityWeb.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityWeb.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                return super.onSuccess(jsonObject, msg);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable t) {
            }
            mWebView = null;
        }
    }
}
