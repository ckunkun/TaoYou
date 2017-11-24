package com.ad.taoyou.swk.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.view.SecClickListener;

import java.util.List;

/**
 * Created by sunweike on 2017/8/24.
 */

public class AdapterRecords extends RecyclerView.Adapter {
    public static final int RECHAERGE = 1; //充值记录
    public static final int CONSUMPTION = 2;//消费记录

    private Context mContext;
    private List<Recordsinfo> mList;
    private SecClickListener mSecClickListener;
    private int state;

    public AdapterRecords(Context context, List<Recordsinfo> list, int state) {
        mContext = context;
        mList = list;
        this.state = state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_records, parent, false);
        return new AdapterRecords.ViewHolder(view, mSecClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Recordsinfo data = mList.get(position);

        viewHolder.mTvTime.setText(data.getCreateTime());
        viewHolder.mTvMoney.setText(data.getTitle());

        if (state == RECHAERGE) {
            viewHolder.mTvContent.setVisibility(View.GONE);
            viewHolder.mTvMsg.setVisibility(View.GONE);
        } else {
            viewHolder.mTvContent.setVisibility(View.VISIBLE);
            viewHolder.mTvMsg.setVisibility(View.VISIBLE);
            viewHolder.consumption_view.setVisibility(View.VISIBLE);
            viewHolder.recharge_view.setVisibility(View.GONE);
            viewHolder.mTvContent.setText(data.getDesc());
            viewHolder.mTvMsg.setText(data.getDesc2());
        }

    }

    public void setSecClickListener(SecClickListener mSecClickListener) {
        this.mSecClickListener = mSecClickListener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTvTime, mTvMoney, mTvContent, mTvMsg;
        private RelativeLayout rl;
        private View v, consumption_view,recharge_view;
        private SecClickListener mSecClickListener;

        public ViewHolder(View v, SecClickListener secClickListener) {
            super(v);
            this.v = v;
            mSecClickListener = secClickListener;
            mTvTime = (TextView) v.findViewById(R.id.tv_time);
            mTvMoney = (TextView) v.findViewById(R.id.tv_money);
            mTvContent = (TextView) v.findViewById(R.id.tv_content);
            mTvMsg = (TextView) v.findViewById(R.id.tv_msg);
            consumption_view = v.findViewById(R.id.consumption_view);
            recharge_view = v.findViewById(R.id.recharge_view);

        }

        @Override
        public void onClick(View view) {
            if (mSecClickListener != null) {
                mSecClickListener.onSecClick(view, getAdapterPosition());
            }
        }

    }
}
