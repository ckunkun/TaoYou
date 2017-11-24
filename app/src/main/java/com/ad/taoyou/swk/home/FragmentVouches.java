package com.ad.taoyou.swk.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.view.SecClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunweike on 2017/8/24.
 * 我的代金券
 */

public class FragmentVouches extends BaseFragment implements SecClickListener {
    public static final String TAG = "FragmentVouches";


    public RecyclerView recyclerView;
    public RecyclerView exchange_recyclerview;

    private AdapterVourcher adapterVVourcher, adapterExchangeVourcher;
    private List<VouchersInfo> mList = new ArrayList<>();
    private List<VouchersInfo> mExchangeList = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.fragment_vouches;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        exchange_recyclerview = (RecyclerView) mRootView.findViewById(R.id.exchange_recyclerview);
        initView();
        initData();

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, "");

        } else if (i == R.id.tv_my_voucher) {//我的代金券
            mRootView.findViewById(R.id.view_my_voucher).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.view_exchange_voucher).setVisibility(View.GONE);
            ((TextView) mRootView.findViewById(R.id.tv_my_voucher)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) mRootView.findViewById(R.id.tv_exchange_voucher)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
            isHaveRecord();
            exchange_recyclerview.setVisibility(View.GONE);

        } else if (i == R.id.btn_go_exchange || i == R.id.tv_exchange_voucher) {//去兑换

            //兑换代金券
            mRootView.findViewById(R.id.view_my_voucher).setVisibility(View.GONE);
            mRootView.findViewById(R.id.view_exchange_voucher).setVisibility(View.VISIBLE);
            ((TextView) mRootView.findViewById(R.id.tv_my_voucher)).setTextColor(getResources().getColor(R.color.TranslucentWhite));
            ((TextView) mRootView.findViewById(R.id.tv_exchange_voucher)).setTextColor(getResources().getColor(R.color.white));
            recyclerView.setVisibility(View.GONE);
            mRootView.findViewById(R.id.empty).setVisibility(View.GONE);
            exchange_recyclerview.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.tv_my_voucher).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_exchange_voucher).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_go_exchange).setOnClickListener(this);
    }

    @Override
    public void initData() {

        for (int i = 0; i < 10; i++) {
            VouchersInfo info = new VouchersInfo();
            info.setTime("2有效期至：2016-12-08");
            info.setContent("使用条件：游戏充值满500元可使用");
            info.setTitle("100元游戏充值代金劵");
            info.setMoney("30000");
            info.setCount("150");
            mList.add(info);
        }

        //第一次进来显示我的代金劵
        adapterVVourcher = new AdapterVourcher(mContext, mList, AdapterVourcher.MY_VOURCHER);
        adapterVVourcher.setSecClickListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapterVVourcher);

        //兑换代金劵
        adapterExchangeVourcher = new AdapterVourcher(mContext, mList, AdapterVourcher.EXCHANGE_VOURCHER);
        adapterExchangeVourcher.setSecClickListener(this);
        exchange_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        exchange_recyclerview.setAdapter(adapterExchangeVourcher);
    }

    private void isHaveRecord() {
        if (mList.size() <= 0) {
            recyclerView.setVisibility(View.GONE);
            mRootView.findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.empty).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSecClick(View view, int position) {
        if (((TextView) view.findViewById(view.getId())).getText().equals("充值")) {
            //充值
            MyApplication.showToast("充值" + position);
        } else {
            //兑换
            MyApplication.showToast("兑换" + position);
        }
    }
}
