package com.ad.taoyou.swk.gift;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.imageloder.UILUtil;
import com.ad.taoyou.common.view.RoundProgressBar;
import com.ad.taoyou.common.view.SecClickListener;

import java.util.List;

/**
 * Created by sunweike on 2017/8/30.
 */

public class AdapterGift extends RecyclerView.Adapter {
    public static final int RECOMMEND = 1; //推荐礼包
    public static final int ALL = 2;//全部礼包
    public static final int MY = 3;//我的礼包

    private Context mContext;
    private List<GiftInfo> mList;
    private SecClickListener mSecClickListener;
    private int state;

    public AdapterGift(Context context, List<GiftInfo> list, int state) {
        mContext = context;
        mList = list;
        this.state = state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gift, parent, false);
        return new ViewHolder(view, mSecClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterGift.ViewHolder viewHolder = (AdapterGift.ViewHolder) holder;
        GiftInfo data = mList.get(position);

        viewHolder.mTvTitle.setText(data.getGameName());
        viewHolder.mTvSubTitle.setText(data.getName());
        viewHolder.mTvContent.setText(data.getInfo());

        if (Integer.valueOf(data.getTotal())==0){
            viewHolder.mImgReceive.setVisibility(View.GONE);
        }else{
            viewHolder.mImgReceive.setVisibility(View.VISIBLE);
            viewHolder.mImgReceive.setMax(Integer.valueOf(data.getTotal()));
            viewHolder.mImgReceive.setProgress(Integer.valueOf(data.getTotal()) - Integer.valueOf(data.getCount()));
        }
        UILUtil.getLoader(mContext).displayImage(data.getLogoPath(), viewHolder.mImgIcon, UILUtil.normalOption());
        if (state == MY) {
            viewHolder.mbtnReceive.setVisibility(View.GONE);
            viewHolder.mBtnCopy.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mbtnReceive.setVisibility(View.VISIBLE);
            viewHolder.mBtnCopy.setVisibility(View.GONE);
            if (!Boolean.valueOf(data.getReceive())) {
                //未领取
                viewHolder.mImgReceive.setRoundProgressColor(Color.parseColor("#7ccfff"));
                viewHolder.mImgReceive.setRoundColor(Color.parseColor("#7b7ccfff"));
//                viewHolder.mImgReceive.setImageResource(R.mipmap.ic_no_receive);
                viewHolder.mTvReceive.setText("领");
                viewHolder.mTvReceive.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                //已领取
                viewHolder.mImgReceive.setRoundProgressColor(Color.parseColor("#c2bebd"));
                viewHolder.mImgReceive.setRoundColor(Color.parseColor("#7bc2bebd"));
//                viewHolder.mImgReceive.setImageResource(R.mipmap.ic_receive);
                viewHolder.mTvReceive.setText("已领");
                viewHolder.mTvReceive.setTextColor(mContext.getResources().getColor(R.color.TranslucentWhite));
            }
        }

        //最后一条隐藏line
        viewHolder.line.setVisibility(position == mList.size() - 1 ? View.GONE : View.VISIBLE);

    }

    public void setSecClickListener(SecClickListener mSecClickListener) {
        this.mSecClickListener = mSecClickListener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTvTitle, mTvSubTitle, mTvContent, mTvReceive,mBtnCopy;
        private ImageView mImgIcon;
        private RoundProgressBar mImgReceive;
        private RelativeLayout mbtnReceive;
        private View v, line;
        private SecClickListener mSecClickListener;

        public ViewHolder(View v, SecClickListener secClickListener) {
            super(v);
            this.v = v;
            mSecClickListener = secClickListener;
            mTvTitle = (TextView) v.findViewById(R.id.tv_title);
            mTvSubTitle = (TextView) v.findViewById(R.id.tv_subtitle);
            mTvContent = (TextView) v.findViewById(R.id.tv_content);
            mTvReceive = (TextView) v.findViewById(R.id.tv_receive);
            mBtnCopy = (TextView) v.findViewById(R.id.btn_copy);
            mbtnReceive = (RelativeLayout) v.findViewById(R.id.btn_receive);
            mImgIcon = (ImageView) v.findViewById(R.id.img_icon);
            mImgReceive = (RoundProgressBar) v.findViewById(R.id.img_receive);
            line = v.findViewById(R.id.view);

            mbtnReceive.setOnClickListener(this);
            mBtnCopy.setOnClickListener(this);
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
