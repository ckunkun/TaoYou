package com.ad.taoyou.swk.gift;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class FragmentGift extends BaseFragment implements SecClickListener {
    public static final String TAG = "FragmentGift";
    private static final String ARG_PARAM = "param";
    private String mParam;
    private int recommendPage = 0, allPage = 0, myPage = 0;

    public TextView mTvRecommend;
    public TextView mTvAll;
    public TextView mTvMy;

    public PullToRefreshRecyclerView recommendRecyclerView;
    public PullToRefreshRecyclerView allRecyclerView;
    public PullToRefreshRecyclerView myRecyclerView;

    private List<GiftInfo> recommendList = new ArrayList<>();
    private List<GiftInfo> allList = new ArrayList<>();
    private List<GiftInfo> myList = new ArrayList<>();
    private AdapterGift recommendAdapter, allAdapter, myAdapter;
    private TextView mGiftToast;

    public static FragmentGift newInstance(String param) {
        FragmentGift fragment = new FragmentGift();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_gift;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mTvRecommend = (TextView) mRootView.findViewById(R.id.tv_recommend);
        mTvAll = (TextView) mRootView.findViewById(R.id.tv_all);
        mTvMy = (TextView) mRootView.findViewById(R.id.tv_my);
        recommendRecyclerView = (PullToRefreshRecyclerView) mRootView.findViewById(R.id.recommend_recyclerView);
        allRecyclerView = (PullToRefreshRecyclerView) mRootView.findViewById(R.id.all_recyclerView);
        myRecyclerView = (PullToRefreshRecyclerView) mRootView.findViewById(R.id.my_recyclerView);

        mGiftToast = (TextView) mRootView.findViewById(R.id.gift_toast);
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
                if (intent.getIntExtra("to", 0) == 1) {
                    //礼包详情的查看我的礼包
                    toMyGift();
                    mRootView.findViewById(R.id.bg).setVisibility(View.VISIBLE);
                } else {
                    //回退显示view
                    mRootView.findViewById(R.id.bg).setVisibility(View.VISIBLE);
                }
            }
        }

    };

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_go_receive || i == R.id.tv_recommend) {//推荐礼包
            clear();
            mTvRecommend.setTextColor(getActivity().getResources().getColor(R.color.white));
            if (recommendList.size() != 0) {
                recommendRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mGiftToast.setText("暂时还没有礼包哦~!");
                mRootView.findViewById(R.id.btn_go_receive).setVisibility(View.INVISIBLE);
                mRootView.findViewById(R.id.view_recommend).setVisibility(View.VISIBLE);
                mRootView.findViewById(R.id.my_empty).setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.tv_all) {//全部礼包
            clear();
            mTvAll.setTextColor(getActivity().getResources().getColor(R.color.white));
            if (allList.size() != 0) {
                allRecyclerView.setVisibility(View.VISIBLE);
            } else {

                mGiftToast.setText("暂时还没有礼包哦~!");
                mRootView.findViewById(R.id.btn_go_receive).setVisibility(View.INVISIBLE);

                mRootView.findViewById(R.id.view_all).setVisibility(View.VISIBLE);
                mRootView.findViewById(R.id.my_empty).setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.tv_my) {//我的礼包
            toMyGift();

        }
    }

    @Override
    public void initView() {
        mTvRecommend.setOnClickListener(this);
        mTvAll.setOnClickListener(this);
        mTvMy.setOnClickListener(this);
        mRootView.findViewById(R.id.btn_go_receive).setOnClickListener(this);
    }


    public void toMyGift() {
        clear();
        mTvMy.setTextColor(getActivity().getResources().getColor(R.color.white));
        mRootView.findViewById(R.id.view_my).setVisibility(View.VISIBLE);
        if (myList.size() != 0)
            myRecyclerView.setVisibility(View.VISIBLE);
        else

            mGiftToast.setText("你还没有领取礼包哦~!");
        mRootView.findViewById(R.id.btn_go_receive).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.my_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        //第一次进来显示推荐礼包
        RecyclerView recyclerView1 = recommendRecyclerView.getRefreshableView();
        recommendAdapter = new AdapterGift(mContext, recommendList, AdapterGift.RECOMMEND);
        recommendAdapter.setSecClickListener(this);
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView1.setAdapter(recommendAdapter);

        //全部礼包
        RecyclerView recyclerView2 = allRecyclerView.getRefreshableView();
        allAdapter = new AdapterGift(mContext, allList, AdapterGift.ALL);
        allAdapter.setSecClickListener(this);
        recyclerView2.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView2.setAdapter(allAdapter);

        //我的礼包
        RecyclerView recyclerView3 = myRecyclerView.getRefreshableView();
        myAdapter = new AdapterGift(mContext, myList, AdapterGift.MY);
        myAdapter.setSecClickListener(this);
        recyclerView3.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView3.setAdapter(myAdapter);

        recommendRecyclerView.setVisibility(View.VISIBLE);
        allRecyclerView.setVisibility(View.GONE);
        myRecyclerView.setVisibility(View.GONE);

        getRecommendGift();
        getAllGift();
        getMyGift();

        PullToRefreshBase.OnRefreshListener2 refreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                if (recommendRecyclerView.getVisibility() == View.VISIBLE) {
                    recommendPage = 0;
                    recommendList.clear();
                    getRecommendGift();
                } else if (allRecyclerView.getVisibility() == View.VISIBLE) {
                    allPage = 0;
                    allList.clear();
                    getAllGift();
                } else {
                    myPage = 0;
                    myList.clear();
                    getMyGift();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                if (recommendRecyclerView.getVisibility() == View.VISIBLE) {
                    getRecommendGift();
                } else if (allRecyclerView.getVisibility() == View.VISIBLE) {
                    getAllGift();
                } else {
                    getMyGift();
                }
            }
        };

        recommendRecyclerView.setOnRefreshListener(refreshListener2);
        allRecyclerView.setOnRefreshListener(refreshListener2);
        myRecyclerView.setOnRefreshListener(refreshListener2);

    }


    public void clear() {
        mTvRecommend.setTextColor(getActivity().getResources().getColor(R.color.TranslucentWhite));
        mTvAll.setTextColor(getActivity().getResources().getColor(R.color.TranslucentWhite));
        mTvMy.setTextColor(getActivity().getResources().getColor(R.color.TranslucentWhite));

        mRootView.findViewById(R.id.view_recommend).setVisibility(View.GONE);
        mRootView.findViewById(R.id.view_all).setVisibility(View.GONE);
        mRootView.findViewById(R.id.view_my).setVisibility(View.GONE);

        recommendRecyclerView.setVisibility(View.GONE);
        allRecyclerView.setVisibility(View.GONE);
        myRecyclerView.setVisibility(View.GONE);

        mRootView.findViewById(R.id.my_empty).setVisibility(View.GONE);
    }

    @Override
    public void onSecClick(View view, int position) {
        if (view.getId() == R.id.btn_receive) {
            //领取礼包
            if (recommendRecyclerView.getVisibility() == View.VISIBLE) {
                if (!Boolean.valueOf(recommendList.get(position).getReceive())) {
                    mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putInt("state", 1);
                    bundle.putInt("giftId", Integer.valueOf(recommendList.get(position).getGiftId()));
                    skipFragment(FragmentGiftDetails.TAG, new FragmentGiftDetails(), this, bundle);
                }
            } else {
                if (!Boolean.valueOf(allList.get(position).getReceive())) {
                    mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putInt("state", 1);
                    bundle.putInt("giftId", Integer.valueOf(allList.get(position).getGiftId()));
                    skipFragment(FragmentGiftDetails.TAG, new FragmentGiftDetails(), this, bundle);
                }
            }

        } else if (view.getId() == R.id.btn_copy) {
            getCode(position);
        } else {
            //查看礼包详情
            mRootView.findViewById(R.id.bg).setVisibility(View.GONE);
            if (recommendRecyclerView.getVisibility() == View.VISIBLE) {
                Bundle bundle = new Bundle();
                bundle.putInt("state", 0);
                bundle.putInt("giftId", Integer.valueOf(recommendList.get(position).getGiftId()));
                skipFragment(FragmentGiftDetails.TAG, new FragmentGiftDetails(), this, bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("state", 0);
                bundle.putInt("giftId", Integer.valueOf(allList.get(position).getGiftId()));
                skipFragment(FragmentGiftDetails.TAG, new FragmentGiftDetails(), this, bundle);
            }
        }

    }

    private void getRecommendGift() {
        recommendPage++;
        String url = HttpTaskValues.API_POST_GIFT_RECOMMEND;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("p", recommendPage);
        params.addFormDataPart("s", 10);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, "1.7.1 推荐礼包接口" + rawJsonData);
                    try {
                        List<GiftInfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<GiftInfo>>() {
                                }.getType());

                        if (list.size() > 0) {
                            recommendList.addAll(list);
                            if (recommendList.size() == list.size())
                                recommendAdapter.notifyDataSetChanged();
                            else
                                recommendAdapter.notifyItemRangeChanged(recommendList.size() - list.size(), list.size());

                            recommendRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (recommendPage != 1) {
                                MyApplication.showToast("暂无更多礼包");
                                recommendRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                            recommendRecyclerView.setVisibility(View.GONE);
                            mGiftToast.setText("暂时还没有礼包哦~!");
                            mRootView.findViewById(R.id.btn_go_receive).setVisibility(View.INVISIBLE);

                            mRootView.findViewById(R.id.my_empty).setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recommendRecyclerView.onRefreshComplete();
                return super.onSuccess(jsonObject, msg);
            }
        });


    }

    private void getAllGift() {
        allPage++;
        String url = HttpTaskValues.API_POST_GIFT_ALL;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("p", allPage);
        params.addFormDataPart("s", 10);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, "1.7.2 所有礼包接口" + rawJsonData);
                    try {
                        List<GiftInfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<GiftInfo>>() {
                                }.getType());

                        if (list.size() > 0) {
                            allList.addAll(list);
                            if (allList.size() == list.size())
                                allAdapter.notifyDataSetChanged();
                            else
                                allAdapter.notifyItemRangeChanged(allList.size() - list.size(), list.size());

                            allRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (allPage != 1) {
                                MyApplication.showToast("暂无更多礼包");
                                allRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                allRecyclerView.onRefreshComplete();
                return super.onSuccess(jsonObject, msg);
            }
        });

    }

    private void getMyGift() {
        myPage++;
        String url = HttpTaskValues.API_POST_GIFT_MY;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("p", myPage);
        params.addFormDataPart("s", 10);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, "1.7.3 我的礼包接口" + rawJsonData);
                    try {
                        List<GiftInfo> list = new Gson().fromJson(
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("list"),
                                new TypeToken<List<GiftInfo>>() {
                                }.getType());


                        if (list.size() > 0) {
                            myList.addAll(list);
                            if (myList.size() == list.size())
                                myAdapter.notifyDataSetChanged();
                            else
                                myAdapter.notifyItemRangeChanged(myList.size() - list.size(), list.size());

                            myRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (myPage != 1) {
                                MyApplication.showToast("暂无更多礼包");
                                myRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                myRecyclerView.onRefreshComplete();
                return super.onSuccess(jsonObject, msg);
            }
        });

    }

    private void getCode(int position) {
        String url = HttpTaskValues.API_POST_GIFT_CODE;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("recId", myList.get(position).getRecId());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, "1.7.6 获取礼包码接口" + rawJsonData);
                    try {
                        //复制兑换码
                        // 得到剪贴板管理器
                        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(new org.json.JSONObject(jsonObject.get("data").toString()).getString("giftCode"));
                        MyApplication.showToast("兑换码复制成功");
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && FragmentReceiveGift.isOther) {
            FragmentReceiveGift.isOther = false;
            clear();
            mTvRecommend.setTextColor(getActivity().getResources().getColor(R.color.white));
            mRootView.findViewById(R.id.view_recommend).setVisibility(View.VISIBLE);
            recommendRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}

