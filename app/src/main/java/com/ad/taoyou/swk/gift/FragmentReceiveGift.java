package com.ad.taoyou.swk.gift;

import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;


/**
 * Created by sunweike on 2017/8/30.
 */

public class FragmentReceiveGift extends BaseFragment {
    public static final String TAG = "FragmentReceiveGift";
    public static boolean isOther = false;
    private String giftCode, use;

    public TextView mTvCode;
    public TextView mTvUser;

    public FragmentReceiveGift() {
    }

    public FragmentReceiveGift(String giftCode, String use) {
        this.giftCode = giftCode;
        this.use = use;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_receivegift;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mTvCode = (TextView) mRootView.findViewById(R.id.tv_gift_code);
        mTvUser = (TextView) mRootView.findViewById(R.id.tv_user);
        initView();
        initData();
        initDialog();
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, FragmentGiftDetails.TAG);
        } else if (i == R.id.btn_copy) {//复制兑换码
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(giftCode);
            mDilogTvMessage.setText("兑换码复制成功");
            myUniversalDialog.show();

        } else if (i == R.id.btn_2) {
            myUniversalDialog.dismiss();
        } else if (i == R.id.btn_other_gift) {
            //领取其他礼包
            isOther = true;
            backFragment(this, FragmentGiftDetails.TAG);
        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_copy).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_other_gift).setOnClickListener(this);

    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(giftCode) && !TextUtils.isEmpty(use)) {
            mTvCode.setText("兑换码：" + giftCode);
            mTvUser.setText(use);
            mRootView.findViewById(R.id.success_ll).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.fail_ll).setVisibility(View.GONE);
        } else {
            mRootView.findViewById(R.id.success_ll).setVisibility(View.GONE);
            mRootView.findViewById(R.id.fail_ll).setVisibility(View.VISIBLE);
        }

    }
}
