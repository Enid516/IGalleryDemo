package cn.igallery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Enid on 2016/9/14.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;


    }

    @Override
    public void onClick(View v) {

    }
}
