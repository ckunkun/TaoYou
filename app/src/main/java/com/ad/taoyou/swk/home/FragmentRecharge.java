package com.ad.taoyou.swk.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.DateUtil;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.common.view.ConstansPopupWindow;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/24.
 * 充值记录
 */

public class FragmentRecharge extends BaseFragment {
    public static final String TAG = "FragmentRecharge";

    public PullToRefreshRecyclerView recyclerView;
    public PullToRefreshRecyclerView consumptionRecyclerView;
    public TextView mTvTime;

    private AdapterRecords adapterVRecords, adapterConsumption;
    private List<Recordsinfo> rechargeList = new ArrayList<>();
    private List<Recordsinfo> consumptionList = new ArrayList<>();
    private int rechargePage = 0, consumptionPage = 0;
    private long beginTime, endTime;//默认一周


    @Override
    public int getContentViewId() {
        return R.layout.fragment_recharge;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        recyclerView = (PullToRefreshRecyclerView) mRootView.findViewById(R.id.recyclerview);
        consumptionRecyclerView = (PullToRefreshRecyclerView) mRootView.findViewById(R.id.consumption_recyclerview);
        mTvTime = (TextView) mRootView.findViewById(R.id.tv_time);
        initView();
        initData();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, "");

        } else if (i == R.id.tv_recharge) {//充值记录
            mRootView.findViewById(R.id.view_recharge).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.view_consumption).setVisibility(View.GONE);
            ((TextView) mRootView.findViewById(R.id.tv_recharge)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) mRootView.findViewById(R.id.tv_consumption)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
            consumptionRecyclerView.setVisibility(View.GONE);
            mRootView.findViewById(R.id.consumption_empty).setVisibility(View.GONE);

            //无充值记录显示
            if (rechargeList.size() <= 0) {
                mRootView.findViewById(R.id.recharge_empty).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                mRootView.findViewById(R.id.recharge_empty).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.tv_consumption) {//消费记录
            mRootView.findViewById(R.id.view_recharge).setVisibility(View.GONE);
            mRootView.findViewById(R.id.view_consumption).setVisibility(View.VISIBLE);
            ((TextView) mRootView.findViewById(R.id.tv_recharge)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
            ((TextView) mRootView.findViewById(R.id.tv_consumption)).setTextColor(getResources().getColor(R.color.white));

            recyclerView.setVisibility(View.GONE);
            mRootView.findViewById(R.id.recharge_empty).setVisibility(View.GONE);

            //无消费记录显示
            if (consumptionList.size() <= 0) {
                mRootView.findViewById(R.id.consumption_empty).setVisibility(View.VISIBLE);
                consumptionRecyclerView.setVisibility(View.GONE);
            } else {
                mRootView.findViewById(R.id.consumption_empty).setVisibility(View.GONE);
                consumptionRecyclerView.setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.tv_time) {//打开浮窗选择时间排序
            pop.start();

        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.tv_recharge).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_consumption).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_time).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);

        endTime = System.currentTimeMillis();
        beginTime = endTime - 86400 * 1000 * 7;
    }

    @Override
    public void initData() {

        initPopup();
        getRecharge();
        getConsumption();
        //充值记录
        RecyclerView recyclerView1 = recyclerView.getRefreshableView();
        adapterVRecords = new AdapterRecords(mContext, rechargeList, AdapterRecords.RECHAERGE);
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView1.setAdapter(adapterVRecords);

        //消费记录
        RecyclerView recyclerView2 = consumptionRecyclerView.getRefreshableView();
        adapterConsumption = new AdapterRecords(mContext, consumptionList, AdapterRecords.CONSUMPTION);
        recyclerView2.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView2.setAdapter(adapterConsumption);

        recyclerView.setVisibility(View.VISIBLE);
        consumptionRecyclerView.setVisibility(View.GONE);

        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                rechargePage = 0;
                rechargeList.clear();
                getRecharge();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getRecharge();
            }
        });

        consumptionRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                consumptionPage = 0;
                consumptionList.clear();
                getConsumption();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getConsumption();
            }
        });

    }

    private void getRecharge() {
        rechargePage++;
        String url = HttpTaskValues.API_POST_RECHARGE_LIST;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("p", rechargePage);
        params.addFormDataPart("s", 10);
//        params.addFormDataPart("beginDate", beginTime);
//        params.addFormDataPart("endDate", endTime);
        Log.i(TAG, "测试 充值记录列表" + params);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, "1.9.2 充值记录列表" + rawJsonData);
                    try {
                        List<Recordsinfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<Recordsinfo>>() {
                                }.getType());

                        if (list.size() > 0) {
                            rechargeList.addAll(list);
                            if (rechargeList.size() == list.size())
                                adapterVRecords.notifyDataSetChanged();
                            else
                                adapterVRecords.notifyItemRangeChanged(rechargeList.size() - list.size(), list.size());

                            recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (rechargePage != 1) {
                                MyApplication.showToast("暂无更多充值记录");
                                recyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                        }

                        //无充值记录显示
                        if (rechargeList.size() <= 0) {
                            mRootView.findViewById(R.id.recharge_empty).setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            mRootView.findViewById(R.id.recharge_empty).setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
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

    private void getConsumption() {
        consumptionPage++;
        String url = HttpTaskValues.API_POST_CONSUME_LIST;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("p", consumptionPage);
        params.addFormDataPart("s", 10);
        params.addFormDataPart("beginDate", beginTime);
        params.addFormDataPart("endDate", endTime);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.9.3 消费记录列表" + rawJsonData);
                    try {
                        List<Recordsinfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<Recordsinfo>>() {
                                }.getType());

                        if (list.size() > 0) {
                            consumptionList.addAll(list);
                            if (consumptionList.size() == list.size())
                                adapterConsumption.notifyDataSetChanged();
                            else
                                adapterConsumption.notifyItemRangeChanged(consumptionList.size() - list.size(), list.size());

                            consumptionRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (consumptionPage != 1) {
                                MyApplication.showToast("暂无更多充值记录");
                                consumptionRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                consumptionRecyclerView.onRefreshComplete();
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    private View view;
    private ConstansPopupWindow pop;

    /**
     * 更换封面pop
     */
    private void initPopup() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // 引入窗口配置文件
        view = inflater.inflate(R.layout.view_time_pop, null);
        View.OnClickListener popListerner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.close();
                int i = view.getId();
                Date date = null;
                try {
                    date = DateUtil.yyyy_MM_dd_HH_mm_ss.parse(DateUtil.yyyy_MM_dd_HH_mm_ss.format(endTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (i == R.id.btn_delete || i == R.id.top_view) {
                } else if (i == R.id.btn_week) {//一周
                    mTvTime.setText("最近一周");
                    Date date1 = DateUtil.addDay(date, -7);
                    beginTime = date1.getTime();
                } else if (i == R.id.btn_month) {//一月
                    mTvTime.setText("最近一月");
                        Date date1 = DateUtil.addMonth(date, -1);
                        beginTime = date1.getTime();
                } else if (i == R.id.btn_three_month) {//三月
                    mTvTime.setText("最近三月");
                        Date date1 = DateUtil.addMonth(date, -3);
                        beginTime = date1.getTime();
                } else if (i == R.id.btn_year) {//一年
                    mTvTime.setText("最近一年");
                        Date date1 = DateUtil.addYear(date, -1);
                        beginTime = date1.getTime();
                }
                Log.e(TAG, "beginTime:" + beginTime + ";endTime:" + endTime);
                if (recyclerView.getVisibility() == View.VISIBLE || mRootView.findViewById(R.id.recharge_empty).getVisibility() == View.VISIBLE) {
                    rechargePage = 0;
                    rechargeList.clear();
                    getRecharge();
                } else {
                    consumptionPage = 0;
                    consumptionList.clear();
                    getConsumption();
                }
            }
        };

        view.findViewById(R.id.btn_delete).setOnClickListener(popListerner);
        view.findViewById(R.id.btn_week).setOnClickListener(popListerner);
        view.findViewById(R.id.btn_month).setOnClickListener(popListerner);
        view.findViewById(R.id.btn_three_month).setOnClickListener(popListerner);
        view.findViewById(R.id.btn_year).setOnClickListener(popListerner);
        view.findViewById(R.id.top_view).setOnClickListener(popListerner);

        pop = new ConstansPopupWindow(mContext,
                mRootView.findViewById(R.id.bg_ll), view);
    }

}
