package com.ad.taoyou.swk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.common.view.SecClickListener;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/23.
 */

public class FragmentActivity extends BaseFragment implements SecClickListener {
    public static final String TAG = "FragmentActivity";
    private static final String ARG_PARAM = "param";
    private String mParam;
    private int page = 0;


    public PullToRefreshRecyclerView recyclerView;

    private AdapterActivity adapterVActivity;
    private List<ActivityInfo> mList = new ArrayList<>();

    public static FragmentActivity newInstance(String param) {
        FragmentActivity fragment = new FragmentActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_activity;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        recyclerView = (PullToRefreshRecyclerView) mRootView.findViewById(R.id.recyclerview);
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
                mRootView.findViewById(R.id.bg).setVisibility(View.VISIBLE);
            }
        }

    };

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initView() {
        RecyclerView recyclerView1 = recyclerView.getRefreshableView();
        adapterVActivity = new AdapterActivity(mContext, mList);
        adapterVActivity.setSecClickListener(this);
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView1.setAdapter(adapterVActivity);

        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page = 0;
                mList.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
            }
        });
    }

    @Override
    public void initData() {
        page++;
        String url = HttpTaskValues.API_POST_ACTIVITY_LIST;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("p", page);
        params.addFormDataPart("s", 10);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.8.1 活动列表接口" + rawJsonData);
                    try {
                        List<ActivityInfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<ActivityInfo>>() {
                                }.getType());

                        if (list.size() > 0) {
                            mList.addAll(list);
                            if (mList.size() == list.size())
                                adapterVActivity.notifyDataSetChanged();
                            else
                                adapterVActivity.notifyItemRangeChanged(mList.size() - list.size(), list.size());

                            recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (page!=1){
                                MyApplication.showToast("暂无更多活动");
                                recyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.onRefreshComplete();
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    @Override
    public void onSecClick(View view, int position) {
        mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("actId", mList.get(position).getActId());
        bundle.putString("url", mList.get(position).getUrl());
        skipFragment(FragmentActivityDetails.TAG, new FragmentActivityDetails(), this, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

}
