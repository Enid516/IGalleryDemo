package com.enid.igallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.enid.igallery.utils.ImageLoaderUtil;
import com.enid.igallerydemo.R;

import java.util.List;

import cn.igallery.model.ImageModel;
import cn.igallery.ui.adapter.RecyclerViewHolder;

/**
 * Created by enid on 2017/3/31.
 */

public class ImageSelectedAdapter extends RecyclerView.Adapter<ImageSelectedAdapter.ViewHolder> {
    private Context mContext;
    private List<ImageModel> mImageModelList;
    private OnDeleteListener mOnDeleteListener;
    public ImageSelectedAdapter(Context context, List<ImageModel> list) {
        this.mContext = context;
        this.mImageModelList = list;
    }

    public void setData(List<ImageModel> data) {
        this.mImageModelList = data;
    }
    public void setListener(OnDeleteListener listener){
        this.mOnDeleteListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_grid, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String path;
        ImageModel imageModel = mImageModelList.get(position);
        path = imageModel.getThumbnailSmallPath();
        if (TextUtils.isEmpty(path)) {
            path = imageModel.getThumbnailBigPath();
        }
        if (TextUtils.isEmpty(path)) {
            path = imageModel.getOriginalPath();
        }
        ImageLoaderUtil.getInstance().loadImage(mContext, path, holder.imageView);
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteListener != null) {
                    mOnDeleteListener.onDelete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageModelList == null ? 0 : mImageModelList.size();
    }

    class ViewHolder extends RecyclerViewHolder {
        private ImageView imageView;
        private ImageView iconDelete;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            iconDelete = (ImageView) view.findViewById(R.id.iconDelete);
        }
    }

    public interface OnDeleteListener{
        void onDelete(int position);
    }
}
