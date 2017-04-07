package cn.igallery.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import cn.enid.igallery.R;
import cn.igallery.Configuration;
import cn.igallery.job.ImageThumbnailJob;
import cn.igallery.job.Job;
import cn.igallery.job.RxJob;
import cn.igallery.model.ImageModel;


/**
 * Created by enid on 2016/9/9.
 */
public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ImageModel> mData;
    private OnItemOnClickListener mOnItemOnClickListener;
    private Configuration mConfiguration;

    public interface OnItemOnClickListener {
        void onItemClick(View v, int position);

        void onItemCheck();
    }

    public ImageGridAdapter(Context context, Configuration configuration) {
        this.mContext = context;
        this.mConfiguration = configuration;
        this.mData = (ArrayList<ImageModel>) configuration.getImageList();
    }

    public void setData(Configuration configuration) {
        this.mConfiguration = configuration;
        mData = (ArrayList<ImageModel>) configuration.getImageList();
        notifyDataSetChanged();
    }

    public void setOnItemOnClickListener(OnItemOnClickListener listener) {
        this.mOnItemOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gv_frag_image, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ImageModel imageModel = mData.get(position);
        //设置是否显示checkBox
        if (mConfiguration.getChoiceModel() == Configuration.ImageChoiceModel.MULTIPLE) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(mConfiguration.getSelectedList() != null && mConfiguration.getSelectedList().contains(imageModel));//设置checkBox是否是选中状态
            holder.checkBox.setOnClickListener(new CheckBoxClickListener(mData.get(position)));
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        //如果大缩略图或小缩略图不存在，则去创建
        if (!new File(imageModel.getThumbnailBigPath()).exists() || !new File(imageModel.getThumbnailSmallPath()).exists()) {
            Job job = new ImageThumbnailJob(mContext, imageModel);
            RxJob.getInstance().addJob(job);
        }

        //显示图片
        String path = imageModel.getThumbnailSmallPath();
        if (TextUtils.isEmpty(path)) {
            path = imageModel.getThumbnailBigPath();
        }
        if (TextUtils.isEmpty(path)) {
            path = imageModel.getOriginalPath();
        }
        Glide.with(mContext)
                .load(path)
                .centerCrop()
                .into(holder.imageView);
        //图片是否为选择状态
        if (mConfiguration.getSelectedList().contains(imageModel)){
            holder.checkBox.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    //checkBox click listener
    private class CheckBoxClickListener implements View.OnClickListener {
        ImageModel mImageModel;
        CheckBoxClickListener(ImageModel imageModel) {
            this.mImageModel = imageModel;
        }

        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                String message = mConfiguration.addSelectImage(mImageModel);
                if (!TextUtils.isEmpty(message)) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
                if (!mConfiguration.getSelectedList().contains(mImageModel)) {
                    checkBox.setChecked(false);
                }
            } else {
                mConfiguration.removeSelectImage(mImageModel);
            }
            if (mOnItemOnClickListener != null) {
                mOnItemOnClickListener.onItemCheck();
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        CheckBox checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemOnClickListener != null) {
                mOnItemOnClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
