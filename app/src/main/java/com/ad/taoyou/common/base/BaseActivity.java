package com.ad.taoyou.common.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.utils.AppManager;
import com.ad.taoyou.common.utils.DisplayUtil;
import com.ad.taoyou.common.view.MyCustomDialog;

/**
 * Created by sunweike on 2017/8/25.
 */

public abstract class BaseActivity extends FragmentActivity implements BaseActivityInterface, View.OnClickListener {
    public abstract int getContentViewId();

    protected abstract void initAllMembersView(Bundle savedInstanceState);

    public static int state = 0;

    protected MyCustomDialog myUniversalDialog;
    protected TextView mDilogTvMessage, mDialogTitle;
    protected Button btn_2;
    protected Context mContext;
    protected int mCurrentOrientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        if (state == 0) {
            //禁止横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            //禁止竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        mCurrentOrientation = getResources().getConfiguration().orientation;
        setContentView(getContentViewId());
        mContext = this;
        initAllMembersView(savedInstanceState);
        findViewById(R.id.btn_back_game).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back_game) {
            AppManager.getAppManager().finishActivity();
        } else if (view.getId() == R.id.btn_back) {
            AppManager.getAppManager().finishActivity();
        } else if (view.getId() == R.id.btn_2) {
            myUniversalDialog.dismiss();
        }
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

    protected void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    protected void showSoftImput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            v.requestFocus();
            imm.showSoftInput(v, 0);
        }
    }

}
