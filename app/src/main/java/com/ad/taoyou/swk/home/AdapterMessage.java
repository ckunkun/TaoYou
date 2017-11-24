package com.ad.taoyou.swk.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.view.SecClickListener;

import java.util.List;

/**
 * Created by sunweike on 2017/8/30.
 */

public class AdapterMessage extends RecyclerView.Adapter {

    private Context mContext;
    private List<MessageInfo> mList;
    private SecClickListener mSecClickListener;

    public AdapterMessage(Context context, List<MessageInfo> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        return new AdapterMessage.ViewHolder(view, mSecClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterMessage.ViewHolder viewHolder = (AdapterMessage.ViewHolder) holder;
        MessageInfo data = mList.get(position);
        viewHolder.mTvTitle.setText(data.getTitle());
        viewHolder.mTvTime.setText(data.getCreateTime());
        viewHolder.mTvFrom.setText(data.getSenderName());

        viewHolder.dian.setVisibility(!Boolean.valueOf(data.getRead()) ? View.VISIBLE : View.GONE);

    }

    public void setSecClickListener(SecClickListener mSecClickListener) {
        this.mSecClickListener = mSecClickListener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTvTime, mTvTitle, mTvFrom;
        private View v, dian;
        private SecClickListener mSecClickListener;

        public ViewHolder(View v, SecClickListener secClickListener) {
            super(v);
            this.v = v;
            mSecClickListener = secClickListener;
            mTvTime = (TextView) v.findViewById(R.id.tv_time);
            mTvTitle = (TextView) v.findViewById(R.id.tv_title);
            mTvFrom = (TextView) v.findViewById(R.id.tv_from);
            dian = v.findViewById(R.id.view_read);
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
