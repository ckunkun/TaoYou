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
 * Created by sunweike on 2017/9/28.
 */

public class AdapterTypeHelp extends RecyclerView.Adapter {
    private Context mContext;
    private List<HelpInfo> mList;
    private SecClickListener mSecClickListener;

    public AdapterTypeHelp(Context context, List<HelpInfo> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_type_help, parent, false);
        return new AdapterTypeHelp.ViewHolder(view, mSecClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterTypeHelp.ViewHolder viewHolder = (AdapterTypeHelp.ViewHolder) holder;
        HelpInfo data = mList.get(position);
        if (data.isClick()) {
            viewHolder.btnType.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.btnType.setBackgroundResource(R.drawable.dra_pay_balance_check_bg);
        } else {
            viewHolder.btnType.setTextColor(mContext.getResources().getColor(R.color.TranslucentWhite));
            viewHolder.btnType.setBackgroundResource(R.drawable.dra_pay_balance_bg);
        }
        viewHolder.btnType.setText(data.getName());
    }

    public void setSecClickListener(SecClickListener mSecClickListener) {
        this.mSecClickListener = mSecClickListener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView btnType;
        private View v;
        private SecClickListener mSecClickListener;

        public ViewHolder(View v, SecClickListener secClickListener) {
            super(v);
            this.v = v;
            mSecClickListener = secClickListener;
            btnType = (TextView) v.findViewById(R.id.btn_type);
            btnType.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mSecClickListener != null) {
                mSecClickListener.onSecClick(view, getAdapterPosition());
            }
        }

    }
}
