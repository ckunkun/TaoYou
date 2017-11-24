package com.ad.taoyou.swk.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.common.view.SecClickListener;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/29.
 */
public class FragmentHelp extends BaseFragment implements SecClickListener {
    public static final String TAG = "FragmentHelp";

    private List<HelpInfo> mList = new ArrayList<>();
    private List<HelpInfo> helpInfos = new ArrayList<>();
    private RecyclerView hosRecycleView, helpRecycleView;
    private AdapterTypeHelp adapterTypeHelp;
    private AdapterHelp adapterHelp;
    private int mPosition = 0;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_help;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            backFragment(this, "");
        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);

        hosRecycleView = (RecyclerView) mRootView.findViewById(R.id.hos_recyclerview);
        adapterTypeHelp = new AdapterTypeHelp(mContext, mList);
        adapterTypeHelp.setSecClickListener(this);
        hosRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        hosRecycleView.setAdapter(adapterTypeHelp);

        helpRecycleView = (RecyclerView) mRootView.findViewById(R.id.help_recyclerview);
        adapterHelp = new AdapterHelp(mContext, helpInfos);
        adapterHelp.setSecClickListener(this);
        helpRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        helpRecycleView.setAdapter(adapterHelp);
    }

    @Override
    public void onSecClick(View view, int position) {
        if (view.getId() == R.id.btn_type) {
            helpInfos.clear();
            mList.get(mPosition).setClick(false);
            mPosition = position;
            mList.get(position).setClick(true);
            adapterTypeHelp.notifyDataSetChanged();
            getList(mList.get(position).getType());
        } else if (view.getId() == R.id.tv_title) {
            Bundle bundle = new Bundle();
            bundle.putString("id", helpInfos.get(position).getId());
            skipFragment(FragmentHelpDetails.TAG, new FragmentHelpDetails(), this, bundle);
        }
    }

    @Override
    public void initData() {
        getTypeList();
    }

    private void getTypeList() {
        String url = HttpTaskValues.API_POST_HELP_TYPE_LIST;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "2.1.1 帮助问题类型列表" + rawJsonData);
                    try {
                        List<HelpInfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<HelpInfo>>() {
                                }.getType());
                        mList.addAll(list);

                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setClick(i == 0 ? true : false);
                        }
                        adapterTypeHelp.notifyDataSetChanged();
                        getList(mList.get(0).getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    private void getList(String type) {
        String url = HttpTaskValues.API_POST_HELP_LIST;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("type", type);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "2.1.2 帮助问题列表" + rawJsonData);
                    try {
                        List<HelpInfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<HelpInfo>>() {
                                }.getType());
                        helpInfos.addAll(list);
                        adapterHelp.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }
}
