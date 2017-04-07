package cn.igallery.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enid on 2016/9/14.
 */
public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected List<T> mListModel = new ArrayList<T>();
    protected ViewGroup mParent;
    protected LayoutInflater mInflater;
    protected Activity mActivity;
    protected EnumAdapterMode mMode = EnumAdapterMode.SINGLE;
    protected View mView;

    /**
     * 单选模式时候被选中的项
     */
    protected int mSelectedPosition = -1;
    /**
     * 多选模式时候被选中的项集合
     */
    protected List<T> mListSelectedModel = new ArrayList<T>();

    /**
     * 返回adapter的模式（多选，单选）
     *
     * @return
     */
    public EnumAdapterMode getmMode() {
        return mMode;
    }

    /**
     * 设置adapter的模式（多选，单选）
     *
     * @param mode
     */
    public void setmMode(EnumAdapterMode mode) {
        this.mMode = mode;
    }

    public void setmView(View mView) {
        this.mView = mView;
    }

    /**
     * 获得选中的项
     *
     * @return
     */
    public int getmSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * 获得选中的list实体集合
     *
     * @return
     */
    public List<T> getmListSelectedModel() {
        return mListSelectedModel;
    }

    /**
     * 设置选中的项
     *
     * @param position 项的位置
     * @param selected true选中，false未选中
     */
    public void setmSelectedPosition(int position, boolean selected) {
        if (mListModel != null && position >= 0 && position < mListModel.size()) {
            switch (getmMode()) {
                case SINGLE:
                    if (mSelectedPosition >= 0) {
                        onSelectedChange(mSelectedPosition, false, false);
                    }
                    if (selected) {
                        mSelectedPosition = position;
                    } else {
                        mSelectedPosition = -1;
                    }
                    onSelectedChange(position, selected, true);
                    break;
                case MULTI:
                    T model = mListModel.get(position);
                    if (selected) {
                        if (!mListSelectedModel.contains(model)) {
                            mListSelectedModel.add(model);
                            onSelectedChange(position, true, true);
                        }
                    } else {
                        if (mListSelectedModel.contains(model)) {
                            mListSelectedModel.remove(model);
                            onSelectedChange(position, false, true);
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 当调用public void setmSelectedPosition(int position, boolean
     * selected)方法后，会回调这个方法，次方法用来重写，改变实体状态
     *
     * @param position 项位置
     * @param selected true选中，false未选中
     * @param notify   是否需要刷新adapter
     */
    protected void onSelectedChange(int position, boolean selected, boolean notify) {

    }

    public RecyclerViewAdapter(Activity activity,List<T> listModel) {
        setData(listModel);
        this.mActivity = activity;
        this.mInflater = mActivity.getLayoutInflater();
    }

    /**
     * 获得adapter的实体集合
     *
     * @return
     */
    public List<T> getData() {
        return mListModel;
    }

    /**
     * 更新adapter的数据集合，并刷新adapter
     *
     * @param listModel
     */
    public void updateData(List<T> listModel) {
        setData(listModel);
        this.notifyDataSetChanged();
    }

    /**
     * 给adapter设置数据
     *
     * @param listModel
     */
    public void setData(List<T> listModel) {
        if (listModel != null) {
            this.mListModel = listModel;
        } else {
            this.mListModel = new ArrayList<T>();
        }
    }

//    @Override
    public int getCount() {
        if (mListModel != null) {
            return mListModel.size();
        } else {
            return 0;
        }
    }

//    @Override
    public T getItem(int position) {
        if (mListModel != null && position >= 0 && mListModel.size() > position) {
            return mListModel.get(position);
        } else {
            return null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public View initConvertView(int layoutResId, View convertView) {
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResId, null);
        }
        return convertView;
    }

//    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, getItem(position));
    }

    public View getView(int position, View convertView, ViewGroup parent, T model) {
        return convertView;
    }

    public void getViewUpdate(int position, View convertView, ViewGroup parent) {
        mParent = parent;
        convertView.setId(position);
    }

    /**
     * 更新某一item项的布局，要在getview中调用getViewUpdate(int position, View convertView,
     * ViewGroup parent)方法后此方法才有效
     *
     * @param position
     */
    public void updateItem(int position) {
        if (mParent != null && position >= 0 && position < getCount()) {
            View itemView = mParent.findViewById(position);
            if (itemView != null) {
                getView(position, itemView, mParent);
            }
        }
    }

    public enum EnumAdapterMode {
        /**
         * 单选模式
         */
        SINGLE,
        /**
         * 多选模式
         */
        MULTI;
    }

    class MyViewHolder extends RecyclerViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }


}
