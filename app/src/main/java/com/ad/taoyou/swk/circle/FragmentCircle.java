package com.ad.taoyou.swk.circle;

import android.os.Bundle;
import android.view.View;

import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;

/**
 * Created by sunweike on 2017/8/23.
 */

public class FragmentCircle extends BaseFragment {

    public static final String TAG = "FragmentCircle";
    private static final String ARG_PARAM = "param";
    private String mParam;

    public static FragmentCircle newInstance(String param) {
        FragmentCircle fragment = new FragmentCircle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
