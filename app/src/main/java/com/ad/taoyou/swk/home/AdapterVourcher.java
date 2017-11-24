package com.ad.taoyou.swk.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.view.SecClickListener;

import java.util.List;

/**
 * Created by sunweike on 2017/8/24.
 */

public class AdapterVourcher extends RecyclerView.Adapter {
    public static final int MY_VOURCHER = 1; //我的代金劵
    public static final int EXCHANGE_VOURCHER = 2;//兑换代金劵
    private Context mContext;
    private List<VouchersInfo> mList;
    private SecClickListener mSecClickListener;
    private int state;

    public AdapterVourcher(Context context, List<VouchersInfo> list, int state) {
        mContext = context;
        mList = list;
        this.state = state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_voucher, parent, false);
        return new AdapterVourcher.ViewHolder(view, mSecClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        VouchersInfo data = mList.get(position);

        viewHolder.mTvTime.setText(data.getTime());
        viewHolder.mTvTitle.setText(data.getTitle());
        viewHolder.mTvContent.setText(data.getContent());
        if (state == 1) {
            viewHolder.btn_recharge.setText(mContext.getResources().getString(R.string.recharge));
            viewHolder.bg_rl.setBackgroundResource(R.mipmap.ic_back_bg);

            viewHolder.line.setVisibility(View.GONE);
            viewHolder.mTvMoney.setVisibility(View.GONE);
            viewHolder.mTvCount.setVisibility(View.GONE);
        } else {
            viewHolder.btn_recharge.setText(mContext.getResources().getString(R.string.exchange));
            viewHolder.bg_rl.setBackgroundResource(R.mipmap.ic_bg_big);

            viewHolder.mTvCount.setText("剩余" + data.getCount() + "张");
            viewHolder.mTvMoney.setText(Html.fromHtml("需要<font color=#ffee2d>" + data.getMoney() + "</font>淘豆"));

            viewHolder.line.setVisibility(View.VISIBLE);
            viewHolder.mTvMoney.setVisibility(View.VISIBLE);
            viewHolder.mTvCount.setVisibility(View.VISIBLE);

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

        private TextView mTvTitle, mTvTime, mTvContent, btn_recharge, mTvCount, mTvMoney;
        private LinearLayout bg_rl;
        private View v, line;
        private SecClickListener mSecClickListener;

        public ViewHolder(View v, SecClickListener secClickListener) {
            super(v);
            this.v = v;
            mSecClickListener=secClickListener;
            mTvTime = (TextView) v.findViewById(R.id.tv_time);
            mTvTitle = (TextView) v.findViewById(R.id.tv_title);
            mTvContent = (TextView) v.findViewById(R.id.tv_content);
            mTvCount = (TextView) v.findViewById(R.id.tv_count);
            mTvMoney = (TextView) v.findViewById(R.id.tv_money);
            btn_recharge = (TextView) v.findViewById(R.id.btn_recharge);
            bg_rl = (LinearLayout) v.findViewById(R.id.bg_rl);
            line = v.findViewById(R.id.line);

            v.findViewById(R.id.btn_recharge).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mSecClickListener != null) {
                mSecClickListener.onSecClick(view, getAdapterPosition());
            }
        }

    }
}
