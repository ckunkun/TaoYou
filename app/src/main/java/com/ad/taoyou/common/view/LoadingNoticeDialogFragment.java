package com.ad.taoyou.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.WindowManager;


/**
 * Created by 华硕 on 2016/10/12.
 */
public class LoadingNoticeDialogFragment extends DialogFragment {
    private static final String ROOT_VIEW_ID = "root_view_id";

    private int mRootViewId;

    public static LoadingNoticeDialogFragment newInstance(int rootViewId) {
        LoadingNoticeDialogFragment fragment = new LoadingNoticeDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ROOT_VIEW_ID, rootViewId);
        fragment.setArguments(args);
        return fragment;
    }

    public LoadingNoticeDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mRootViewId = getArguments().getInt(ROOT_VIEW_ID);
        }
        Dialog dialog;
        CustomDialog.Builder customBuilder = new
                CustomDialog.Builder(getActivity());
        customBuilder
                .setRootViewId(mRootViewId);
        dialog = customBuilder.create();


        // 设置透明背景
        Drawable d = new ColorDrawable(Color.TRANSPARENT);

        dialog.getWindow().setBackgroundDrawable(d);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(true);


        return dialog;
    }

}
