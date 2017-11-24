package com.ad.taoyou.swk.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseActivity;
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
 * Created by sunweike on 2017/8/30.
 */

public class ActivityMessage extends BaseActivity implements SecClickListener {
    public static final String TAG = "ActivityMessage";

    public PullToRefreshRecyclerView recyclerView;

    private AdapterMessage adapterVMessage;
    private List<MessageInfo> mList = new ArrayList<>();
    private int page = 0;

    @Override
    public void initView() {
        recyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerview);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void initData() {
        RecyclerView recyclerView1 = recyclerView.getRefreshableView();
        adapterVMessage = new AdapterMessage(mContext, mList);
        adapterVMessage.setSecClickListener(this);
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView1.setAdapter(adapterVMessage);

        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page = 0;
                mList.clear();
                getMsg();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getMsg();
            }
        });

        getMsg();
    }


    @Override
    public void onSecClick(View view, int position) {
        mList.get(position).setRead(true + "");
        adapterVMessage.notifyDataSetChanged();
        Intent intent=new Intent(mContext,ActivityMessageDetails.class);
        intent.putExtra("id",mList.get(position).getTidingsId());
        startActivity(intent);
    }

    private void getMsg() {
        page++;
        String url = HttpTaskValues.API_POST_MESSAGE_LIST;
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
//                    Log.i(TAG, "1.9.1 系统消息列表" + rawJsonData);
                    try {
                        List<MessageInfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<MessageInfo>>() {
                                }.getType());

                        if (list.size() > 0) {
                            mList.addAll(list);
                            if (mList.size() == list.size())
                                adapterVMessage.notifyDataSetChanged();
                            else
                                adapterVMessage.notifyItemRangeChanged(mList.size() - list.size(), list.size());

                            recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (page != 1) {
                                MyApplication.showToast("暂无更多消息");
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
    public int getContentViewId() {
        return R.layout.layout_message;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

}
