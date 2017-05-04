package com.enid.igallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enid.igallery.adapter.ImageSelectedAdapter;
import com.enid.igallerydemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.igallery.model.ImageModel;

/**
 * Created by enid on 2017/4/7.
 */

public class ImageSelectedFragment extends Fragment {
    private Context mContext;

    private List<ImageModel> mList;

    private ImageSelectedAdapter mAdapter;

    private View rootView;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_image_selected, null);
        }
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));

        mList = new ArrayList<>();
        mAdapter = new ImageSelectedAdapter(mContext,mList);
        mAdapter.setListener(new ImageSelectedAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    public void refreshData(List<ImageModel> imageModels){
        this.mList = imageModels;
        mAdapter.setData(mList);
        mAdapter.notifyDataSetChanged();
    }
}
