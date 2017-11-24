package com.ad.taoyou.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ad.taoyou.R;

@SuppressLint("NewApi")
public class ConstansPopupWindow extends PopupWindow {
    private View layout;
    private ViewGroup parent;

    public ConstansPopupWindow(Context context, View parent, View view) {
        this.parent = (ViewGroup) parent;
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);// ȡ�ý���
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.popupwindow_animation);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        layout = new View(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setVisibility(View.GONE);
        layout.setBackgroundColor(Color.parseColor("#00000000"));
        layout.setLayoutParams(params);
        this.parent.addView(layout);

        this.setContentView(view);

        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                close();
            }
        });

    }

    public void start() {
        layout.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(layout, "alpha", 0f, 1f).setDuration(300)
                .start();
        this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public void showpp() {
        layout.setVisibility(View.VISIBLE);
        this.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void close() {
        this.dismiss();
        ObjectAnimator ofFloat = ObjectAnimator
                .ofFloat(layout, "alpha", 1f, 0f);
        ofFloat.setDuration(300);
        ofFloat.start();
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                layout.setVisibility(View.GONE);
            }
        });
    }

}
