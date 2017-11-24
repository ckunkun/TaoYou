package com.ad.taoyou.swk.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.SplashActivity;
import com.ad.taoyou.common.imageloder.UILUtil;
import com.ad.taoyou.common.view.SecClickListener;

import java.util.List;

/**
 * Created by sunweike on 2017/8/29.
 */

public class AdapterActivity extends RecyclerView.Adapter {

    private Context mContext;
    private List<ActivityInfo> mList;
    private SecClickListener mSecClickListener;

    public AdapterActivity(Context context, List<ActivityInfo> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view, mSecClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ActivityInfo data = mList.get(position);

        if (SplashActivity.state == 0) {
            viewHolder.mImgIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ((int) (MyApplication.getScreensWidth() * 150.f / 492.f))));
        }
        viewHolder.mTvTitle.setText(data.getName());
        viewHolder.mTvTime.setText("活动时间：" + data.getBeginTime() + " - " + data.getEndTime());
        UILUtil.getLoader(mContext).displayImage(data.getTitleImg(), viewHolder.mImgIcon, UILUtil.normalOption());
    }

    public void setSecClickListener(SecClickListener mSecClickListener) {
        this.mSecClickListener = mSecClickListener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTvTime, mTvTitle;
        private ImageView mImgIcon;
        private View v;
        private SecClickListener mSecClickListener;

        public ViewHolder(View v, SecClickListener secClickListener) {
            super(v);
            this.v = v;
            mSecClickListener = secClickListener;
            mTvTime = (TextView) v.findViewById(R.id.tv_time);
            mTvTitle = (TextView) v.findViewById(R.id.tv_title);
            mImgIcon = (ImageView) v.findViewById(R.id.img_icon);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mSecClickListener != null) {
                mSecClickListener.onSecClick(view, getAdapterPosition());
            }
        }

    }
}
