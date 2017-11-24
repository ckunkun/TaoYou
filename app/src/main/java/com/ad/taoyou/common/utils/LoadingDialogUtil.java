package com.ad.taoyou.common.utils;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ad.taoyou.R;
import com.ad.taoyou.common.view.LoadingNoticeDialogFragment;


/**
 * Created by 华硕 on 2016/10/12.
 */
public class LoadingDialogUtil {

    public static LoadingNoticeDialogFragment sLoadingDialog;

    public static void showLoadingDialog(FragmentActivity activity) {
        if (sLoadingDialog == null)
            createShowLoadingDialog(activity);
        else if (sLoadingDialog.getActivity() != activity) {
            dismissLoadingDialog();
            createShowLoadingDialog(activity);
        }
    }

    private static void createShowLoadingDialog(FragmentActivity activity) {
        sLoadingDialog = LoadingNoticeDialogFragment.newInstance(
                R.layout.view_loading_dialog);

        //这里直接调用show方法会报java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        //因为show方法中是通过commit进行的提交(通过查看源码)
        //这里为了修复这个问题，使用commitAllowingStateLoss()方法
        //注意：DialogFragment是继承自android.app.Fragment，这里要注意同v4包中的Fragment区分，别调用串了
        //DialogFragment有自己的好处，可能也会带来别的问题
        //sLoadingDialog.show(activity.getSupportFragmentManager(), "loading_dialog");

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(sLoadingDialog, "loading_dialog");
        ft.commitAllowingStateLoss();
    }

    public static void dismissLoadingDialog() {
        if (sLoadingDialog != null) {
            sLoadingDialog.dismissAllowingStateLoss();
            sLoadingDialog = null;
        }
    }
}
