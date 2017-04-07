package cn.igallery.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.enid.igallery.R;
import cn.igallery.model.BucketModel;
import cn.igallery.util.MediaUtil;


/**
 * Created by big_love on 2016/12/26.
 */

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.MyViewHolder> {
    private Context mContext;
    private List<BucketModel> mData;
    private OnItemOnClickListener mOnItemOnClickListener;
    private BucketModel mSelectedBucket;

    public BucketListAdapter(Context context, List<BucketModel> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void setSelectedBucket(BucketModel bucket) {
        this.mSelectedBucket = bucket;
    }

    public BucketModel getSelectedBucket() {
        return this.mSelectedBucket;
    }
    public interface OnItemOnClickListener {
        void onItemClick(BucketModel bucketModel, int position);
    }

    public void setOnItemOnClickListener(BucketListAdapter.OnItemOnClickListener listener) {
        this.mOnItemOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_bucket, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BucketModel bucketModel = mData.get(position);
        Glide.with(mContext)
                .load(bucketModel.getCover())
                .centerCrop()
                .into(holder.imageBucketCover);
        holder.textBucketName.setText(bucketModel.getBucketName());
        if (!bucketModel.getBucketId().equals(MediaUtil.ALL_IMAGES_BUCKETID)) {
            holder.textBucketImagesSize.setText(bucketModel.getImageCount() + "张");
        } else {
            holder.textBucketImagesSize.setText("");
        }
        if (mSelectedBucket.getBucketId().equals(bucketModel.getBucketId())) {
            holder.radioButtonBucketSelect.setVisibility(View.VISIBLE);
        } else {
            holder.radioButtonBucketSelect.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageBucketCover;
        TextView textBucketName,textBucketImagesSize;
        RadioButton radioButtonBucketSelect;

        MyViewHolder(View itemView) {
            super(itemView);
            imageBucketCover = (ImageView) itemView.findViewById(R.id.imageBucketCover);
            textBucketName = (TextView) itemView.findViewById(R.id.textBucketName);
            textBucketImagesSize = (TextView) itemView.findViewById(R.id.textBucketImagesSize);
            radioButtonBucketSelect = (RadioButton) itemView.findViewById(R.id.radioButtonBucketSelect);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemOnClickListener != null) {
                mOnItemOnClickListener.onItemClick(mData.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

}
