package com.fyales.android.demo.rx.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fyales.android.demo.R;
import com.fyales.android.demo.imageloader.ImageUtils;
import com.fyales.android.demo.model.Image;
import com.fyales.android.demo.rx.ImageDetailActivity;
import com.fyales.android.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author fyales
 * @since 2017/6/15
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Image> mImageList;
    private Context mContext;

    public ImageAdapter(Context context) {
        mContext = context;
        mImageList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        final Image imageBean = mImageList.get(position);

        final Context context = ((MyHolder) holder).mImageView.getContext();
        myHolder.mImageView.getLayoutParams().width = Utils.getWindowWidth(context) / 3;
        myHolder.mImageView.getLayoutParams().height = (int) (Math.random() * Utils.dp2px(context, 150)
                + Utils.dp2px(context, 100));

        ImageUtils.getInstance().display(myHolder.mImageView, imageBean.url);
        myHolder.mViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailActivity.class);
                intent.putExtra(ImageDetailActivity.URL, imageBean.url);
                View transitionView = v;
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) mContext, transitionView, "transitionName");

                ActivityCompat.startActivity(mContext, intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    public void addData(List<Image> imageList) {

        int start = mImageList.size();
        int count = imageList.size();

        mImageList.addAll(imageList);
        //避免每次加载更多时整个页面都刷新的问题
        notifyItemRangeChanged(start, count);
    }

    public void setData(List<Image> imageList) {
        mImageList.clear();
        mImageList.addAll(imageList);
        notifyDataSetChanged();
    }

    protected class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageview)
        public ImageView mImageView;
        @BindView(R.id.viewgroup)
        public ViewGroup mViewGroup;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
