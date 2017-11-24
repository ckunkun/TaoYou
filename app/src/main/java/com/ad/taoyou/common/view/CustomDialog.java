package com.ad.taoyou.common.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.taoyou.R;

/**
 * Created by 华硕 on 2016/10/12.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context) {
        super(context);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;

        private View contentView;
        private int rootViewId;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setRootViewId(int rootViewId) {
            this.rootViewId = rootViewId;
            return this;
        }

        /**
         * Create the custom dialog
         */
        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.Dialog);
            if (rootViewId != 0) {
                View layout = inflater.inflate(rootViewId, null);
                dialog.addContentView(layout, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                // set the dialog title
                dialog.setContentView(layout);
            }
            return dialog;
        }

    }

}