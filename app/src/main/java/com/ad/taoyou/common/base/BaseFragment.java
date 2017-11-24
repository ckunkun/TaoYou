package com.ad.taoyou.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.utils.DisplayUtil;
import com.ad.taoyou.common.utils.FragManager;
import com.ad.taoyou.common.view.MyCustomDialog;
import com.ad.taoyou.swk.activity.FragmentActivity;
import com.ad.taoyou.swk.gift.FragmentGift;
import com.ad.taoyou.swk.home.FragmentHome;

import java.util.List;

public abstract class BaseFragment extends Fragment implements BaseFragmentInterface, View.OnClickListener {
    public abstract int getContentViewId();

    protected abstract void initAllMembersView(Bundle savedInstanceState);

    protected View mRootView;
    protected Context mContext;

    protected MyCustomDialog myUniversalDialog;
    protected TextView mDilogTvMessage, mDialogTitle;
    protected Button btn_2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        mContext = getActivity();
        initAllMembersView(savedInstanceState);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void initDialog() {
        myUniversalDialog = new MyCustomDialog(mContext);
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.view_dialog, null);
        myUniversalDialog.setLayoutView(v);
        mDialogTitle = (TextView) v.findViewById(R.id.tv_title);
        mDilogTvMessage = (TextView) v.findViewById(R.id.tv_message);
        btn_2 = (Button) v.findViewById(R.id.btn_2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 80));
        params.gravity = Gravity.CENTER;
        mDilogTvMessage.setLayoutParams(params);
        btn_2.setOnClickListener(this);
    }

    /**
     * fragment之间的回退
     *
     * @param fragment 当前的fragment
     * @param TAG      回退到的fragment的TAG
     */
    public void backFragment(Fragment fragment, String TAG) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Fragment f = manager.findFragmentByTag(TAG);
        //判断是否是二级跳转
        if (!TextUtils.isEmpty(TAG))
            fragmentTransaction.show(f).hide(fragment).commit();
        else {
            //显示首页的view
            switch (FragManager.curItem) {
                case FragManager.TAB_HOME:
                    getActivity().sendBroadcast(new Intent(FragmentHome.TAG));
                    break;
                case FragManager.TAB_CIRCLE:
                    break;
                case FragManager.TAB_GIFT:
                    getActivity().sendBroadcast(new Intent(FragmentGift.TAG));
                    break;
                case FragManager.TAB_ACTIVITY:
                    getActivity().sendBroadcast(new Intent(FragmentActivity.TAG));
                    break;
            }
            fragmentTransaction.hide(fragment).commit();
        }
        //清除fragment
//        List<Fragment> fragments = manager.getFragments();
//        fragments.remove(fragment);
//        fragmentTransaction.remove(fragment);
        //销毁最上层fragment的TAG
        FragManager.getFragManager().finishFragment();
    }


    public void skipFragment(String TAG, Fragment toFragment, Fragment myFragment) {
        skipFragment(TAG, toFragment, myFragment, null);
    }

    /**
     * fragment之间的跳转
     *
     * @param TAG        跳转fragment的TAG
     * @param toFragment 跳转的fragment
     * @param myFragment 自己的fragment
     */
    public void skipFragment(String TAG, Fragment toFragment, Fragment myFragment, Bundle b) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        Fragment f = manager.findFragmentByTag(TAG);

        if (b != null) {
            toFragment.setArguments(b);
        }
        //判断是否第一次显示fragment
        if (f == null) {
            switch (FragManager.curItem) {
                case FragManager.TAB_HOME:
                    fragmentTransaction.add(R.id.container, toFragment, TAG);
                    break;
                case FragManager.TAB_CIRCLE:
//                    fragmentTransaction.add(R.id.container2, toFragment, TAG);
                    break;
                case FragManager.TAB_GIFT:
                    fragmentTransaction.add(R.id.container3, toFragment, TAG);
                    break;
                case FragManager.TAB_ACTIVITY:
                    fragmentTransaction.add(R.id.container4, toFragment, TAG);
                    break;
            }
            //判断是否是首页跳转
            if (myFragment != null)
                fragmentTransaction.hide(myFragment).commit();
            else
                fragmentTransaction.commit();
        } else {
            //判断是否是首页跳转
            if (myFragment != null)
                fragmentTransaction.show(f).hide(myFragment).commit();
            else
                fragmentTransaction.show(f).commit();
        }
        //记录跳转的fragment的TAG
        FragManager.getFragManager().addFragment(TAG);
    }


}
